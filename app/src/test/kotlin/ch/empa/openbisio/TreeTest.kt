/*
 * Copyright 2023 Simone Baffelli
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package ch.empa.openbisio

import ch.empa.openbisio.interfaces.ListTree
import ch.empa.openbisio.interfaces.iterateWithParent
import kotlin.test.Test
import kotlin.test.assertEquals

data class CodeEntity(val name: String, val path: List<String>)

fun TreeFactory(): ListTree<CodeEntity> {
    return ListTree(
        CodeEntity("root", listOf()), listOf(
            ListTree(
                CodeEntity("a", listOf()), listOf(
                    ListTree(
                        CodeEntity("b", listOf()), listOf(
                            ListTree(CodeEntity("c", listOf()), listOf()),
                            ListTree(CodeEntity("d", listOf()), listOf())
                        )
                    )
                )
            ),
            ListTree(
                CodeEntity("e", listOf()), listOf(
                    ListTree(
                        CodeEntity("f", listOf()), listOf(
                            ListTree(CodeEntity("g", listOf()), listOf()),
                            ListTree(CodeEntity("h", listOf()), listOf())
                        )
                    )
                )
            )
        )
    )
}

class TraversalTest {
    val testTree = TreeFactory()

    @Test
    fun testDepthFirst() {
        val trav = testTree.iterator().asSequence().toList()
        assertEquals(
            trav , listOf(
                CodeEntity("root", listOf()),
                CodeEntity("a", listOf()),
                CodeEntity("b", listOf()),
                CodeEntity("c", listOf()),
                CodeEntity("d", listOf()),
                CodeEntity("e", listOf()),
                CodeEntity("f", listOf()),
                CodeEntity("g", listOf()),
                CodeEntity("h", listOf())
            )
        )
    }
}

class PathTest {
    val testTree = TreeFactory()

    @Test
    fun testPath() {
        val tr1 = iterateWithParent(
            testTree,
            { entity: CodeEntity, parent: ListTree<CodeEntity> ->
                CodeEntity(
                    entity.name,
                    parent.value().path + entity.name
                )
            },
            { entity: CodeEntity, children: List<ListTree<CodeEntity>> -> ListTree(entity, children) },
            CodeEntity("root", listOf())
        )
        //Check that all the paths are correct
        val correctPaths = listOf(
            listOf("root"),
            listOf("root", "a"),
            listOf("root", "a", "b"),
            listOf("root", "a", "b", "c"),
            listOf("root", "a", "b", "d"),
            listOf("root", "e"),
            listOf("root", "e", "f"),
            listOf("root", "e", "f", "g"),
            listOf("root", "e", "f", "h")
        )
        val paths = tr1.iterator().asSequence().map { it.path }.toList()
        assertEquals(paths , correctPaths)
    }
}


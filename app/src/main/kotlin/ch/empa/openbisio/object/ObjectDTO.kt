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

package ch.empa.openbisio.`object`

import ch.empa.openbisio.hierarchy.HierarchicalDTO
import ch.empa.openbisio.identifier.ConcreteIdentifier
import ch.empa.openbisio.interfaces.CodeHolder
import ch.empa.openbisio.interfaces.Identifier
import ch.empa.openbisio.interfaces.RelationshipHolder
import ch.empa.openbisio.interfaces.Tree
import kotlinx.serialization.Serializable

@Serializable
data class ObjectDTO(
    override val code: String,
    val type: String,
    val properties: Map<String, String> = mapOf(),
    override val children: List<Identifier>? = listOf(),
    override val parents: List<Identifier>? = listOf()
) : RelationshipHolder, HierarchicalDTO, Tree<HierarchicalDTO>, CodeHolder {
    override fun getChild(name: String): CodeHolder? {
        TODO("Not yet implemented")
    }

    override fun getParent(name: String): CodeHolder? {
        TODO("Not yet implemented")
    }

    override fun value(): ObjectDTO {
        return this
    }

    override fun hasChildren(): Boolean {
        return false
    }

    override fun children(): Collection<Tree<HierarchicalDTO>> {
        return listOf()
    }

    override fun updateCode(code: String): ObjectDTO {
        return this.copy(code = code)
    }


    override fun toEntity(): ObjectEntity {
        return ObjectEntity(this, ConcreteIdentifier.SampleIdentifier(listOf(this.code)))
    }


}




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

package ch.empa.openbisio.interfaces

import kotlin.collections.Collection






data class Tree<T>(private val value: T, private val children: List<Tree<T>>){

    fun value(): T = value
    fun hasChildren(): Boolean = children.isNotEmpty()

    fun <R> map(transformer: (T) -> R): Tree<R> {
        val mapped = children.map { it.map(transformer) }
        return Tree(transformer(value), mapped)
    }

    fun <R> cata(transformer: (T, List<R>) -> R): R {
        val mapped = children.map { it.cata(transformer) }
        return transformer(value, mapped)
    }

}





fun main(){
    val tr = Tree<Int>(1, listOf(Tree(2, listOf(Tree(3, listOf(Tree(4, listOf())))))))
    val res = tr.map { it + 1 }
    val transformer = { value: Int, children: List<Int> ->  value + children.sum()}
    val res1 = tr.cata(transformer)
    println(res1)
}

main()


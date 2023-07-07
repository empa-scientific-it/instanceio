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

typealias Algebra<T, R> = (T, List<R>) -> R
interface Tree<T> {
    fun value(): T
    fun hasChildren(): Boolean
    fun children(): Collection<Tree<T>>

    fun <R> map(transformer: (T) -> R): Tree<R>

    fun <R> cata(transformer: Algebra<T, R>): R {
        return when (hasChildren()) {
            true -> transformer(value(), emptyList())
            false -> {
                val mapped = children().map { it.cata(transformer) }
                transformer(value(), mapped)
            }
        }
    }

}

class ListTree<T>(val value: T, val children: List<ListTree<T>>): Tree<T> {

    override fun value(): T = value
    override fun hasChildren(): Boolean = children.isNotEmpty()
    override fun children(): Collection<ListTree<T>> = children


    override fun <R> map(transformer: (T) -> R): ListTree<R> {
        return when (hasChildren()) {
            true -> ListTree(transformer(value), listOf())
            false -> ListTree(transformer(value), children().map { it.map(transformer) })
        }
    }

    override fun toString(): String {
        fun helper(tree: Tree<T>, indent: Int): String {
            val spaces = " ".repeat(indent)
            when (tree.hasChildren()) {
                true -> return spaces + tree.value().toString()
                false -> return spaces + tree.value().toString() + "\n" + tree.children()
                    .joinToString("\n") { helper(it, indent + 2) }
            }
        }
        return helper(this, 0)
    }


}




fun <T, R> iterateWithParentHelper(tree: ListTree<T>, parent: ListTree<R>, transformer: (T, ListTree<R>) -> R): ListTree<R> {
    return when (tree.hasChildren()) {
        false -> {
            val mapped = transformer(tree.value(), parent)
            ListTree(mapped, listOf())
        }
        true -> {
            val mapped = ListTree(transformer(tree.value(), parent), listOf())
            val updatedChildren = tree.children().map { iterateWithParentHelper(it, mapped, transformer) }
            ListTree(mapped.value(), updatedChildren)
        }
    }
}

fun <R, T> iterateWithParent(tree: ListTree<T>, transformer: (T, ListTree<R>) -> R, seed: R): ListTree<R> {
    return iterateWithParentHelper(tree, ListTree(seed, emptyList()), transformer)
}

fun updateIdentifiers(tree: ListTree<Entity>): ListTree<Entity> {
    return iterateWithParent(tree, { entity, parent -> entity.copy(identifier = parent.value().identifier + entity.code) }, tree.value())
}







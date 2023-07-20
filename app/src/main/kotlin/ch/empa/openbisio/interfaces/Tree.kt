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

typealias Algebra<T, R> = (T, List<R>) -> R

interface Tree<T> {
    fun value(): T
    fun hasChildren(): Boolean
    fun children(): Collection<Tree<T>>



    fun <R> cata(transformer: Algebra<T, R>): R {
        return when (hasChildren()) {
            false -> transformer(value(), emptyList())
            true -> {
                val mapped = children().map { it.cata(transformer) }
                transformer(value(), mapped)
            }
        }
    }

    fun pprint(): String {
        fun helper(tree: Tree<T>, indent: Int): String {
            val spaces = " ".repeat(indent)
            when (tree.hasChildren()) {
                false -> return spaces + tree.value().toString()
                true -> return spaces + tree.value().toString() + "\n" + tree.children()
                    .joinToString("\n") { helper(it, indent + 2) }
            }
        }
        return helper(this, 0)
    }

    fun <R> flatMap(transform: (T) -> R): List<R> {
        return when (hasChildren()) {
            false -> listOf(transform(value()))
            true -> listOf(transform(value())) + children().flatMap { it.flatMap(transform) }
        }
    }


}


fun <T, R, U : Tree<R>, V : Tree<T>> iterateWithParentHelper(
    tree: V,
    parent: U,
    transformer: (T, U) -> R,
    builder: (R, List<U>) -> U
): U {
    return when (tree.hasChildren()) {
        false -> {
            val mapped = transformer(tree.value(), parent)
            builder(mapped, listOf())
        }

        true -> {
            val mapped = builder(transformer(tree.value(), parent), listOf())
            val updatedChildren = tree.children().map { iterateWithParentHelper(it, mapped, transformer, builder) }
            builder(mapped.value(), updatedChildren)
        }
    }
}


fun <R : Tree<T>, T> toListTreee(input: R): ListTree<T> {
    return ListTree(input.value(), input.children().map { toListTreee(it) })
}


fun <T, R, U : Tree<R>, V : Tree<T>> iterateWithParent(
    tree: V,
    transformer: (T, U) -> R,
    builder: (R, List<U>) -> U,
    seed: R
): U {
    return iterateWithParentHelper(tree, builder(seed, listOf()), transformer, builder)
}






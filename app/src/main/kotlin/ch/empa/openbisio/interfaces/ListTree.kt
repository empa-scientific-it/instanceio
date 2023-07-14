package ch.empa.openbisio.interfaces

/**
 * A tree implemented as list of lists
 * @param T the type of the value contained in the tree
 * @property value the value contained in the tree
 * @property children the children of the tree
 */
class ListTree<T>(val value: T, val children: List<ListTree<T>>) : Tree<T> {

    override fun value(): T = value
    override fun hasChildren(): Boolean = children.isNotEmpty()
    override fun  children(): Collection<ListTree<T>> = children
    fun filter(predicate: (T) -> Boolean): ListTree<T> {
        return when (hasChildren()) {
            false -> if (predicate(value)) this else ListTree(value, listOf())
            true -> {
                val filteredChildren = children().map { it.filter(predicate) }.filter { it.hasChildren() }
                if (predicate(value)) ListTree(value, filteredChildren) else ListTree(value, filteredChildren)
            }
        }
    }


    fun <R> map(transformer: (T) -> R): ListTree<R> {
        return when (hasChildren()) {
            false -> ListTree(transformer(value), listOf())
            true -> ListTree(transformer(value), children().map { it.map(transformer) })
        }
    }


    fun iterator() = object : Iterator<T> {
        private var elements = flatMap { it }
        private var iterator = elements.iterator()


        override fun hasNext(): Boolean {
            return iterator.hasNext()
        }

        override fun next(): T {
            return iterator.next()
        }
    }

    override fun toString(): String {
        return pprint()
    }


}
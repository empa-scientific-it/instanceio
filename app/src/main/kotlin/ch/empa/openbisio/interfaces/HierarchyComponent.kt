package ch.empa.openbisio.interfaces

interface HierarchyComponent : ICreatable {
    val children: MutableList<out HierarchyComponent>?
    val ancestorCodes: ArrayDeque<String>
    val identifier: HierarchyIdentifier



//    fun add(el: IHierarchyComponent) {
//        children?.add(el)
//    }
//
//    fun remove(el: IHierarchyComponent) {
//        children?.remove(el)
//    }




    fun getChild(code: String): HierarchyComponent? {
        return children?.find { it.code  == code}
    }

    fun updateCodes(): Unit {
        if (children == null) return else {
            children?.forEach { it ->
                it.ancestorCodes.addFirst(code)
                it.ancestorCodes.addAll(ancestorCodes)
                it.updateCodes()
                it.ancestorCodes.reverse()
            }


        }

    }


}
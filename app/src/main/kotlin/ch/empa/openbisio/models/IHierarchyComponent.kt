package ch.empa.openbisio.models

interface IHierarchyComponent : ICreatable {
    val children: MutableList<out IHierarchyComponent>?
    val ancestorCodes: ArrayDeque<String>
    val identifier: HierarchyIdentifier



//    fun add(el: IHierarchyComponent) {
//        children?.add(el)
//    }
//
//    fun remove(el: IHierarchyComponent) {
//        children?.remove(el)
//    }




    fun getChild(code: String): IHierarchyComponent? {
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
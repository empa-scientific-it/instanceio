package openbisio.models

import ch.ethz.sis.openbis.generic.asapi.v3.dto.common.create.IObjectCreation
import ch.ethz.sis.openbis.generic.asapi.v3.dto.common.operation.IOperation
import openbisio.OpenBISService
import openbisio.models.ICreatable
import kotlin.collections.ArrayDeque

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
                it.ancestorCodes?.addFirst(code)
                it.ancestorCodes.addAll(ancestorCodes)
                it.updateCodes()
                it.ancestorCodes.reverse()
            }


        }

    }


}
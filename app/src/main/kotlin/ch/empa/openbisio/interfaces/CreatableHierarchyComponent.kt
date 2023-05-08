package ch.empa.openbisio.interfaces

import ch.ethz.sis.openbis.generic.asapi.v3.dto.common.operation.IOperation
import ch.empa.openbisio.openbis.OpenBISService

interface CreatableHierarchyComponent: HierarchyComponent {
    override val children: MutableList<out CreatableHierarchyComponent>?
    fun createHierarchy(connection: OpenBISService): List<IOperation>{
        return create(connection) + children?.flatMap{it.createHierarchy(connection)}.orEmpty()
    }
}
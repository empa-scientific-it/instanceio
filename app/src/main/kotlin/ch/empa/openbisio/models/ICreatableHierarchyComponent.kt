package ch.empa.openbisio.models

import ch.ethz.sis.openbis.generic.asapi.v3.dto.common.operation.IOperation
import ch.empa.openbisio.OpenBISService

interface ICreatableHierarchyComponent: IHierarchyComponent {
    override val children: MutableList<out ICreatableHierarchyComponent>?
    fun createHierarchy(connection: OpenBISService): List<IOperation>{
        return create(connection) + children?.flatMap{it.createHierarchy(connection)}.orEmpty()
    }
}
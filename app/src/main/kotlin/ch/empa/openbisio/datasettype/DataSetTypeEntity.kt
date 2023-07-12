package ch.empa.openbisio.datasettype

import ch.empa.openbisio.interfaces.CreatableEntity
import ch.ethz.sis.openbis.generic.asapi.v3.dto.common.create.ICreation
import ch.ethz.sis.openbis.generic.asapi.v3.dto.dataset.create.DataSetTypeCreation

class DataSetTypeEntity(override val dto: DataSetTypeDTO) : CreatableEntity {
    override val identifier: DataSetTypeIdentifier = DataSetTypeIdentifier(dto.code)

    override fun persist(): List<ICreation> {
        val dataSetTypeCreation = DataSetTypeCreation().apply {
            this.code = identifier.identifier
            this.description = dto.description
            this.propertyAssignments = dto.propertyAssignments.flatMap { it.toEntity().persist() }
        }
        return listOf(dataSetTypeCreation)
    }
}
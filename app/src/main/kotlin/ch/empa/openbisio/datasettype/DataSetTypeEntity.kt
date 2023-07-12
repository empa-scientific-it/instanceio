package ch.empa.openbisio.datasettype

import ch.empa.openbisio.interfaces.CreatableEntity
import ch.empa.openbisio.interfaces.DTO
import ch.empa.openbisio.interfaces.Identifier
import ch.ethz.sis.openbis.generic.asapi.v3.dto.common.create.ICreation
import ch.ethz.sis.openbis.generic.asapi.v3.dto.dataset.create.DataSetTypeCreation

class DataSetTypeEntity(override val dto: DataSetTypeDTO) : CreatableEntity {
    override val identifier: DataSetTypeIdentifier = DataSetTypeIdentifier(dto.code)

    override fun persist(): ICreation {
        val dataSetTypeCreation = DataSetTypeCreation().apply {
            this.code = identifier.identifier
            this.description = dto.description
            this.propertyAssignments = dto.propertyAssignments.map { it.toEntity().persist() }
        }
        return dataSetTypeCreation
    }
}
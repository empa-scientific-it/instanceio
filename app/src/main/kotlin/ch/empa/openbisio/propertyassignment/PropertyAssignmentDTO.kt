package ch.empa.openbisio.propertyassignment

import ch.empa.openbisio.datatype.DataTypeDTO
import ch.empa.openbisio.interfaces.DTO
import ch.empa.openbisio.interfaces.Entity
import kotlinx.serialization.Serializable


@Serializable
data class PropertyAssignmentDTO(override val code: String, val section: String, val mandatory: Boolean, val type: DataTypeDTO):
    DTO {
    override fun toEntity(): Entity {
        TODO("Not yet implemented")
    }
}

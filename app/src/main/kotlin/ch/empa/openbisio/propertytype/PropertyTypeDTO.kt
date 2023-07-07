package ch.empa.openbisio.propertytype

import ch.empa.openbisio.datatype.DataTypeDTO
import ch.empa.openbisio.interfaces.DTO
import ch.empa.openbisio.interfaces.Entity
import kotlinx.serialization.Serializable

@Serializable
data class PropertyTypeDTO(override val code: String, val label: String, val description: String, val dataType: DataTypeDTO): DTO {
    override fun toEntity(): Entity {
        TODO("Not yet implemented")
    }
}

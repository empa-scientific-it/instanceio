package ch.empa.openbisio.propertytype

import ch.empa.openbisio.datatype.DataType
import ch.empa.openbisio.interfaces.DTO
import kotlinx.serialization.Serializable

@Serializable
data class PropertyTypeDTO(override val code: String, val label: String, val description: String, val dataType: DataType): DTO

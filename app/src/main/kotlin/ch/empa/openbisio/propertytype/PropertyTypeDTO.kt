package ch.empa.openbisio.propertytype

import ch.empa.openbisio.datatype.DataType
import kotlinx.serialization.Serializable

@Serializable
data class PropertyTypeDTO(val code: String, val label: String, val description: String, val dataType: DataType)

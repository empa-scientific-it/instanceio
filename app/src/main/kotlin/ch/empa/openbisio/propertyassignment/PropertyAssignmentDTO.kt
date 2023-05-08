package ch.empa.openbisio.propertyassignment

import ch.empa.openbisio.datatype.DataType
import ch.empa.openbisio.interfaces.CodeHolder

data class PropertyAssignmentDTO(override val code: String, val section: String, val mandatory: Boolean, val type: DataType): CodeHolder

package ch.empa.openbisio.objectype

import ch.empa.openbisio.interfaces.DTO
import ch.empa.openbisio.propertyassignment.PropertyAssignmentVariants
import kotlinx.serialization.Serializable

@Serializable
data class ObjectTypeDTO(override val code: String, val propertyAssignments: List<PropertyAssignmentVariants>): DTO

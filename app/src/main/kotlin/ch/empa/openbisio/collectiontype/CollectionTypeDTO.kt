package ch.empa.openbisio.collectiontype

import ch.empa.openbisio.interfaces.DTO
import ch.empa.openbisio.propertyassignment.PropertyAssignmentDTO

data class CollectionTypeDTO(override val code: String, val properties: List<PropertyAssignmentDTO>?): DTO

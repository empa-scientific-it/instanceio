package ch.empa.openbisio.collectiontype

import ch.empa.openbisio.propertyassignment.PropertyAssignmentDTO

data class CollectionTypeDTO(val code: String, val properties: List<PropertyAssignmentDTO>?)

package ch.empa.openbisio.propertyassignment

import ch.empa.openbisio.propertytype.PropertyType
import kotlinx.serialization.Serializable

@Serializable
sealed class PropertyAssignmentVariants{
    class NominalAssignment(val name: String)
    class LocalAssignment(val pt: PropertyType)
}
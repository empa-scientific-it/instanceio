package ch.empa.openbisio.collectiontype

import ch.empa.openbisio.interfaces.DTO
import ch.empa.openbisio.interfaces.Entity
import ch.empa.openbisio.objectype.SectionDTO
import ch.empa.openbisio.propertyassignment.PropertyAssignmentDTO
import kotlinx.serialization.Serializable

@Serializable data class CollectionTypeDTO(override val code: String, val description: String?,  val propertyAssignments: List<SectionDTO>): DTO {
    override fun toEntity(): Entity {
        TODO("Not yet implemented")
    }
}

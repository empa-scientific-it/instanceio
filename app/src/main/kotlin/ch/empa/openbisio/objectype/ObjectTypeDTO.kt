package ch.empa.openbisio.objectype

import ch.empa.openbisio.interfaces.DTO
import ch.empa.openbisio.interfaces.Entity
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ObjectTypeDTO(override val code: String, val description: String?, @SerialName("prefix") val generatedCodePrefix: String, @SerialName("properties") val propertyAssignments: List<SectionDTO>): DTO {
    override fun toEntity(): Entity {
        TODO("Not yet implemented")
    }
}

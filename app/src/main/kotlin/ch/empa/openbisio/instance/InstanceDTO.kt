package ch.empa.openbisio.instance

import ch.empa.openbisio.collectiontype.CollectionTypeDTO
import ch.empa.openbisio.interfaces.*
import ch.empa.openbisio.objectype.ObjectTypeDTO
import ch.empa.openbisio.propertytype.PropertyTypeDTO
import ch.empa.openbisio.space.SpaceDTO
import ch.empa.openbisio.vocabulary.VocabularyDTO
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class InstanceDTO(
    val spaces: List<SpaceDTO>?,
    @SerialName("property_types")val propertyTypes: List<PropertyTypeDTO>? = null,
    @SerialName("object_types")val objectTypes: List<ObjectTypeDTO>? = null,
    @SerialName("collection_types")val collectionTypes: List<CollectionTypeDTO>? = null,
    val vocabularies: List<VocabularyDTO>? = null,
): DTO {

    fun getSpace(code: String): SpaceDTO? {
        return spaces?.find { it.code == code }
    }

    override fun toEntity(): Entity {
        TODO("Not yet implemented")
    }

    override val code: String
        get() = "/"



}
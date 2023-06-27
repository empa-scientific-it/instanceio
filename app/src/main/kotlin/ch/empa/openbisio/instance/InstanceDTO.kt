package ch.empa.openbisio.instance

import ch.empa.openbisio.interfaces.ChildrenHolder
import ch.empa.openbisio.interfaces.DTO
import ch.empa.openbisio.interfaces.DomainObject
import ch.empa.openbisio.objectype.ObjectType
import ch.empa.openbisio.objectype.ObjectTypeDTO
import ch.empa.openbisio.propertytype.PropertyType
import ch.empa.openbisio.propertytype.PropertyTypeDTO
import ch.empa.openbisio.space.SpaceDTO
import ch.empa.openbisio.vocabulary.VocabularyDTO
import kotlinx.serialization.Serializable

@Serializable
data class InstanceDTO(
    val spaces: List<SpaceDTO>?,
    val propertyTypes: List<PropertyTypeDTO>? = null,
    val objectTypes: List<ObjectTypeDTO>? = null,
    val vocabularies: List<VocabularyDTO>? = null,
): ChildrenHolder, DTO {
    override fun getChildren(code: String): ChildrenHolder? {
        return spaces?.find { it.code == code }
    }

    override fun asDomainObject(): DomainObject {
        TODO("Not yet implemented")
    }

    override val code: String
        get() = "/"


}
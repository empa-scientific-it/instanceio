package ch.empa.openbisio.`object`

import ch.empa.openbisio.interfaces.*
import ch.empa.openbisio.objectype.ObjectTypeAdapter
import ch.empa.openbisio.property.PropertyValue
import kotlinx.serialization.Serializable

@Serializable
data class ObjectDTO(override val code: String, val type: String, val properties: Map<String, String>, override val children: List<Identifier>? = null,
                     override val parents: List<Identifier>? = null): RelationshipHolder, DTO, Tree<DTO> {
    override fun getChild(name: String): CodeHolder? {
        TODO("Not yet implemented")
    }

    override fun getParent(name: String): CodeHolder? {
        TODO("Not yet implemented")
    }

    override fun value(): DTO {
        return this
    }

    override fun hasChildren(): Boolean {
        return false
    }

    override fun children(): Collection<Tree<DTO>> {
       return listOf()
    }

    override fun toEntity(): Entity {
        return ObjectEntity(this, listOf())
    }


}




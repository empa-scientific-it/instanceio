package ch.empa.openbisio.`object`

import ch.empa.openbisio.interfaces.ChildrenHolder
import ch.empa.openbisio.interfaces.CodeHolder
import ch.empa.openbisio.interfaces.Identifier
import ch.empa.openbisio.interfaces.RelationshipHolder
import ch.empa.openbisio.property.PropertyValue
import kotlinx.serialization.Serializable

@Serializable
data class ObjectDTO(override val code: String, val properties: Map<String, PropertyValue>, override val children: List<Identifier>? = null,
                     override val parents: List<Identifier>? = null): RelationshipHolder, CodeHolder, ChildrenHolder {
    override fun getChild(name: String): CodeHolder? {
        TODO("Not yet implemented")
    }

    override fun getParent(name: String): CodeHolder? {
        TODO("Not yet implemented")
    }

    override fun getChildren(code: String): ChildrenHolder? {
        return null
    }

}



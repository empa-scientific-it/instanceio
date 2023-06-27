package ch.empa.openbisio.project

import ch.empa.openbisio.collection.CollectionDTO
import ch.empa.openbisio.interfaces.ChildrenHolder
import ch.empa.openbisio.interfaces.DTO
import kotlinx.serialization.Serializable

@Serializable
data class ProjectDTO(override val code: String, val collections: List<CollectionDTO>? = null): ChildrenHolder, DTO {
    override fun getChildren(code: String): ChildrenHolder? {
        return collections?.find { it.code == code }
    }
}

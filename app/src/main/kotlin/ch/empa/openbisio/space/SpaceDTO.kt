package ch.empa.openbisio.space

import ch.empa.openbisio.collection.CollectionDTO
import ch.empa.openbisio.interfaces.ChildrenHolder
import ch.empa.openbisio.project.ProjectDTO
import kotlinx.serialization.Serializable

@Serializable
data class SpaceDTO(override val code: String, val projects: List<ProjectDTO>? = null): ChildrenHolder {
    override fun getChildren(code: String): ChildrenHolder? {
        return projects?.find { it.code == code }
    }
}

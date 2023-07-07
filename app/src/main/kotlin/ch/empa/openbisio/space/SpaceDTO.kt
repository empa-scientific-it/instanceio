package ch.empa.openbisio.space

import ch.empa.openbisio.collection.CollectionDTO
import ch.empa.openbisio.interfaces.ChildrenHolder
import ch.empa.openbisio.interfaces.DTO
import ch.empa.openbisio.interfaces.Entity
import ch.empa.openbisio.interfaces.Tree
import ch.empa.openbisio.project.ProjectDTO
import kotlinx.serialization.Serializable

@Serializable
data class SpaceDTO(override val code: String, val projects: List<ProjectDTO> = listOf(), val description: String = ""):
    DTO, Tree<DTO> {


    override fun value(): DTO {
       return this
    }

    override fun hasChildren(): Boolean {
        return projects.isNotEmpty()
    }

    override fun children(): Collection<Tree<DTO>> {
        return projects
    }

    override fun toEntity(): Entity {
        TODO("Not yet implemented")
    }


}

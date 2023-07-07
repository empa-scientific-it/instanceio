package ch.empa.openbisio.project

import ch.empa.openbisio.collection.CollectionDTO
import ch.empa.openbisio.interfaces.DTO
import ch.empa.openbisio.interfaces.Entity
import ch.empa.openbisio.interfaces.Tree
import ch.empa.openbisio.person.PersonDTO
import kotlinx.serialization.Serializable

@Serializable
data class ProjectDTO(override val code: String, val collections: List<CollectionDTO> = listOf(), val description: String = "", val leader: PersonDTO = PersonDTO("a", "a", "a", "a", "a")): Tree<DTO>, DTO {
    override fun value(): DTO {
        return this
    }

    override fun hasChildren(): Boolean {
        return collections.isNotEmpty()
    }

    override fun children(): Collection<Tree<DTO>> {
        return collections ?: listOf()
    }

    override fun toEntity(): Entity {
        TODO("Not yet implemented")
    }

}

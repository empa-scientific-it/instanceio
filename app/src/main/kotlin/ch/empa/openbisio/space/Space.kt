package ch.empa.openbisio.space

import ch.empa.openbisio.interfaces.*
import ch.empa.openbisio.openbis.OpenBISService
import ch.empa.openbisio.person.Person
import ch.empa.openbisio.project.Project
import ch.ethz.sis.openbis.generic.asapi.v3.dto.common.operation.IOperation
import ch.ethz.sis.openbis.generic.asapi.v3.dto.space.Space
import ch.ethz.sis.openbis.generic.asapi.v3.dto.space.create.CreateSpacesOperation
import ch.ethz.sis.openbis.generic.asapi.v3.dto.space.create.SpaceCreation
import ch.ethz.sis.openbis.generic.asapi.v3.dto.space.fetchoptions.SpaceFetchOptions
import ch.ethz.sis.openbis.generic.asapi.v3.dto.space.search.SpaceSearchCriteria
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

@Serializable
data class Space(
    override val code: String,
    @Transient override var ancestorCodes: ArrayDeque<String> = ArrayDeque<String>(listOf()),
    val description: String? = null,
    @SerialName("projects") override val children: MutableList<Project>? = null,
    @Transient override val registrator: Person? = null
) : CreatableHierarchyComponent, RegistratorHolder {

    constructor(sp: Space) : this(
        sp.code,
        ArrayDeque<String>(listOf()),
        sp.description,
        sp.projects.map { Project(it) }.toMutableList(),
        Person(sp.registrator)
    )

    override val identifier: HierarchyIdentifier
        get() = ConcreteIdentifier.SpaceIdentifier(listOf(code))


    override fun createOperation(connection: OpenBISService): List<IOperation> {
        val sc = SpaceCreation().also {
            it.code = code
            it.description = description ?: ""
        }
        return listOf(CreateSpacesOperation(mutableListOf<SpaceCreation>(sc)))
    }


    override fun getFromAS(connection: OpenBISService): Space? {
        val sc = SpaceFetchOptions()
        val sid = SpaceSearchCriteria().withAndOperator().apply {
            withCode().thatEquals(code)
        }
        val result = connection.con.searchSpaces(connection.token, sid, sc)
        return if(result.objects.size > 0)  result.objects[0] else null
    }


}
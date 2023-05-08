package ch.empa.openbisio.project

import ch.empa.openbisio.collection.Collection
import ch.empa.openbisio.interfaces.*
import ch.empa.openbisio.openbis.OpenBISService
import ch.empa.openbisio.person.Person
import ch.ethz.sis.openbis.generic.asapi.v3.dto.common.operation.IOperation
import ch.ethz.sis.openbis.generic.asapi.v3.dto.project.Project
import ch.ethz.sis.openbis.generic.asapi.v3.dto.project.create.CreateProjectsOperation
import ch.ethz.sis.openbis.generic.asapi.v3.dto.project.create.ProjectCreation
import ch.ethz.sis.openbis.generic.asapi.v3.dto.project.fetchoptions.ProjectFetchOptions
import ch.ethz.sis.openbis.generic.asapi.v3.dto.project.id.ProjectIdentifier
import ch.ethz.sis.openbis.generic.asapi.v3.dto.project.search.ProjectSearchCriteria
import ch.ethz.sis.openbis.generic.asapi.v3.dto.space.id.SpacePermId
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

@Serializable
data class Project(
    override val code: String,
    val description: String? = null,
    var leader: Person? = null,
    @Transient override val ancestorCodes: ArrayDeque<String> = ArrayDeque(
        listOf()),
    @SerialName("collections") override val children: MutableList<Collection>?,
    @Transient override val registrator: Person? = null,
) : CreatableHierarchyComponent, RegistratorHolder {
    constructor(
        pr: Project
    ) : this(
        pr.code,
        pr.description,
        if (pr.leader != null) Person(pr.leader) else null,
        ArrayDeque(listOf()),
        pr.experiments.map { Collection(it) }.toMutableList(),
        Person(pr.registrator),
    )

    override val identifier: HierarchyIdentifier
        get() = ConcreteIdentifier.ProjectIdentifier(ancestorCodes + code)


    override fun createOperation(connection: OpenBISService): List<IOperation> {

        val pc = ProjectCreation().also {
            it.spaceId = SpacePermId(identifier.getAncestor()!!.identifier)
            it.code = code
            it.description = description
        }
        val prc = CreateProjectsOperation(pc)
        return listOf(prc)
    }

    override fun getFromAS(connection: OpenBISService): Project? {
        val fo = ProjectFetchOptions()
        val ps = ProjectSearchCriteria().withAndOperator().apply {  withId().thatEquals(ProjectIdentifier(identifier.identifier)) }
        val res = connection.con.searchProjects(connection.token, ps, fo)
        println(res)
        return if(res.objects.size > 0) res.objects[0] else null
    }



}
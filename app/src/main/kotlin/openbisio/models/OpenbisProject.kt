package openbisio.models

import ch.ethz.sis.openbis.generic.asapi.v3.IApplicationServerApi
import ch.ethz.sis.openbis.generic.asapi.v3.dto.project.Project
import ch.ethz.sis.openbis.generic.asapi.v3.dto.project.create.*
import ch.ethz.sis.openbis.generic.asapi.v3.dto.project.fetchoptions.*
import ch.ethz.sis.openbis.generic.asapi.v3.dto.project.id.ProjectIdentifier
import ch.ethz.sis.openbis.generic.asapi.v3.dto.space.id.SpacePermId
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
class OpenbisProject(
    override val code: String,
    override val ancestorsCodes: MutableList<String>? = null,
    val description: String?,
    var leader: OpenbisPerson?,
    @SerialName("collections") override val children: List<OpenbisCollection>?,
    override val registrator: OpenbisPerson
) : OpenbisIdentifiedObject() {
    constructor(
        pr: Project
    ) : this(
        pr.code,
        mutableListOf(),
        pr.description,
        if (pr.leader != null) OpenbisPerson(pr.leader) else null,
        pr.experiments.map { OpenbisCollection(it) },
        OpenbisPerson(pr.getRegistrator())
    )


    override fun createOperation(connection: IApplicationServerApi, token: String) {
        val pc = ProjectCreation().apply {
            this.spaceId = SpacePermId(ancestorsCodes!![0])
            this.code = code
            this.description = description
        }
        connection.createProjects(token, listOf(pc))
        println("creating ${code}")
    }

    override fun getFromOpenBIS(connection: IApplicationServerApi, token: String): Project? {
        val fo = ProjectFetchOptions()
        val pi = ProjectIdentifier(code, code)
        val res = connection.getProjects(token, listOf(pi), fo)
        return res[pi]
    }


}
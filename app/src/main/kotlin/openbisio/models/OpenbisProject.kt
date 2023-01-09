package openbisio.models

import ch.ethz.sis.openbis.generic.asapi.v3.IApplicationServerApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import kotlinx.serialization.SerialName

import ch.ethz.sis.openbis.generic.asapi.v3.dto.project.Project

import ch.ethz.sis.openbis.generic.asapi.v3.dto.project.create.*
import ch.ethz.sis.openbis.generic.asapi.v3.dto.project.fetchoptions.*
import ch.ethz.sis.openbis.generic.asapi.v3.dto.project.id.ProjectIdentifier


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
            this.code = code
            this.description = description
        }
        connection.createProjects(token, listOf(pc))
    }

    override fun getFromOpenBIS(connection: IApplicationServerApi, token: String): Project? {
        val fo = ProjectFetchOptions()
        val pi = ProjectIdentifier(code, code)
        val res = connection.getProjects(token, listOf(pi), fo)
        return res[pi]
    }



}
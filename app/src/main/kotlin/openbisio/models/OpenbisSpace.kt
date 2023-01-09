package openbisio.models

import ch.ethz.sis.openbis.generic.asapi.v3.IApplicationServerApi
import ch.ethz.sis.openbis.generic.asapi.v3.dto.space.Space
import ch.ethz.sis.openbis.generic.asapi.v3.dto.space.fetchoptions.SpaceFetchOptions
import ch.ethz.sis.openbis.generic.asapi.v3.dto.space.create.*
import ch.ethz.sis.openbis.generic.asapi.v3.dto.space.id.SpacePermId

import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import kotlinx.serialization.SerialName


@Serializable
data class OpenbisSpace(
    override val code: String,
    override val ancestorsCodes: MutableList<String>? = null,
    val description: String?,
    @SerialName("projects") override val children: List<OpenbisProject>?,
    override val registrator: OpenbisPerson
) : OpenbisIdentifiedObject() {
    constructor(sp: Space) : this(
        sp.code,
        mutableListOf(),
        sp.description,
        sp.projects.map { OpenbisProject(it) },
        OpenbisPerson(sp.registrator)
    )

    override fun createOperation(connection: IApplicationServerApi, token: String) {
        val sc = SpaceCreation().apply {
            this.code = code
            this.description = description ?: ""
        }
        connection.createSpaces(token, listOf(sc))
    }


    override fun getFromOpenBIS(connection: IApplicationServerApi, token: String): Space? {
        val sc = SpaceFetchOptions()
        val sid = SpacePermId(code)
        val result = connection.getSpaces(token, listOf(sid), sc)
        return result[sid]
    }


}
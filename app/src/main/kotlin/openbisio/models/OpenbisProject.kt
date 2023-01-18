/*
 * Copyright (c) 2023. Simone Baffelli
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package openbisio.models

import ch.ethz.sis.openbis.generic.asapi.v3.IApplicationServerApi
import ch.ethz.sis.openbis.generic.asapi.v3.dto.project.Project
import ch.ethz.sis.openbis.generic.asapi.v3.dto.project.create.*
import ch.ethz.sis.openbis.generic.asapi.v3.dto.project.fetchoptions.*
import ch.ethz.sis.openbis.generic.asapi.v3.dto.project.id.ProjectIdentifier
import ch.ethz.sis.openbis.generic.asapi.v3.dto.space.id.SpacePermId
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient


@Serializable
class OpenbisProject(
    override val code: String,
    @Transient override val ancestorsCodes: MutableList<String>? = null,
    val description: String?,
    var leader: OpenbisPerson?,
    @SerialName("collections") override val children: List<OpenbisCollection>?,
    @Transient override val registrator: OpenbisPerson? = null
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
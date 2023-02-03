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
import ch.ethz.sis.openbis.generic.asapi.v3.dto.space.Space
import ch.ethz.sis.openbis.generic.asapi.v3.dto.space.fetchoptions.SpaceFetchOptions
import ch.ethz.sis.openbis.generic.asapi.v3.dto.space.create.*
import ch.ethz.sis.openbis.generic.asapi.v3.dto.space.id.SpacePermId

import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import kotlinx.serialization.SerialName


@Serializable
data class Space(
    override val code: String,
    @Transient override val ancestorsCodes: MutableList<String>? = null,
    val description: String?,
    @SerialName("projects") override val children: List<Project>?,
    @Transient override val registrator: OpenbisPerson? = null
) : IdentifiedObject() {
    constructor(sp: Space) : this(
        sp.code,
        mutableListOf(),
        sp.description,
        sp.projects.map { Project(it) },
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
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

package ch.empa.openbisio.models

import ch.ethz.sis.openbis.generic.asapi.v3.dto.common.operation.IOperation
import ch.ethz.sis.openbis.generic.asapi.v3.dto.space.Space
import ch.ethz.sis.openbis.generic.asapi.v3.dto.space.create.*
import ch.ethz.sis.openbis.generic.asapi.v3.dto.space.fetchoptions.SpaceFetchOptions
import ch.ethz.sis.openbis.generic.asapi.v3.dto.space.search.SpaceSearchCriteria
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import ch.empa.openbisio.OpenBISService


@Serializable
data class Space(
    override val code: String,
    @Transient override var ancestorCodes: ArrayDeque<String> = ArrayDeque<String>(listOf()),
    val description: String? = null,
    @SerialName("projects") override val children: MutableList<Project>? = null,
    @Transient override val registrator: OpenbisPerson? = null
) : ICreatableHierarchyComponent, IRegistratorHolder {

    constructor(sp: Space) : this(
        sp.code,
        ArrayDeque<String>(listOf()),
        sp.description,
        sp.projects.map { Project(it) }.toMutableList(),
        OpenbisPerson(sp.registrator)
    )

    override val identifier: HierarchyIdentifier
        get() = ConcreteIdentifier.SpaceIdentifier(listOf(code))


    override fun createOperation(connection: OpenBISService): List<IOperation> {
        val sc = SpaceCreation().also {
            it.code = code
            it.description = description ?: ""
        }
        return listOf( CreateSpacesOperation(mutableListOf<SpaceCreation>(sc)))
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
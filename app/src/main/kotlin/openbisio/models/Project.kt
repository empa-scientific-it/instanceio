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

import ch.ethz.sis.openbis.generic.asapi.v3.dto.common.operation.IOperation
import ch.ethz.sis.openbis.generic.asapi.v3.dto.project.Project
import ch.ethz.sis.openbis.generic.asapi.v3.dto.project.create.*
import ch.ethz.sis.openbis.generic.asapi.v3.dto.project.fetchoptions.*
import ch.ethz.sis.openbis.generic.asapi.v3.dto.project.id.ProjectIdentifier
import ch.ethz.sis.openbis.generic.asapi.v3.dto.space.id.SpacePermId
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import openbisio.OpenBISService


@Serializable
data class Project(
    override val code: String,
    val description: String? = null,
    var leader: OpenbisPerson? = null,
    @Transient override val ancestorCodes: ArrayDeque<String> = ArrayDeque(
        listOf()),
    @SerialName("collections") override val children: MutableList<Collection>?,
    @Transient override val registrator: OpenbisPerson? = null,
) : ICreatableHierarchyComponent, IRegistratorHolder {
    constructor(
        pr: Project
    ) : this(
        pr.code,
        pr.description,
        if (pr.leader != null) OpenbisPerson(pr.leader) else null,
        ArrayDeque(listOf()),
        pr.experiments.map { Collection(it) }.toMutableList(),
        OpenbisPerson(pr.getRegistrator()),
    )

    override val identifier: HierarchyIdentifier
        get() = ConcreteIdentifier.ProjectIdentifier(ancestorCodes + code)


    override fun createOperation(connection: OpenBISService): List<IOperation> {
        println("creating ${code}")

        val pc = ProjectCreation().apply {
            this.spaceId = SpacePermId(ancestorCodes!![0])
            this.code = code
            this.description = description
        }

        val prc = CreateProjectsOperation(pc)
        return listOf(prc)
    }

    override fun getFromAS(connection: OpenBISService): Project? {
        val fo = ProjectFetchOptions()
        val pi = ProjectIdentifier(ancestorCodes!![0], code)
        println(pi)
        val res = connection.con.getProjects(connection.token, listOf(pi), fo)
        return res[pi]
    }



}
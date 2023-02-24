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

import ch.ethz.sis.openbis.generic.asapi.v3.dto.common.interfaces.IPermIdHolder
import ch.ethz.sis.openbis.generic.asapi.v3.dto.common.operation.IOperation
import ch.ethz.sis.openbis.generic.asapi.v3.dto.experiment.Experiment
import ch.ethz.sis.openbis.generic.asapi.v3.dto.experiment.create.*
import ch.ethz.sis.openbis.generic.asapi.v3.dto.experiment.fetchoptions.ExperimentFetchOptions
import ch.ethz.sis.openbis.generic.asapi.v3.dto.experiment.search.ExperimentSearchCriteria
import ch.ethz.sis.openbis.generic.asapi.v3.dto.project.id.ProjectIdentifier
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName
import kotlinx.serialization.Transient
import openbisio.OpenBISService
import openbisio.models.Object as OpenBISObject

@Serializable
data class Collection(
    override val code: String,
    @Transient override val ancestorCodes: ArrayDeque<String> = ArrayDeque(listOf()),
    val type: String,
    @SerialName("samples") override val children: MutableList<OpenBISObject>? = null,
    @Transient override val registrator: OpenbisPerson? = null,
    override val properties: Map<String, String>? = null,
) : ICreatableHierarchyComponent, IPropertyHolder, IRegistratorHolder {
    constructor(
        c: Experiment,
        includeSamples: Boolean = false,
    ) : this(
        c.code,
        ArrayDeque(listOf()),
        c.type.code,
        if (includeSamples) c.samples.map { OpenBISObject(it) }.toMutableList() else null,
        OpenbisPerson(c.getRegistrator()),
        c.properties)


    override val identifier: ConcreteIdentifier.CollectionIdentifier
        get() = ConcreteIdentifier.CollectionIdentifier(ancestorCodes + code)


    override fun createOperation(connection: OpenBISService): List<IOperation> {
        val ec = ExperimentCreation().apply {
            this.code = code
            this.projectId = ProjectIdentifier(ancestorCodes!![0], ancestorCodes!![1])
        }
        return listOf(CreateExperimentsOperation(ec))
    }

    override fun getFromAS(connection: OpenBISService): IPermIdHolder? {
        println(identifier)
        val so = ExperimentSearchCriteria().apply { withIdentifier().thatEquals(identifier.identifier) }
        val res = connection.con.searchExperiments(connection.token, so, ExperimentFetchOptions())
        return if (res.totalCount > 0) {
            res.objects[0]
        } else {
            null
        }
    }


}
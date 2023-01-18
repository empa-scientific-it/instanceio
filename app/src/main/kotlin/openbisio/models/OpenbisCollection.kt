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
import ch.ethz.sis.openbis.generic.asapi.v3.dto.common.interfaces.IPermIdHolder
import ch.ethz.sis.openbis.generic.asapi.v3.dto.experiment.Experiment
import ch.ethz.sis.openbis.generic.asapi.v3.dto.experiment.ExperimentType
import ch.ethz.sis.openbis.generic.asapi.v3.dto.experiment.create.*
import ch.ethz.sis.openbis.generic.asapi.v3.dto.experiment.fetchoptions.ExperimentFetchOptions
import ch.ethz.sis.openbis.generic.asapi.v3.dto.experiment.id.IExperimentId
import ch.ethz.sis.openbis.generic.asapi.v3.dto.experiment.search.ExperimentSearchCriteria
import ch.ethz.sis.openbis.generic.asapi.v3.dto.project.id.ProjectIdentifier
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName
import kotlinx.serialization.Transient

@Serializable
class OpenbisCollection(
    override val code: String,
    @Transient override val ancestorsCodes: MutableList<String>? = null,
    val type: String,
    @SerialName("samples") override var children: List<OpenbisObject>?,
    @Transient override val registrator: OpenbisPerson? = null
) : OpenbisIdentifiedObject() {
    constructor(
        c: Experiment,
        includeSamples: Boolean = false,
    ) : this(c.code, mutableListOf(), c.type.code, if(includeSamples)c.samples.map { OpenbisObject(it) } else listOf(), OpenbisPerson(c.getRegistrator()))


    override fun createOperation(connection: IApplicationServerApi, token: String) {
        val ec = ExperimentCreation().apply {
            this.code = code
            this.projectId = ProjectIdentifier(ancestorsCodes!![0], ancestorsCodes!![1])
        }
    }

    override fun getFromOpenBIS(connection: IApplicationServerApi, token: String): IPermIdHolder? {
        println(identifier)
        val so = ExperimentSearchCriteria().apply { withIdentifier().thatEquals(identifier) }
        val res = connection.searchExperiments(token, so, ExperimentFetchOptions())
        return res.objects[0]
    }


}
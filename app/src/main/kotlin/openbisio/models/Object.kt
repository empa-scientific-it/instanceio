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
import ch.ethz.sis.openbis.generic.asapi.v3.dto.experiment.fetchoptions.ExperimentFetchOptions
import ch.ethz.sis.openbis.generic.asapi.v3.dto.experiment.search.ExperimentSearchCriteria
import ch.ethz.sis.openbis.generic.asapi.v3.dto.sample.Sample
import ch.ethz.sis.openbis.generic.asapi.v3.dto.sample.SampleType
import ch.ethz.sis.openbis.generic.asapi.v3.dto.sample.create.SampleCreation
import ch.ethz.sis.openbis.generic.asapi.v3.dto.sample.fetchoptions.SampleFetchOptions
import ch.ethz.sis.openbis.generic.asapi.v3.dto.sample.fetchoptions.SampleTypeFetchOptions
import ch.ethz.sis.openbis.generic.asapi.v3.dto.sample.search.SampleSearchCriteria
import ch.ethz.sis.openbis.generic.asapi.v3.dto.sample.search.SampleTypeSearchCriteria
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient


@Serializable
class Object(
    override val code: String,
    private val type: String,
    @Transient override val ancestorsCodes: MutableList<String>? = null,
    override val children: List<Object>? = null,
    val properties: Map<String, String>,
    @Transient override val registrator: OpenbisPerson? = null,
    ) : IdentifiedObject() {

    constructor(
        o: Sample
    ) : this(o.code, o.type.code, mutableListOf(), null, o.properties, OpenbisPerson(o.getRegistrator()))

    fun getType(connection: IApplicationServerApi, token: String): SampleType {
        val typeResult = connection.searchSampleTypes(token, SampleTypeSearchCriteria().apply { this.withCode().thatEquals(type) },
            SampleTypeFetchOptions()
        )
        return typeResult.objects[0]
    }

    override fun getFromOpenBIS(connection: IApplicationServerApi, token: String): IPermIdHolder? {
        val sc  = SampleSearchCriteria().apply { withCode().thatEquals(code) }.withAndOperator().apply { withProject().withCode().thatEquals(ancestorsCodes!![2]) }
        val res = connection.searchSamples(token, sc, SampleFetchOptions())
        return res.objects[0]
    }

    override fun createOperation(connection: IApplicationServerApi, token: String) {
        val typeResult = connection.searchSampleTypes(token, SampleTypeSearchCriteria().apply { this.withCode().thatEquals(type) },
            SampleTypeFetchOptions()
        )
        val experimentResult = connection.searchExperiments(token, ExperimentSearchCriteria().apply { this.withIdentifier().thatEquals(code) },
            ExperimentFetchOptions()
        )
        val sc = SampleCreation().apply {
            this.code = code
            this.experimentId = experimentResult!!.objects[0].permId
            this.typeId = typeResult!!.objects[0].permId
            this.properties = properties
        }
        connection.createSamples(token, listOf(sc))
    }


}
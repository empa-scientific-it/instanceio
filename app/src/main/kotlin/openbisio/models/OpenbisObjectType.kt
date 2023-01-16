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
import ch.ethz.sis.openbis.generic.asapi.v3.dto.property.fetchoptions.PropertyTypeFetchOptions
import ch.ethz.sis.openbis.generic.asapi.v3.dto.sample.SampleType
import ch.ethz.sis.openbis.generic.asapi.v3.dto.sample.create.SampleTypeCreation
import ch.ethz.sis.openbis.generic.asapi.v3.dto.sample.fetchoptions.SampleTypeFetchOptions
import ch.ethz.sis.openbis.generic.asapi.v3.dto.sample.search.SampleTypeSearchCriteria
import kotlinx.serialization.Serializable

@Serializable
class OpenbisObjectType(
    override val code: String,
    var properties: List<OpenbisPropertyAssignment>,
    override val registrator: OpenbisPerson?
) : OpenbisCreatable() {
    constructor(
        ot: SampleType
    ) : this(
        ot.code,
        ot.propertyAssignments.map { OpenbisPropertyAssignment(it) },
        ot.propertyAssignments.map { OpenbisPerson(it.getRegistrator()) }.elementAtOrNull(0)
    )

    override fun getFromOpenBIS(connection: IApplicationServerApi, token: String): IPermIdHolder? {
        val sc = SampleTypeSearchCriteria().apply { withCode().thatEquals(code) }
        val res = connection.searchSampleTypes(token, sc, SampleTypeFetchOptions())
        return res.objects[0]
    }

    override fun createOperation(connection: IApplicationServerApi, token: String) {
        val stc=  SampleTypeCreation().apply {
            code = code
            properties = properties
        }
        connection.createSampleTypes(token, mutableListOf(stc))
    }
}
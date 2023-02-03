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
import ch.ethz.sis.openbis.generic.asapi.v3.dto.property.PropertyType
import ch.ethz.sis.openbis.generic.asapi.v3.dto.sample.SampleType
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable

data class Instance(
    var spaces: List<Space>?,
    var users: List<OpenbisPerson>?,
    @SerialName("property_types")  var openbisPropertyTypes: List<openbisio.models.PropertyType>?,
    @SerialName("object_types") var objectTypes: List<ObjectType>?
) {
    constructor(
        sp: List<ch.ethz.sis.openbis.generic.asapi.v3.dto.space.Space>,
        pt: List<PropertyType>,
        st: List<SampleType>
    ) : this(sp.map { Space(it) }, null, pt.map { PropertyType(it) }, st.map { ObjectType(it) })

    fun updateCodes(){
        spaces?.map{it.addAncestors()}
    }

    fun create(service: IApplicationServerApi, token: String){

        openbisPropertyTypes?.map { it.create(service, token) }
        objectTypes?.map{it.create(service, token)}
        spaces?.map {
            it.createHierarchy(service, token)
        }

    }
}
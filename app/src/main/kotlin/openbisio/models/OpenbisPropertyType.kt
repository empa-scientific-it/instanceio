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
import ch.ethz.sis.openbis.generic.asapi.v3.dto.property.PropertyType
import ch.ethz.sis.openbis.generic.asapi.v3.dto.property.create.PropertyTypeCreation
import ch.ethz.sis.openbis.generic.asapi.v3.dto.property.fetchoptions.PropertyTypeFetchOptions
import ch.ethz.sis.openbis.generic.asapi.v3.dto.property.id.PropertyTypePermId
import ch.ethz.sis.openbis.generic.asapi.v3.dto.property.search.PropertyTypeSearchCriteria
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import openbisio.DataType

@Serializable
class OpenbisPropertyType(
    override val code: String,
    val label: String,
    val description: String,
    val dataType: DataType,
    @Transient override val registrator: OpenbisPerson? = null
) : OpenbisCreatable() {
    constructor(
        pt: PropertyType
    ) : this(pt.code, pt.label, pt.description, DataType(pt.dataType), OpenbisPerson(pt.getRegistrator()))

    override fun getFromOpenBIS(connection: IApplicationServerApi, token: String): IPermIdHolder? {
        val sc = PropertyTypeSearchCriteria().apply {  withCode().thatEquals(code)}
        val res=  connection.searchPropertyTypes(token, sc, PropertyTypeFetchOptions())
        return res.objects[0] ?: null
    }

    override fun createOperation(connection: IApplicationServerApi, token: String) {
        val cr = PropertyTypeCreation().apply {
            code = code
            label = label
            description = description
            dataType = dataType
        }
        connection.createPropertyTypes(token, listOf(cr))
    }
}
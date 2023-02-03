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
import ch.ethz.sis.openbis.generic.asapi.v3.dto.property.PropertyAssignment
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import openbisio.DataType


@Serializable
class PropertyAssignment(
    override val code: String,
    val section: String?,
    val mandatory: Boolean,
    val type: DataType,
    @Transient override val registrator: OpenbisPerson? = null
) : ICreatable {
    constructor(
        pa: PropertyAssignment
    ) : this(
        pa.getPermId().getPropertyTypeId().toString(),
        pa.getSection(),
        pa.isMandatory(),
        type = DataType(pa.propertyType.dataType),
        OpenbisPerson(pa.getRegistrator())
    )

    override fun getFromOpenBIS(connection: IApplicationServerApi, token: String): IPermIdHolder? {
        TODO("Not yet implemented")
    }

    override fun createOperation(connection: IApplicationServerApi, token: String) {
        TODO("Not yet implemented")
    }
}
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

interface ICreatable : IEntity {
    fun createOperation(connection: IApplicationServerApi, token: String)
    fun exists(connection: IApplicationServerApi, token: String): Boolean {
        return getFromOpenBIS(connection, token) != null
    }

    fun create(connection: IApplicationServerApi, token: String) {
        if (!exists(connection, token)) createOperation(connection, token)
    }
}
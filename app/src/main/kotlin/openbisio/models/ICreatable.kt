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
import openbisio.OpenBISService

/**
 * Interface representing all openBIS entities that can be created on the openBIS
 * AS.
 */
interface ICreatable : IEntity {
    /**
     * This method should be implemented in order
     * for the entity to be created.
     * The method returns a {@link ch.ethz.sis.openbis.generic.asapi.v3.dto.common.create.IObjectCreation} object from the openBIS V3 API. In this way we implement
     * transactional creations.
     */
    fun createOperation(connection: OpenBISService): List<IOperation>
    fun exists(connection: OpenBISService): Boolean {
        return getFromAS(connection) != null
    }

    /**
     * This methods wraps the {@link #createOperation} method and only returns
     * a creation object when the object does not already exist on the openBIS AS
     */
    fun create(connection: OpenBISService): List<IOperation> {
        return if (!exists(connection)) createOperation(connection) else listOf()
    }
}
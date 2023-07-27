/*
 * Copyright 2023 Simone Baffelli
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package ch.empa.openbisio.interfaces

import ch.ethz.sis.openbis.generic.OpenBIS
import ch.ethz.sis.openbis.generic.asapi.v3.dto.common.create.CreateObjectsOperation
import ch.ethz.sis.openbis.generic.asapi.v3.dto.common.operation.IOperation

/**
 * Interface representing openBIS entities that can be created.
 * The interface is quite "monadic" as they do not perform the operation
 * themselves but return a ICreation object that can be used to create the entity from the V3 api
 */
interface CreatableEntity : Entity, CheckableEntity, IdentifiedEntity {

    private fun <E : IOperation> runIfNeeded(service: OpenBIS, operation: () -> List<E>): List<E> {
        return if (!exists(service)) {
            operation()
        } else {
            // Entity already exists, return an empty list of operations
            emptyList()
        }
    }


    /**
     * Returns a Ioperation object that can be used
     * to create the openBIS entity
     */
    fun persist(): List<CreateObjectsOperation<*>>


    fun remove(): List<IOperation>

    fun create(service: OpenBIS): List<CreateObjectsOperation<*>> {
        return runIfNeeded(service, this::persist)
    }

    fun delete(service: OpenBIS): List<IOperation> {
        return runIfNeeded(service, this::remove)
    }


}
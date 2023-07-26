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

package ch.empa.openbisio.instance

import ch.ethz.sis.openbis.generic.OpenBIS
import ch.ethz.sis.openbis.generic.asapi.v3.dto.operation.SynchronousOperationExecutionOptions

/**
 * This class is responsible for serializing an [InstanceEntity] into a list of [IOperation]s
 * and persisting them into openBIS.
 * @param openBIS an instance of [OpenBIS] that is used to communicate with the openBIS server
 */
class InstanceSerializer(val openBIS: OpenBIS) {
    /** Serializes and persists an [InstanceEntity] into openBIS.
     * @param entity the [InstanceEntity] to be serialized and persisted
     * @param oneByOne if true, the operations are executed one by one and the current operation is printed to the shell,
     * otherwise they are executed all at once. This is useful for debugging purposes. In production, it is recommended
     * to execute all operations at once for transactional behavior.
     */
    fun persist(entity: InstanceEntity, oneByOne: Boolean = false) {
        val operations = entity.create(openBIS)
        val options = SynchronousOperationExecutionOptions().apply {
            isExecuteInOrder = true
        }
        val res = if (oneByOne) {
            operations.map {
                println("Executing operation $it")
                openBIS.executeOperations(openBIS.sessionToken, listOf(it), options)
            }
        } else {
            if (operations.isNotEmpty()) openBIS.executeOperations(openBIS.sessionToken, operations, options) else null
        }
    }
}


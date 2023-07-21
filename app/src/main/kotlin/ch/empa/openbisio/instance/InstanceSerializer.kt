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
import ch.ethz.sis.openbis.generic.asapi.v3.dto.operation.IOperationExecutionResults
import ch.ethz.sis.openbis.generic.asapi.v3.dto.operation.SynchronousOperationExecutionOptions

class InstanceSerializer(val openBIS: OpenBIS) {
    fun persist(entity: InstanceEntity): IOperationExecutionResults {
        val operations = entity.create(openBIS)
        val options = SynchronousOperationExecutionOptions().apply {
            isExecuteInOrder = true
        }


        val res = openBIS.executeOperations(openBIS.sessionToken, operations, options)

        return res
    }
}

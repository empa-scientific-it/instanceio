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

package ch.empa.openbisio.interfaces

import ch.empa.openbisio.identifier.ConcreteIdentifier
import ch.ethz.sis.openbis.generic.asapi.v3.dto.common.interfaces.IPermIdHolder
import ch.empa.openbisio.openbis.OpenBISService
import ch.ethz.sis.openbis.generic.OpenBIS
import ch.ethz.sis.openbis.generic.asapi.v3.dto.common.create.ICreation
import ch.ethz.sis.openbis.generic.asapi.v3.dto.common.operation.IOperation
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id

/**
 * Interface representing openBIS entities
 * that can be taken from openBIS and have
 * a code as identifier
 *
 */
interface Entity {
    val dto: DTO
    val identifier: List<String>

    /**
     * Get the corresponding entity from an openBIS application server
     *
     */
    fun get(connection: OpenBIS): IPermIdHolder?

    /**
     * Returns an operation object
     */
    fun persist(): ICreation

    /**
     * Return an identifier object
     *
     */
    fun openBISIdentifier(): Identifier
}
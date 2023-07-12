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

package ch.empa.openbisio.collectiontype

import ch.empa.openbisio.interfaces.CreatableEntity
import ch.empa.openbisio.interfaces.DTO
import ch.empa.openbisio.interfaces.Identifier
import ch.ethz.sis.openbis.generic.asapi.v3.dto.common.create.ICreation
import ch.ethz.sis.openbis.generic.asapi.v3.dto.experiment.create.ExperimentTypeCreation

class CollectionTypeEntity(override val dto: CollectionTypeDTO) : CreatableEntity {
    override val identifier: Identifier
        get() = TODO("Not yet implemented")

    override fun persist(): ExperimentTypeCreation {
        val experimentTypeCreation = ExperimentTypeCreation().apply {
            this.code = dto.code
            this.description = dto.description
            this.propertyAssignments = dto.propertyAssignments.map { it.toEntity().persist() }
        }
        return experimentTypeCreation
    }
}
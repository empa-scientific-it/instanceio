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
import ch.empa.openbisio.interfaces.Identifier
import ch.ethz.sis.openbis.generic.OpenBIS
import ch.ethz.sis.openbis.generic.asapi.v3.dto.common.operation.IOperation
import ch.ethz.sis.openbis.generic.asapi.v3.dto.entitytype.id.EntityTypePermId
import ch.ethz.sis.openbis.generic.asapi.v3.dto.experiment.create.CreateExperimentTypesOperation
import ch.ethz.sis.openbis.generic.asapi.v3.dto.experiment.create.ExperimentTypeCreation
import ch.ethz.sis.openbis.generic.asapi.v3.dto.experiment.delete.DeleteExperimentTypesOperation
import ch.ethz.sis.openbis.generic.asapi.v3.dto.experiment.delete.ExperimentTypeDeletionOptions
import ch.ethz.sis.openbis.generic.asapi.v3.dto.experiment.fetchoptions.ExperimentFetchOptions
import ch.ethz.sis.openbis.generic.asapi.v3.dto.experiment.search.ExperimentSearchCriteria

class CollectionTypeEntity(override val dto: CollectionTypeDTO) : CreatableEntity {
    override val identifier: Identifier
        get() = TODO("Not yet implemented")

    override fun persist(): List<IOperation> {
        val experimentTypeCreation = ExperimentTypeCreation().apply {
            this.code = dto.code
            this.description = dto.description
            this.propertyAssignments = dto.propertyAssignments.map { it.toEntity().persist() }
        }
        return listOf(CreateExperimentTypesOperation(listOf(experimentTypeCreation)))
    }

    override fun exists(service: OpenBIS): Boolean {
        val sc = ExperimentSearchCriteria().apply {
            this.withType().withCode().thatEquals(dto.code)
        }
        val res = service.searchExperiments(sc, ExperimentFetchOptions())
        return res.totalCount > 0
    }

    override fun delete(): List<IOperation> {
        return listOf(
            DeleteExperimentTypesOperation(
                listOf(EntityTypePermId(identifier.identifier)),
                ExperimentTypeDeletionOptions()
            )
        )
    }
}
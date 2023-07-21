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

package ch.empa.openbisio.datasettype

import ch.empa.openbisio.interfaces.CreatableEntity
import ch.ethz.sis.openbis.generic.OpenBIS
import ch.ethz.sis.openbis.generic.asapi.v3.dto.common.operation.IOperation
import ch.ethz.sis.openbis.generic.asapi.v3.dto.dataset.create.CreateDataSetTypesOperation
import ch.ethz.sis.openbis.generic.asapi.v3.dto.dataset.create.DataSetTypeCreation
import ch.ethz.sis.openbis.generic.asapi.v3.dto.dataset.delete.DataSetTypeDeletionOptions
import ch.ethz.sis.openbis.generic.asapi.v3.dto.dataset.delete.DeleteDataSetTypesOperation
import ch.ethz.sis.openbis.generic.asapi.v3.dto.dataset.fetchoptions.DataSetTypeFetchOptions
import ch.ethz.sis.openbis.generic.asapi.v3.dto.dataset.search.DataSetTypeSearchCriteria
import ch.ethz.sis.openbis.generic.asapi.v3.dto.entitytype.id.EntityTypePermId

class DataSetTypeEntity(override val dto: DataSetTypeDTO) : CreatableEntity {
    override val identifier: DataSetTypeIdentifier = DataSetTypeIdentifier(dto.code)

    override fun persist(): List<IOperation> {
        val dataSetTypeCreation = DataSetTypeCreation().apply {
            this.code = identifier.identifier
            this.description = dto.description
            this.propertyAssignments = dto.propertyAssignments.map { it.toEntity().persist() }
        }
        return listOf(CreateDataSetTypesOperation(listOf(dataSetTypeCreation)))
    }

    override fun exists(service: OpenBIS): Boolean {
        val sc = DataSetTypeSearchCriteria().apply {
            this.withCode().thatEquals(identifier.identifier)
        }
        val res = service.searchDataSetTypes(sc, DataSetTypeFetchOptions())
        return res.totalCount > 0
    }

    override fun delete(): List<IOperation> {
        return listOf(
            DeleteDataSetTypesOperation(
                listOf(
                    EntityTypePermId(identifier.identifier)
                ),
                DataSetTypeDeletionOptions()
            )
        )
    }
}
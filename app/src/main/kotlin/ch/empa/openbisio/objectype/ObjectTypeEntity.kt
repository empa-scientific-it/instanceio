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

package ch.empa.openbisio.objectype

import ch.empa.openbisio.interfaces.CreatableEntity
import ch.ethz.sis.openbis.generic.OpenBIS
import ch.ethz.sis.openbis.generic.asapi.v3.dto.common.operation.IOperation
import ch.ethz.sis.openbis.generic.asapi.v3.dto.sample.create.CreateSampleTypesOperation
import ch.ethz.sis.openbis.generic.asapi.v3.dto.sample.create.SampleTypeCreation
import ch.ethz.sis.openbis.generic.asapi.v3.dto.sample.fetchoptions.SampleTypeFetchOptions
import ch.ethz.sis.openbis.generic.asapi.v3.dto.sample.search.SampleTypeSearchCriteria

class ObjectTypeEntity(override val dto: ObjectTypeDTO) : CreatableEntity {
    override val identifier: ObjectTypeIdentifier = ObjectTypeIdentifier(dto.code)
    override fun persist(): List<IOperation> {
        val objectTypeCreation = SampleTypeCreation().apply {
            this.code = dto.code
            this.description = dto.description
            this.propertyAssignments = dto.propertyAssignments.map { it.toEntity().persist() }
        }
        return listOf(CreateSampleTypesOperation(listOf(objectTypeCreation)))
    }

    override fun exists(service: OpenBIS): Boolean {
        val sc = SampleTypeSearchCriteria().apply {
            this.withCode().thatEquals(identifier.identifier)
        }
        val res = service.searchSampleTypes(sc, SampleTypeFetchOptions())
        return res.totalCount > 0
    }
}
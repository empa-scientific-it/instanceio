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

package ch.empa.openbisio.propertytype

import ch.empa.openbisio.datatype.DataTypeDTO
import ch.empa.openbisio.interfaces.CreatableEntity
import ch.empa.openbisio.interfaces.Identifier
import ch.empa.openbisio.vocabulary.VocabularyIdentifier
import ch.ethz.sis.openbis.generic.OpenBIS
import ch.ethz.sis.openbis.generic.asapi.v3.dto.common.operation.IOperation
import ch.ethz.sis.openbis.generic.asapi.v3.dto.property.create.CreatePropertyTypesOperation
import ch.ethz.sis.openbis.generic.asapi.v3.dto.property.create.PropertyTypeCreation
import ch.ethz.sis.openbis.generic.asapi.v3.dto.property.delete.DeletePropertyTypesOperation
import ch.ethz.sis.openbis.generic.asapi.v3.dto.property.delete.PropertyTypeDeletionOptions
import ch.ethz.sis.openbis.generic.asapi.v3.dto.property.fetchoptions.PropertyTypeFetchOptions
import ch.ethz.sis.openbis.generic.asapi.v3.dto.property.id.PropertyTypePermId
import ch.ethz.sis.openbis.generic.asapi.v3.dto.property.search.PropertyTypeSearchCriteria
import ch.ethz.sis.openbis.generic.asapi.v3.dto.vocabulary.id.IVocabularyId
import ch.ethz.sis.openbis.generic.asapi.v3.dto.vocabulary.id.VocabularyPermId

class PropertyTypeEntity(override val dto: PropertyTypeDTO) : CreatableEntity {
    override val identifier: Identifier = PropertyTypeIdentifier(dto.code)

    override fun persist(): List<IOperation> {
        val propertyTypeCreation = PropertyTypeCreation().apply {
            this.code = dto.code
            this.label = dto.label
            this.description = dto.description
            this.dataType = dto.dataType.toOpenBISDataType()
            this.vocabularyId = if(dto.vocabularyId != null && dto.dataType == DataTypeDTO.CONTROLLEDVOCABULARY) VocabularyPermId(dto.vocabularyId) else null
        }
        return listOf(CreatePropertyTypesOperation(listOf(propertyTypeCreation)))
    }

    override fun exists(service: OpenBIS): Boolean {
        val sc = PropertyTypeSearchCriteria().apply {
            this.withCode().thatEquals(identifier.identifier)

        }
        println(sc)
        val res = service.searchPropertyTypes(sc, PropertyTypeFetchOptions())
        return res.totalCount > 0
    }

    override fun delete(service: OpenBIS): List<IOperation> {
        return listOf(
            DeletePropertyTypesOperation(
                listOf(PropertyTypePermId(identifier.identifier)),
                PropertyTypeDeletionOptions()
            )
        )
    }
}
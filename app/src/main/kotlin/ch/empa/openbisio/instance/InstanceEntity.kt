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

import ch.empa.openbisio.collectiontype.CollectionTypeEntity
import ch.empa.openbisio.datasettype.DataSetTypeEntity
import ch.empa.openbisio.identifier.ConcreteIdentifier
import ch.empa.openbisio.interfaces.CreatableEntity
import ch.empa.openbisio.objectype.ObjectTypeEntity
import ch.empa.openbisio.propertytype.PropertyTypeEntity
import ch.empa.openbisio.space.SpaceEntity
import ch.empa.openbisio.vocabulary.VocabularyEntity
import ch.ethz.sis.openbis.generic.asapi.v3.dto.common.create.ICreation

class InstanceEntity(override val dto: InstanceDTO) : CreatableEntity {
    override val identifier = ConcreteIdentifier.InstanceIdentifier()
    override fun persist(): List<ICreation> {
        val propertyTypeCreations = propertyTypes.flatMap { it.persist() }
        val objectTypeCreations = objectTypes.flatMap { it.persist() }
        val vocabularyCreations = vocabularies.flatMap { it.persist() }
        val dataSetTypeCreations = dataSetTypes.flatMap { it.persist() }
        val collectionTypeCreations = collectionTypes.flatMap { it.persist() }
        val spaceCreations = spaces.flatMap { it.persist() }
        return propertyTypeCreations.plus(objectTypeCreations).plus(vocabularyCreations).plus(dataSetTypeCreations)
            .plus(collectionTypeCreations).plus(spaceCreations)
    }

    val spaces: List<SpaceEntity> = dto.spaces?.map { it.toEntity() } ?: listOf()
    val propertyTypes: List<PropertyTypeEntity> = dto.propertyTypes?.map { it.toEntity() } ?: listOf()
    val objectTypes: List<ObjectTypeEntity> = dto.objectTypes?.map { it.toEntity() } ?: listOf()
    val vocabularies: List<VocabularyEntity> = dto.vocabularies?.map { it.toEntity() } ?: listOf()
    val collectionTypes: List<CollectionTypeEntity> = dto.collectionTypes?.map { it.toEntity() } ?: listOf()
    val dataSetTypes: List<DataSetTypeEntity> = dto.dataSetTypes?.map { it.toEntity() } ?: listOf()


}
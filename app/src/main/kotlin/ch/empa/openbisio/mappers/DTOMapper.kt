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

package ch.empa.openbisio.mappers

import ch.empa.openbisio.collection.CollectionDTO
import ch.empa.openbisio.collection.CollectionEntity
import ch.empa.openbisio.collectiontype.CollectionTypeDTO
import ch.empa.openbisio.collectiontype.CollectionTypeEntity
import ch.empa.openbisio.collectiontype.CollectionTypeIdentifier
import ch.empa.openbisio.datasettype.DataSetTypeDTO
import ch.empa.openbisio.datasettype.DataSetTypeEntity
import ch.empa.openbisio.datasettype.DataSetTypeIdentifier
import ch.empa.openbisio.identifier.ConcreteIdentifier
import ch.empa.openbisio.instance.InstanceDTO
import ch.empa.openbisio.instance.InstanceEntity
import ch.empa.openbisio.`object`.ObjectDTO
import ch.empa.openbisio.`object`.ObjectEntity
import ch.empa.openbisio.objectype.ObjectTypeDTO
import ch.empa.openbisio.objectype.ObjectTypeEntity
import ch.empa.openbisio.objectype.ObjectTypeIdentifier
import ch.empa.openbisio.project.ProjectDTO
import ch.empa.openbisio.project.ProjectEntity
import ch.empa.openbisio.propertyassignment.PropertyAssignmentDTO
import ch.empa.openbisio.propertyassignment.PropertyAssignmentEntity
import ch.empa.openbisio.propertyassignment.PropertyAssignmentIdentifier
import ch.empa.openbisio.propertytype.PropertyTypeDTO
import ch.empa.openbisio.propertytype.PropertyTypeEntity
import ch.empa.openbisio.propertytype.PropertyTypeIdentifier
import ch.empa.openbisio.space.SpaceDTO
import ch.empa.openbisio.space.SpaceEntity
import ch.empa.openbisio.vocabulary.*
import org.mapstruct.Mapper
import org.mapstruct.Mapping

@Mapper
interface DTOMapper {

    @Mapping(target = "identifier", source = "code")
    fun toProjectEntity(dto: ProjectDTO): ProjectEntity

    @Mapping(target = "identifier", source = "code")
    fun toObjectEntity(ob: ObjectDTO): ObjectEntity

    fun objectDTOListToObjectEntityList(ob: List<ObjectDTO>): List<ObjectEntity>

    fun toObjectIdentifierList(ob: List<String>): List<ConcreteIdentifier.SampleIdentifier>

    @Mapping(target = "identifier", source = "code")
    fun toCollectionEntity(dto: CollectionDTO): CollectionEntity

    @Mapping(target = "identifier", source = "code", defaultValue = "/")
    fun toSpaceEntity(dto: SpaceDTO): SpaceEntity


    fun toInstanceEntity(dto: InstanceDTO): InstanceEntity

    @Mapping(target = "identifier", source = "code")
    fun toPropertyTypeEntity(dto: PropertyTypeDTO): PropertyTypeEntity

    @Mapping(target = "identifier", source = "code")
    fun toObjectTypeEntity(dto: ObjectTypeDTO): ObjectTypeEntity

    @Mapping(target = "identifier", source = "code")
    fun toVocabularyEntity(dto: VocabularyDTO): VocabularyEntity

    @Mapping(target = "identifier", source = "code")
    fun toVocabularyTermEntity(dto: VocabularyTermDTO): VocabularyTermEntity

    @Mapping(target = "identifier", source = "code")
    fun toCollectionTypeEntity(dto: CollectionTypeDTO): CollectionTypeEntity

    @Mapping(target = "identifier", source = "code")
    fun toDataSetTypeEntity(dto: DataSetTypeDTO): DataSetTypeEntity

    fun toPropertyAssignmentEntity(dto: PropertyAssignmentDTO): PropertyAssignmentEntity


    fun toPropertyAssignmentIdentifier(code: String): PropertyAssignmentIdentifier

    @Mapping(target = "identifier", source = "code")
    fun toSampleIdentifier(code: String): ConcreteIdentifier.SampleIdentifier

    @Mapping(target = "identifier", source = "code")
    fun toProjectIdentifier(code: String): ConcreteIdentifier.ProjectIdentifier

    @Mapping(target = "identifier", source = "code", defaultValue = "/")
    fun toSpaceIdentifier(code: String): ConcreteIdentifier.SpaceIdentifier

    @Mapping(target = "identifier", source = "code")
    fun toCollectionIdentifier(code: String): ConcreteIdentifier.CollectionIdentifier

    @Mapping(target = "identifier", source = "code")
    fun toVocabularyIdentifier(code: String): VocabularyIdentifier

    @Mapping(target = "identifier", source = "code")
    fun toPropertyTypeIdentifier(code: String): PropertyTypeIdentifier

    @Mapping(target = "identifier", source = "code")
    fun toObjectTypeIdentifier(code: String): ObjectTypeIdentifier

    @Mapping(target = "identifier", source = "code")
    fun toVocabularyTermIdentifier(code: String): VocabularyTermIdentifier

    @Mapping(target = "identifier", source = "code")
    fun toCollectionTypeIdentifier(code: String): CollectionTypeIdentifier

    @Mapping(target = "identifier", source = "code")
    fun toDataSetTypeIdentifier(code: String): DataSetTypeIdentifier

    fun projectDTOListToProjectEntityList(dto: List<ProjectDTO>): List<ProjectEntity>

    fun spaceDTOListToSpaceEntityList(dto: List<SpaceDTO>): List<SpaceEntity>
}
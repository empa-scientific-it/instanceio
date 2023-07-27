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
import ch.empa.openbisio.hierarchy.HierarchicalEntity
import ch.empa.openbisio.identifier.ConcreteIdentifier
import ch.empa.openbisio.interfaces.CreatableEntity
import ch.empa.openbisio.interfaces.Tree
import ch.empa.openbisio.objectype.ObjectTypeEntity
import ch.empa.openbisio.propertytype.PropertyTypeEntity
import ch.empa.openbisio.space.SpaceEntity
import ch.empa.openbisio.vocabulary.VocabularyEntity
import ch.ethz.sis.openbis.generic.OpenBIS
import ch.ethz.sis.openbis.generic.asapi.v3.dto.common.create.CreateObjectsOperation
import ch.ethz.sis.openbis.generic.asapi.v3.dto.common.operation.IOperation
import ch.ethz.sis.openbis.generic.asapi.v3.dto.space.create.CreateSpacesOperation

/**
 * This class represents an instance of openBIS.
 * It is used to create, delete and update the instance.
 * @param dto the data transfer object used to initialize the instance
 */
class InstanceEntity(
    val spaces: MutableList<SpaceEntity> = mutableListOf(),
    val propertyTypes: MutableList<PropertyTypeEntity>?,
    val objectTypes: MutableList<ObjectTypeEntity>?,
    val vocabularies: MutableList<VocabularyEntity>?,
    val collectionTypes: MutableList<CollectionTypeEntity>?,
    val dataSetTypes: MutableList<DataSetTypeEntity>?
) : HierarchicalEntity {

    /**
     * The identifier of the instance, in this case it is fixed to `/` because the
     * instance is the root of the tree.
     */
    override val identifier = ConcreteIdentifier.InstanceIdentifier()

    val flatEntities: List<CreatableEntity> = listOf(vocabularies, propertyTypes, collectionTypes, objectTypes, dataSetTypes).filterNotNull().flatten()

    override fun value(): InstanceEntity {
        return this
    }

    override fun hasChildren(): Boolean {
        return spaces?.isNotEmpty() ?: false
    }

    override fun children(): List<HierarchicalEntity> {
        return spaces ?: listOf()
    }


    override fun exists(service: OpenBIS): Boolean {
        return false
    }

    /**
     * Performs an operation on all the entities of the instance. This is assuming that all
     * the operations return a list of `IOperation` because they potentially act on multiple
     * entities in the hierarchy. The function combined them monadically using `flatMap` and returns a single
     * list of operations. To take care of the dependencies, the operations are performed in the following order:
     * 1. Property types
     * 2. Object types
     * 3. Vocabularies
     * 4. Data set types
     * 5. Collection types
     * 6. Spaces are performed last because they depend on the other entities and the operations are performed breadth-first because every entity can depend on the previous ones:
     *
     * @param op the operation to perform
     * @param service the openBIS service to use
     * @return a list of operations that can be used to perform the operation
     */

    private fun <E : IOperation> performHierarchicalOperations(
        entities: List<Tree<HierarchicalEntity>>,
        service: OpenBIS,
        operation: (CreatableEntity) -> List<E>
    ): List<E> {
        return entities.flatMap { entity ->
            when (entity) {
                is CreatableEntity -> {
                    val childrenOps = performHierarchicalOperations(entity.children().toList(), service, operation)
                    if (!entity.exists(service)) {
                        val entityOps = operation(entity)
                        entityOps + childrenOps
                    } else {
                        childrenOps
                    }
                }

                else -> {
                    emptyList()
                }
            }
        }
    }

    private fun <E : IOperation> performOperations(
        service: OpenBIS,
        operation: (CreatableEntity) -> List<E>
    ): List<E> {
        return performFlatOperations(flatEntities, service, operation) + performHierarchicalOperations(
            spaces,
            service,
            operation
        )

    }

    private fun <E : IOperation> performFlatOperations(
        entities: List<CreatableEntity>,
        service: OpenBIS,
        operation: (CreatableEntity) -> List<E>
    ): List<E> {
        return entities.flatMap { entity -> println(entity); if (!entity.exists(service)) operation(entity) else emptyList() }
    }


    override fun create(service: OpenBIS): List<CreateObjectsOperation<*>> {

        return performOperations(service) { it.create(service) }
    }

    override fun delete(service: OpenBIS): List<IOperation> {

        return performOperations(service) { it.delete(service) }
    }

    override fun persist(): List<CreateSpacesOperation> {
        return listOf()
    }

    override fun remove(): List<IOperation> {
        return listOf()
    }

}
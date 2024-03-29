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

package ch.empa.openbisio.collection

import ch.empa.openbisio.hierarchy.HierarchicalDTO
import ch.empa.openbisio.identifier.ConcreteIdentifier
import ch.empa.openbisio.interfaces.CodeHolder
import ch.empa.openbisio.interfaces.PropertyHolder
import ch.empa.openbisio.interfaces.Tree
import ch.empa.openbisio.`object`.ObjectDTO
import kotlinx.serialization.Serializable

/**
 * DTO representing a collection. It is a data class, therefore it is immutable, any change returns a copy.
 * It is a tree, so it can have children that represents the objects contained in the collection.
 */
@Serializable
data class CollectionDTO(
    override val code: String,
    val type: String,
    val objects: List<ObjectDTO> = listOf(),
    override val properties: Map<String, String> = mapOf<String, String>()
) :
    HierarchicalDTO, Tree<HierarchicalDTO>, CodeHolder, PropertyHolder {


    override fun value(): HierarchicalDTO {
        return this
    }

    /**
     * Add a child to the collection and return a new collection.
     * @param child the child to add
     * @return the collection with the child added
     */
    fun withObject(child: ObjectDTO): CollectionDTO {
        return this.copy(objects = objects.plus(child))
    }

    override fun hasChildren(): Boolean {
        return objects.isNotEmpty()
    }

    override fun children(): Collection<Tree<HierarchicalDTO>> {
        return objects
    }

    /**
     * Update the code of the collection and return a new collection.
     * This is used to update the code of the collection when it is added to the tree, so that the code
     * represents the path to the collection.
     * @param code the new code
     * @return the collection with the new code
     */
    override fun updateCode(code: String): HierarchicalDTO {
        return this.copy(code = code)
    }

    fun toEntity(): CollectionEntity {
        return CollectionEntity(
            ConcreteIdentifier.CollectionIdentifier(code),
            properties,
            objects.map { it.toEntity() },
            type
        )
    }

}

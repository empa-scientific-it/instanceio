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

import ch.empa.openbisio.collection.CollectionDTO
import ch.empa.openbisio.collectiontype.CollectionTypeDTO
import ch.empa.openbisio.datasettype.DataSetTypeDTO
import ch.empa.openbisio.hierarchy.HierarchicalDTO
import ch.empa.openbisio.interfaces.*
import ch.empa.openbisio.`object`.ObjectDTO
import ch.empa.openbisio.objectype.ObjectTypeDTO
import ch.empa.openbisio.project.ProjectDTO
import ch.empa.openbisio.propertytype.PropertyTypeDTO
import ch.empa.openbisio.space.SpaceDTO
import ch.empa.openbisio.vocabulary.VocabularyDTO
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class InstanceDTO(
    val spaces: List<SpaceDTO>? = listOf(),
    @SerialName("property_types") val propertyTypes: List<PropertyTypeDTO>? = null,
    @SerialName("object_types") val objectTypes: List<ObjectTypeDTO>? = null,
    @SerialName("collection_types") val collectionTypes: List<CollectionTypeDTO>? = null,
    val vocabularies: List<VocabularyDTO>? = null,
    val dataSetTypes: List<DataSetTypeDTO>? = null
) : HierarchicalDTO, Tree<HierarchicalDTO> {

    fun getSpace(code: String): SpaceDTO? {
        return spaces?.find { it.code == code }
    }

    fun withSpace(space: SpaceDTO): InstanceDTO {
        return this.copy(spaces = spaces?.plus(space))
    }

    override fun updateCode(code: String): HierarchicalDTO {
        return this.copy()
    }

    override fun toEntity(): CreatableEntity {
        return InstanceEntity(this)
    }

    override val code: String
        get() = "/"


    fun filter(pred: (DTO) -> Boolean): InstanceDTO {
        return InstanceDTO(
            spaces = spaces?.filter(pred),
            propertyTypes = propertyTypes?.filter(pred),
            objectTypes = objectTypes?.filter(pred),
            collectionTypes = collectionTypes?.filter(pred),
            vocabularies = vocabularies?.filter(pred)
        )
    }

    override fun value(): HierarchicalDTO {
        return this
    }

    override fun hasChildren(): Boolean {
        return spaces?.isNotEmpty() ?: false
    }

    override fun children(): Collection<Tree<HierarchicalDTO>> {
        return spaces ?: listOf()
    }



    fun updateCodes(): InstanceDTO{

        fun  builder(entity: HierarchicalDTO, children: List<HierarchicalDTO>): HierarchicalDTO {

            val res = when(entity){
                is SpaceDTO ->  entity.copy(projects=children.map { it as ProjectDTO })
                is ProjectDTO ->  entity.copy(collections = children.map { it as CollectionDTO })
                is CollectionDTO ->  entity.copy(objects=children.map { it as ObjectDTO })
                is InstanceDTO -> entity.copy(spaces=children.map { it as SpaceDTO })
                else -> entity
            }
            return res
        }

        val newCode = iterateWithParent(
            this.copy(),
            { entity: HierarchicalDTO, parent: HierarchicalDTO -> entity.updateCode(parent.code + "/" + entity.code)},
            { entity: HierarchicalDTO, children: List<HierarchicalDTO> ->  builder(entity, children)},
            this.copy(spaces = listOf())
        )
        return newCode as InstanceDTO
    }

    fun toEntityWithCodes(): InstanceEntity {
        return InstanceEntity(this.updateCodes())
    }


}
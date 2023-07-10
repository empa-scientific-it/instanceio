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

import ch.empa.openbisio.collectiontype.CollectionTypeDTO
import ch.empa.openbisio.datasettype.DataSetTypeDTO
import ch.empa.openbisio.interfaces.CodeHolder
import ch.empa.openbisio.interfaces.DTO
import ch.empa.openbisio.interfaces.Entity
import ch.empa.openbisio.objectype.ObjectTypeDTO
import ch.empa.openbisio.propertytype.PropertyTypeDTO
import ch.empa.openbisio.space.SpaceDTO
import ch.empa.openbisio.vocabulary.VocabularyDTO
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class InstanceDTO(
    val spaces: List<SpaceDTO>?,
    @SerialName("property_types") val propertyTypes: List<PropertyTypeDTO>? = null,
    @SerialName("object_types") val objectTypes: List<ObjectTypeDTO>? = null,
    @SerialName("collection_types") val collectionTypes: List<CollectionTypeDTO>? = null,
    val vocabularies: List<VocabularyDTO>? = null,
    val dataSetTypes: List<DataSetTypeDTO>? = null
) : DTO, CodeHolder {

    fun getSpace(code: String): SpaceDTO? {
        return spaces?.find { it.code == code }
    }

    override fun toEntity(): Entity {
        TODO("Not yet implemented")
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


}
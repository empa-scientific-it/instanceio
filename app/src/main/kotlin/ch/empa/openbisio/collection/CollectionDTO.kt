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

import ch.empa.openbisio.interfaces.*
import ch.empa.openbisio.`object`.ObjectDTO
import kotlinx.serialization.Serializable

@Serializable
data class CollectionDTO(
    override val code: String,
    val objects: List<ObjectDTO> = listOf(),
    val type: String,
    override val properties: Map<String, String> = mapOf<String, String>()
) :
    DTO, Tree<DTO>, CodeHolder, PropertyHolder {


    override fun value(): DTO {
        return this
    }

    override fun hasChildren(): Boolean {
        return objects.isNotEmpty()
    }

    override fun children(): Collection<Tree<DTO>> {
        return objects
    }

    override fun toEntity(): Entity {
        return CollectionEntity(this, listOf())
    }

}

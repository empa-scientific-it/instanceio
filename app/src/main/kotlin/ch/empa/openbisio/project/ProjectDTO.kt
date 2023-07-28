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

package ch.empa.openbisio.project

import ch.empa.openbisio.collection.CollectionDTO
import ch.empa.openbisio.hierarchy.HierarchicalDTO
import ch.empa.openbisio.identifier.ConcreteIdentifier
import ch.empa.openbisio.interfaces.CodeHolder
import ch.empa.openbisio.interfaces.Tree
import kotlinx.serialization.Serializable

@Serializable
data class ProjectDTO(
    override val code: String,
    val collections: List<CollectionDTO> = listOf(),
    val description: String = "",
    //val leader: PersonDTO? = null
) : Tree<HierarchicalDTO>, HierarchicalDTO, CodeHolder {
    override fun value(): HierarchicalDTO {
        return this
    }

    override fun hasChildren(): Boolean {
        return collections.isNotEmpty()
    }

    override fun children(): Collection<Tree<HierarchicalDTO>> {
        return collections
    }

    override fun updateCode(code: String): HierarchicalDTO {
        return this.copy(code = code)
    }



    fun withCollection(collection: CollectionDTO): ProjectDTO {
        return this.copy(collections = this.collections + collection)
    }


}



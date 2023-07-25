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

package ch.empa.openbisio.space

import ch.empa.openbisio.hierarchy.HierarchicalEntity
import ch.empa.openbisio.identifier.ConcreteIdentifier
import ch.empa.openbisio.interfaces.CreatableEntity
import ch.empa.openbisio.interfaces.IdentifiedEntity
import ch.empa.openbisio.interfaces.Tree
import ch.empa.openbisio.project.ProjectEntity
import ch.ethz.sis.openbis.generic.OpenBIS
import ch.ethz.sis.openbis.generic.asapi.v3.dto.common.operation.IOperation
import ch.ethz.sis.openbis.generic.asapi.v3.dto.space.create.CreateSpacesOperation
import ch.ethz.sis.openbis.generic.asapi.v3.dto.space.create.SpaceCreation
import ch.ethz.sis.openbis.generic.asapi.v3.dto.space.delete.DeleteSpacesOperation
import ch.ethz.sis.openbis.generic.asapi.v3.dto.space.delete.SpaceDeletionOptions
import ch.ethz.sis.openbis.generic.asapi.v3.dto.space.fetchoptions.SpaceFetchOptions
import ch.ethz.sis.openbis.generic.asapi.v3.dto.space.id.SpacePermId
import ch.ethz.sis.openbis.generic.asapi.v3.dto.space.search.SpaceSearchCriteria

data class SpaceEntity(
    override val identifier: ConcreteIdentifier.SpaceIdentifier,
    val description: String,
    val projects: List<ProjectEntity> = listOf()
) : HierarchicalEntity{


    override fun persist(): List<IOperation> {
        val sc = SpaceCreation().apply {
            this.code = identifier.code
            this.description = description
        }
        return listOf(CreateSpacesOperation(listOf(sc)))
    }

    override fun exists(service: OpenBIS): Boolean {
        val sc = SpaceSearchCriteria().apply {
            this.withCode().thatEquals(identifier.code)
        }
        val res = service.searchSpaces(sc, SpaceFetchOptions())
        return res.totalCount > 0
    }



    override fun delete(service: OpenBIS): List<IOperation> {
        return listOf(DeleteSpacesOperation(listOf(SpacePermId(identifier.code)), SpaceDeletionOptions()))
    }

    override fun value(): HierarchicalEntity {
        return this
    }

    override fun hasChildren(): Boolean {
        return this.projects.isNotEmpty()
    }

    override fun children(): Collection<Tree<HierarchicalEntity>> {
        return this.projects
    }


}
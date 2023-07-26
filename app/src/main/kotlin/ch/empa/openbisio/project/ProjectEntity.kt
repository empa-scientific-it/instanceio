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

import ch.empa.openbisio.collection.CollectionEntity
import ch.empa.openbisio.hierarchy.HierarchicalEntity
import ch.empa.openbisio.identifier.ConcreteIdentifier
import ch.empa.openbisio.interfaces.Tree
import ch.ethz.sis.openbis.generic.OpenBIS
import ch.ethz.sis.openbis.generic.asapi.v3.dto.common.operation.IOperation
import ch.ethz.sis.openbis.generic.asapi.v3.dto.project.create.CreateProjectsOperation
import ch.ethz.sis.openbis.generic.asapi.v3.dto.project.create.ProjectCreation
import ch.ethz.sis.openbis.generic.asapi.v3.dto.project.delete.DeleteProjectsOperation
import ch.ethz.sis.openbis.generic.asapi.v3.dto.project.delete.ProjectDeletionOptions
import ch.ethz.sis.openbis.generic.asapi.v3.dto.project.fetchoptions.ProjectFetchOptions
import ch.ethz.sis.openbis.generic.asapi.v3.dto.project.id.ProjectIdentifier
import ch.ethz.sis.openbis.generic.asapi.v3.dto.project.search.ProjectSearchCriteria
import ch.ethz.sis.openbis.generic.asapi.v3.dto.space.id.SpacePermId

data class ProjectEntity(
    override val identifier: ConcreteIdentifier.ProjectIdentifier,
    val description: String,
    val collections: List<CollectionEntity>
) :
    HierarchicalEntity {


    override fun persist(): List<IOperation> {
        val pc = ProjectCreation().apply {
            this.code = identifier.code
            this.description = this@ProjectEntity.description
            this.spaceId = SpacePermId(identifier.space().code)
            this.leaderId = null
        }
        return listOf(CreateProjectsOperation(listOf(pc)))
    }

    override fun exists(service: OpenBIS): Boolean {
        val sc = ProjectSearchCriteria().apply {
            this.withCode().thatEquals(identifier.code)
            this.withSpace().withCode().thatEquals(identifier.space().code)
        }
        val res = service.searchProjects(sc, ProjectFetchOptions())
        if (res.totalCount > 0) {
            println("Project ${res.objects[0].identifier} already exists: ${identifier.identifier}")
        }
        return res.totalCount > 0
    }

    override fun create(service: OpenBIS): List<IOperation> {
        val sc = collections.flatMap { it.create(service) }
        if (exists(service)) {
            return sc
        } else {
            return persist().plus(sc)
        }
    }

    override fun delete(service: OpenBIS): List<IOperation> {
        return listOf(
            DeleteProjectsOperation(
                listOf(ProjectIdentifier(identifier.identifier)),
                ProjectDeletionOptions()
            )
        )
    }

    override fun value(): HierarchicalEntity {
        return this
    }

    override fun hasChildren(): Boolean {
        return collections.isNotEmpty()
    }

    override fun children(): Collection<Tree<HierarchicalEntity>> {
        return collections
    }


}
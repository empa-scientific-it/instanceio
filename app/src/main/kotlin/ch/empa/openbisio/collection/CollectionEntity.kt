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

import ch.empa.openbisio.hierarchy.HierarchicalEntity
import ch.empa.openbisio.identifier.ConcreteIdentifier
import ch.empa.openbisio.interfaces.Tree
import ch.empa.openbisio.`object`.ObjectEntity
import ch.ethz.sis.openbis.generic.OpenBIS
import ch.ethz.sis.openbis.generic.asapi.v3.dto.common.operation.IOperation
import ch.ethz.sis.openbis.generic.asapi.v3.dto.entitytype.EntityKind
import ch.ethz.sis.openbis.generic.asapi.v3.dto.entitytype.id.EntityTypePermId
import ch.ethz.sis.openbis.generic.asapi.v3.dto.experiment.create.CreateExperimentsOperation
import ch.ethz.sis.openbis.generic.asapi.v3.dto.experiment.create.ExperimentCreation
import ch.ethz.sis.openbis.generic.asapi.v3.dto.experiment.delete.DeleteExperimentsOperation
import ch.ethz.sis.openbis.generic.asapi.v3.dto.experiment.delete.ExperimentDeletionOptions
import ch.ethz.sis.openbis.generic.asapi.v3.dto.experiment.fetchoptions.ExperimentFetchOptions
import ch.ethz.sis.openbis.generic.asapi.v3.dto.experiment.id.ExperimentIdentifier
import ch.ethz.sis.openbis.generic.asapi.v3.dto.experiment.search.ExperimentSearchCriteria
import ch.ethz.sis.openbis.generic.asapi.v3.dto.project.id.ProjectIdentifier

data class CollectionEntity(
    override val identifier: ConcreteIdentifier.CollectionIdentifier,
    val properties: Map<String, String>,
    val objects: List<ObjectEntity>,
    val type: String
) : HierarchicalEntity {


    override fun persist(): List<IOperation> {
        val cr = ExperimentCreation().apply {
            this.code = identifier.code
            this.projectId = ProjectIdentifier(identifier.project().identifier)
            this.properties = this@CollectionEntity.properties.ifEmpty { null }
            this.typeId = EntityTypePermId(type, EntityKind.EXPERIMENT)
        }

        return listOf(CreateExperimentsOperation(listOf(cr)))
    }

    override fun exists(service: OpenBIS): Boolean {
        val sc = ExperimentSearchCriteria().apply {
            this.withCode().thatEquals(identifier.code)
            this.withProject().withCode().thatEquals(identifier.project().code)
        }
        val res = service.searchExperiments(sc, ExperimentFetchOptions())
        return res.totalCount > 0
    }


    override fun delete(service: OpenBIS): List<IOperation> {
        return listOf(
            DeleteExperimentsOperation(
                mutableListOf(ExperimentIdentifier(identifier.identifier)),
                ExperimentDeletionOptions()
            )
        )
    }

    override fun value(): HierarchicalEntity {
        return this
    }

    override fun hasChildren(): Boolean {
        return objects.isNotEmpty()
    }

    override fun children(): Collection<Tree<HierarchicalEntity>> {
        return objects
    }

}

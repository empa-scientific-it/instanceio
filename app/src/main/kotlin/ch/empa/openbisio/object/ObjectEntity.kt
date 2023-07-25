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

package ch.empa.openbisio.`object`

import ch.empa.openbisio.hierarchy.HierarchicalEntity
import ch.empa.openbisio.identifier.ConcreteIdentifier
import ch.empa.openbisio.interfaces.CreatableEntity
import ch.empa.openbisio.interfaces.IdentifiedEntity
import ch.empa.openbisio.interfaces.Tree
import ch.ethz.sis.openbis.generic.OpenBIS
import ch.ethz.sis.openbis.generic.asapi.v3.dto.common.operation.IOperation
import ch.ethz.sis.openbis.generic.asapi.v3.dto.entitytype.id.EntityTypePermId
import ch.ethz.sis.openbis.generic.asapi.v3.dto.experiment.id.ExperimentIdentifier
import ch.ethz.sis.openbis.generic.asapi.v3.dto.project.id.ProjectIdentifier
import ch.ethz.sis.openbis.generic.asapi.v3.dto.project.id.ProjectPermId
import ch.ethz.sis.openbis.generic.asapi.v3.dto.sample.create.CreateSamplesOperation
import ch.ethz.sis.openbis.generic.asapi.v3.dto.sample.create.SampleCreation
import ch.ethz.sis.openbis.generic.asapi.v3.dto.sample.delete.DeleteSamplesOperation
import ch.ethz.sis.openbis.generic.asapi.v3.dto.sample.delete.SampleDeletionOptions
import ch.ethz.sis.openbis.generic.asapi.v3.dto.sample.fetchoptions.SampleFetchOptions
import ch.ethz.sis.openbis.generic.asapi.v3.dto.sample.id.SampleIdentifier
import ch.ethz.sis.openbis.generic.asapi.v3.dto.sample.search.SampleSearchCriteria
import ch.ethz.sis.openbis.generic.asapi.v3.dto.space.id.SpacePermId

data class ObjectEntity(
    override val identifier: ConcreteIdentifier.SampleIdentifier,
    val type: String,
    val properties: Map<String, String>,
    //val children: List<ConcreteIdentifier.SampleIdentifier> = listOf(),
    //val parents: List<ConcreteIdentifier.SampleIdentifier> = listOf()
) : HierarchicalEntity {


    override fun persist(): List<IOperation> {
        val sc = SampleCreation().apply {
            code = identifier.code
            experimentId = ExperimentIdentifier(identifier.getAncestor().identifier)
            spaceId = SpacePermId(identifier.space().identifier)
            projectId = ProjectIdentifier(identifier.project().identifier)
            typeId = EntityTypePermId(type)
            properties = this@ObjectEntity.properties
        }
        return listOf(CreateSamplesOperation(listOf(sc)))
    }

    override fun exists(service: OpenBIS): Boolean {
        val sc = SampleSearchCriteria().apply {
            withCode().thatEquals(identifier.code)
            withExperiment().withCode().thatEquals(identifier.getAncestor().code)
            withProject().withCode().thatEquals(identifier.project().code)
            withSpace().withCode().thatEquals(identifier.space().code)
            withType().withCode().thatEquals(type)
        }
        println(sc)
        val res = service.searchSamples(sc, SampleFetchOptions())
        println(res)
        return res.totalCount > 0
    }

    override fun delete(service: OpenBIS): List<IOperation> {
        return listOf(DeleteSamplesOperation(listOf(SampleIdentifier(identifier.identifier)), SampleDeletionOptions()))
    }

    override fun value(): HierarchicalEntity {
        return this
    }

    override fun hasChildren(): Boolean {
        return false
    }

    override fun children(): Collection<Tree<HierarchicalEntity>> {
        return listOf()
    }


}
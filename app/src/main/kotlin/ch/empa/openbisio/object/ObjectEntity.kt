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

import ch.empa.openbisio.identifier.ConcreteIdentifier
import ch.empa.openbisio.interfaces.CreatableEntity
import ch.empa.openbisio.interfaces.Entity
import ch.ethz.sis.openbis.generic.asapi.v3.dto.common.create.ICreation
import ch.ethz.sis.openbis.generic.asapi.v3.dto.entitytype.id.EntityTypePermId
import ch.ethz.sis.openbis.generic.asapi.v3.dto.experiment.id.ExperimentIdentifier
import ch.ethz.sis.openbis.generic.asapi.v3.dto.project.id.ProjectPermId
import ch.ethz.sis.openbis.generic.asapi.v3.dto.sample.create.SampleCreation
import ch.ethz.sis.openbis.generic.asapi.v3.dto.space.id.SpacePermId

class ObjectEntity(override val dto: ObjectDTO, override val identifier: ConcreteIdentifier.SampleIdentifier) : CreatableEntity {


    override fun persist(): ICreation {
        val sc = SampleCreation().apply {
            code = dto.code
            experimentId = ExperimentIdentifier(identifier.getAncestor().getCode())
            spaceId = SpacePermId(identifier!!.space()!!.identifier)
            projectId = ProjectPermId(identifier!!.project()!!.identifier)
            typeId = EntityTypePermId(dto.type)
            properties = dto.properties.mapValues { it.toString() }
        }
        return sc
    }



}
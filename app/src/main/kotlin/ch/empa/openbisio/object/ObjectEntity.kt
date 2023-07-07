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
import ch.empa.openbisio.interfaces.DTO
import ch.empa.openbisio.interfaces.Entity
import ch.empa.openbisio.interfaces.Identifier
import ch.empa.openbisio.objectype.ObjectTypeAdapter
import ch.ethz.sis.openbis.generic.OpenBIS
import ch.ethz.sis.openbis.generic.asapi.v3.dto.common.create.ICreation
import ch.ethz.sis.openbis.generic.asapi.v3.dto.common.interfaces.IPermIdHolder
import ch.ethz.sis.openbis.generic.asapi.v3.dto.common.operation.IOperation
import ch.ethz.sis.openbis.generic.asapi.v3.dto.entitytype.id.EntityTypePermId
import ch.ethz.sis.openbis.generic.asapi.v3.dto.event.EntityType
import ch.ethz.sis.openbis.generic.asapi.v3.dto.experiment.id.ExperimentIdentifier
import ch.ethz.sis.openbis.generic.asapi.v3.dto.experiment.id.ExperimentPermId
import ch.ethz.sis.openbis.generic.asapi.v3.dto.sample.create.SampleCreation
import ch.ethz.sis.openbis.generic.asapi.v3.dto.space.id.SpacePermId

class ObjectEntity(override val dto: ObjectDTO, override val identifier: List<String>) : Entity{
    override fun get(connection: OpenBIS): IPermIdHolder? {
        TODO("Not yet implemented")
    }



    override fun persist(): ICreation {
        val sc = SampleCreation().apply {
            code = dto.code
            experimentId = ExperimentIdentifier(openBISIdentifier().getAncestor()!!.getCode())
            typeId = EntityTypePermId(dto.type)
            properties = dto.properties.mapValues { it.toString() }
            }
        return sc
    }

    override fun  openBISIdentifier(): ConcreteIdentifier.SampleIdentifier {
       return ConcreteIdentifier.SampleIdentifier(identifier)
    }

}
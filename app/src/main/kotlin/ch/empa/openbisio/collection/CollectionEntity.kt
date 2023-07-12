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

import ch.empa.openbisio.identifier.ConcreteIdentifier
import ch.empa.openbisio.interfaces.*
import ch.ethz.sis.openbis.generic.asapi.v3.dto.common.create.ICreation
import ch.ethz.sis.openbis.generic.asapi.v3.dto.experiment.create.ExperimentCreation
import ch.ethz.sis.openbis.generic.asapi.v3.dto.project.id.ProjectPermId

class CollectionEntity(override val dto: CollectionDTO) : CreatableEntity{

    override val identifier: ConcreteIdentifier.CollectionIdentifier =
        ConcreteIdentifier.CollectionIdentifier(listOf(dto.code))
    val properties = dto.properties
    //val children = dto.children()?.map { it.toEntity() }

    override fun persist(): List<ICreation> {
        val cr = ExperimentCreation().apply {
            this.code = dto.code
            this.projectId = ProjectPermId(identifier.project().identifier)
            this.properties = dto.properties
        }
        return listOf(cr)
    }

}

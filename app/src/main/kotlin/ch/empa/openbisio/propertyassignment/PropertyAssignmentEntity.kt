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

package ch.empa.openbisio.propertyassignment

import ch.empa.openbisio.interfaces.CreatableEntity
import ch.empa.openbisio.interfaces.Identifier
import ch.ethz.sis.openbis.generic.asapi.v3.dto.common.create.ICreation
import ch.ethz.sis.openbis.generic.asapi.v3.dto.property.create.PropertyAssignmentCreation
import ch.ethz.sis.openbis.generic.asapi.v3.dto.property.id.PropertyTypePermId

class PropertyAssignmentEntity(override val dto: PropertyAssignmentDTO) : CreatableEntity {


    override val identifier: Identifier
        get() = TODO("Not yet implemented")

    override fun persist(): List<PropertyAssignmentCreation> {
        val assignmentCreation = PropertyAssignmentCreation().apply {
            this.propertyTypeId = PropertyTypePermId(dto.propertyTypeCode)
            this.isMandatory = dto.mandatory
            this.section = dto.section
        }
        return listOf(assignmentCreation)
    }
}



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

import ch.empa.openbisio.interfaces.AssignmentEntity
import ch.ethz.sis.openbis.generic.asapi.v3.dto.property.create.PropertyAssignmentCreation
import ch.ethz.sis.openbis.generic.asapi.v3.dto.property.id.PropertyTypePermId

class PropertyAssignmentEntity(
    val propertyTypeCode: String,
    val mandatory: Boolean,
    val section: String?
) : AssignmentEntity {


    override fun persist(): PropertyAssignmentCreation {
        val assignmentCreation = PropertyAssignmentCreation().apply {
            this.propertyTypeId = PropertyTypePermId(propertyTypeCode)
            this.isMandatory = mandatory
            this.section = this@PropertyAssignmentEntity.section
        }
        return assignmentCreation
    }


}




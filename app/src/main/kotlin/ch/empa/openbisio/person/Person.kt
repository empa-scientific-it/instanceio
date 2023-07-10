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

package ch.empa.openbisio.person

import ch.empa.openbisio.interfaces.Entity
import ch.empa.openbisio.interfaces.Identifier
import ch.ethz.sis.openbis.generic.asapi.v3.dto.common.create.ICreation
import ch.ethz.sis.openbis.generic.asapi.v3.dto.person.create.PersonCreation
import ch.ethz.sis.openbis.generic.asapi.v3.dto.space.id.SpacePermId
import kotlinx.serialization.Serializable

@Serializable
class PersonEntity(override val dto: PersonDTO, override val identifier: List<String>) : Entity {

    override fun persist(): ICreation {
        val pc = PersonCreation().apply {
            this.userId = dto.code
            this.spaceId = SpacePermId(dto.space)
        }
        return pc
    }

    override fun openBISIdentifier(): Identifier {
        TODO("Not yet implemented")
    }

}
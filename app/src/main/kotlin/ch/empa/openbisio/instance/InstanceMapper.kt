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

package ch.empa.openbisio.instance

import ch.empa.openbisio.mappers.DTOMapper
import org.mapstruct.factory.Mappers

class InstanceMapper(val inst: InstanceDTO) {
    val mapper = Mappers.getMapper(DTOMapper::class.java)

    fun mapToEntity(): InstanceEntity {
        return mapper.toInstanceEntity(inst.updateCodes())
    }
}
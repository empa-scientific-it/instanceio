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

package ch.empa.openbisio

import ch.empa.openbisio.datatype.DataTypeDTO
import ch.empa.openbisio.objectype.ObjectTypeDTO
import ch.empa.openbisio.propertytype.PropertyTypeDTO
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlin.test.Test

class ObjectTypeTest {

    @Test
    fun testSerialize() {
        val DT = PropertyTypeDTO("TEST", "A", "D", DataTypeDTO.BOOLEAN)
        val OT = ObjectTypeDTO("TEST", "A", "A", listOf())
        val encoded = Json.encodeToString(OT)
        val decoded = Json.decodeFromString<ObjectTypeDTO>(encoded)
        assert(OT == decoded)
    }


}
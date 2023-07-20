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

import ch.empa.openbisio.identifier.ConcreteIdentifier
import ch.empa.openbisio.instance.InstanceDTO
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import kotlin.test.Test
import kotlin.test.assertEquals

class EntityIdentifierTest {
    val json = Json { prettyPrint = true }
    val config = javaClass.getResource("/simple_instance.json").readText()
    val instanceDTO = json.decodeFromString<InstanceDTO>(config)
    val instance = instanceDTO.toEntityWithCodes()

    @Test
    fun testInstanceIdentifier() {
        assertEquals(ConcreteIdentifier.InstanceIdentifier(), instance.identifier)
    }

    @Test
    fun testSpaceIdentifier() {
        assertEquals(ConcreteIdentifier.SpaceIdentifier("/YOUR_SPACE_CODE"), instance.spaces?.first()?.identifier)
    }

    @Test
    fun testSpaceCode() {
        assertEquals("YOUR_SPACE_CODE", instance.spaces?.first()?.identifier?.code)
    }

    @Test
    fun testProjectIdentifier() {
        assertEquals(
            ConcreteIdentifier.ProjectIdentifier("/YOUR_SPACE_CODE/YOUR_FIRST_PROJECT_CODE"),
            instance.spaces?.first()?.projects?.first()?.identifier
        )
    }

}
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

import ch.empa.openbisio.collection.CollectionDTO
import ch.empa.openbisio.instance.InstanceDTO
import ch.empa.openbisio.project.ProjectDTO
import ch.empa.openbisio.space.SpaceDTO
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import kotlin.test.Test
import kotlin.test.assertEquals

/**
 * Test the serialisation and deserialisation of the InstanceDTO class. The test checks
 * whether:
 * 1. the deserialised instance is equal to the local instance
 * 2. the serialised instance is equal to the deserialised instance
 */
class DeserialiserTest {

    val localInst = InstanceDTO().withSpace(
        SpaceDTO("YOUR_SPACE_CODE").withProject(
            ProjectDTO("YOUR_FIRST_PROJECT_CODE").withCollection(
                CollectionDTO("YOUR_FIRST_COLLECTION_CODE", type = "COLLECTION")
            )
        )
    )

    val json = Json { prettyPrint = true }
    val config = javaClass.getResource("/simple_instance.json").readText()

    @Test
    fun testRead() {
        val readInst = json.decodeFromString<InstanceDTO>(config)
        assertEquals(readInst, localInst)
    }

    @Test
    fun testWrite() {
        val readInst = json.decodeFromString<InstanceDTO>(config)
        val writeInst = json.encodeToString(InstanceDTO.serializer(), readInst)
        val rereadInst = json.decodeFromString<InstanceDTO>(writeInst)
        assertEquals(readInst, rereadInst)
        assertEquals(rereadInst, localInst)
    }
}
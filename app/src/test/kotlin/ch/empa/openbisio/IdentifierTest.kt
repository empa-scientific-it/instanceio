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
import ch.empa.openbisio.`object`.ObjectDTO
import ch.empa.openbisio.project.ProjectDTO
import ch.empa.openbisio.space.SpaceDTO
import kotlin.test.Test
import kotlin.test.assertEquals

/**
 * Tests if the identifiers are correctly updated.
 * The purpose is to push the identifier down the hierarchy to obtain the path to each entity. The path is then used by
 * the entities to create the corresponding openBIS entities.
 */
class IdentifierTest {
    val instance = InstanceDTO().withSpace(
        SpaceDTO("space").withProject(
            ProjectDTO("project").withCollection(
                CollectionDTO("collection", "COLLECTION", listOf()).withObject(ObjectDTO("object", "UNKOWN"))
            )
        )
    )
    val updated = instance.updateCodes()

    @Test
    fun testSpaceIdentifier() {
        assertEquals("/space", updated.spaces?.get(0)?.code)
    }

    @Test
    fun testProjectIdentifier() {
        assertEquals("/space/project", updated.spaces?.get(0)?.projects?.get(0)?.code)
    }

    @Test
    fun testCollectionIdentifier() {
        assertEquals("/space/project/collection", updated.spaces?.get(0)?.projects?.get(0)?.collections?.get(0)?.code)
    }

    @Test
    fun testObjectIdentifier() {
        assertEquals(
            "/space/project/collection/object",
            updated.spaces?.get(0)?.projects?.get(0)?.collections?.get(0)?.objects?.get(0)?.code
        )
    }

}
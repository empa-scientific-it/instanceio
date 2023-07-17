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
import org.junit.jupiter.api.assertAll
import kotlin.test.Test
import kotlin.test.*

class ConcreteIdentifierTest {
    @Test
    fun testSpaceIdentifier() {
        val spaceIdentifier = ConcreteIdentifier.SpaceIdentifier("/SPACE")
        assertEquals("SPACE", spaceIdentifier.code)
    }

    @Test
    fun testProjectIdentifier() {
        val spaceIdentifier = ConcreteIdentifier.ProjectIdentifier("/SPACE/PROJECT")
        assertEquals("/SPACE/PROJECT", spaceIdentifier.identifier)
        assertEquals("PROJECT", spaceIdentifier.code)
        assertEquals("SPACE", spaceIdentifier.space().code)
    }

    @Test
    fun testCollectionIdentifier() {
        val spaceIdentifier = ConcreteIdentifier.CollectionIdentifier("/SPACE/PROJECT/COLLECTION")
        assertEquals("/SPACE/PROJECT/COLLECTION", spaceIdentifier.identifier)
        assertEquals("COLLECTION", spaceIdentifier.code)
        assertEquals("PROJECT", spaceIdentifier.project().code)
        assertEquals("SPACE", spaceIdentifier.project().space().code)
    }
}
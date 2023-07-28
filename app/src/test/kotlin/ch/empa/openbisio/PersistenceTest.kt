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

import ch.empa.openbisio.instance.InstanceDeserializer
import ch.empa.openbisio.instance.InstanceSerializer
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Tags
import kotlin.test.Test
import kotlin.test.assertContains
import kotlin.test.assertEquals
@Tags
@Tag("IntegrationTest")
class PersistenceTest : OpenBisContainerTest("simple_instance.json") {


    @Test
    fun assertSame() {
        val res = InstanceSerializer(openBIS).persist(mapper.mapToEntity())
        readInstance = InstanceDeserializer().dumpInstance(openBIS)
        logger.info("Starting test")
        assertContains(readInstance.spaces!!.map { it.code }, "YOUR_SPACE_CODE")
        assertEquals(
            readInstance.getSpace("YOUR_SPACE_CODE")!!.projects,
            instanceDTO.getSpace("YOUR_SPACE_CODE")!!.projects
        )
    }

}
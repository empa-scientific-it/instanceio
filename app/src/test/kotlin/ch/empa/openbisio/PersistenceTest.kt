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

import ch.empa.openbisio.instance.InstanceDTO
import ch.empa.openbisio.instance.InstanceDeserializer
import ch.empa.openbisio.instance.InstanceSerializer
import ch.ethz.sis.openbis.generic.OpenBIS
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import kotlin.test.Test
import kotlin.test.assertContains
import kotlin.test.assertEquals

class PersistenceTest {
    //    val openBISContainer = DockerComposeContainer(File(composeFile))
//        .withExposedService("openbis", 443)
//        .withLocalCompose(true)
//        .withStartupTimeout(Duration.ofSeconds(20))
//
    val json = Json { prettyPrint = true }
    val config = javaClass.getResource("/simple_instance.json").readText()
    val instanceDTO = json.decodeFromString<InstanceDTO>(config)
    val openBIS = OpenBIS("https://localhost:8445")
    val log = openBIS.login("admin", "changeit")
    val res = InstanceSerializer(openBIS).persist(instanceDTO.toEntityWithCodes())
    val readInstance = InstanceDeserializer().dumpInstance(openBIS)

    @Test
    fun assertSame() {
        assertContains(readInstance.spaces!!.map { it.code }, "YOUR_SPACE_CODE")
        assertEquals(
            readInstance.getSpace("YOUR_SPACE_CODE")!!.projects,
            instanceDTO.getSpace("YOUR_SPACE_CODE")!!.projects
        )
        //assertContains(readInstance.spaces!!.asIterable(), readInstance.getSpace("YOUR_SPACE_CODE"))
    }


    /*
        @Test
        fun testSpaceCreation() {
            assertContains(readInstance.spaces!!.asIterable(), readInstance.getSpace("YOUR_SPACE_CODE"))
        }

        @Test
        fun testProjectCreation() {
            println(readInstance.getSpace("YOUR_SPACE_CODE"))
            assertContains(
                readInstance.spaces!!.flatMap { it.projects },
                readInstance.getSpace("YOUR_SPACE_CODE")?.getProject("YOUR_FIRST_PROJECT_CODE")
            )
        }
    */

}
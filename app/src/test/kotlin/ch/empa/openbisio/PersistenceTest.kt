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
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.testcontainers.containers.DockerComposeContainer
import org.testcontainers.containers.output.Slf4jLogConsumer
import org.testcontainers.containers.wait.strategy.Wait
import java.io.File
import java.time.Duration
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertContains
import kotlin.test.assertEquals

class PersistenceTest {
    val logger: Logger = LoggerFactory.getLogger(PersistenceTest::class.java)
    val configFile = javaClass.getResource("/simple_instance.json")
    val composeFile = File(javaClass.getResource("/docker-compose.yml").toURI())
    val logConsumer = Slf4jLogConsumer(logger);
    val openBISContainer = DockerComposeContainer(composeFile)
        .withExposedService("openbis", 443)
        .withLocalCompose(true)
        .withStartupTimeout(Duration.ofSeconds(90))
        .withLogConsumer("openbis", logConsumer)
        .waitingFor("openbis", Wait.forHealthcheck())
        .waitingFor("openbis", Wait.forHttps("/openbis/webapp/openbis-ng-ui/").allowInsecure())
    val json = Json { prettyPrint = true }
    val config = configFile.readText()
    val instanceDTO = json.decodeFromString<InstanceDTO>(config)
    lateinit var readInstance: InstanceDTO
    lateinit var url: String
    lateinit var openBIS: OpenBIS


    @BeforeTest
    fun setup() {
        openBISContainer.start()
        url = "https://" + openBISContainer.getServiceHost("openbis", 443) + ":" + openBISContainer.getServicePort(
            "openbis",
            443
        )
        openBIS = OpenBIS(url)
        val log = openBIS.login("admin", "changeit")
        val res = InstanceSerializer(openBIS).persist(instanceDTO.toEntityWithCodes())
        readInstance = InstanceDeserializer().dumpInstance(openBIS)
    }

    @Test
    fun assertSame() {
        println(openBIS.serverPublicInformation)
        logger.info("Starting test")
        assertContains(readInstance.spaces!!.map { it.code }, "YOUR_SPACE_CODE")
        assertEquals(
            readInstance.getSpace("YOUR_SPACE_CODE")!!.projects,
            instanceDTO.getSpace("YOUR_SPACE_CODE")!!.projects
        )
    }

}
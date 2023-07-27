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
import ch.empa.openbisio.instance.InstanceMapper
import ch.ethz.sis.openbis.generic.OpenBIS
import kotlinx.serialization.json.Json
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.testcontainers.containers.GenericContainer
import org.testcontainers.containers.output.Slf4jLogConsumer
import org.testcontainers.containers.wait.strategy.Wait
import org.testcontainers.utility.DockerImageName
import java.net.URL
import java.time.Duration
import kotlin.test.BeforeTest

open class OpenBisContainerTest(configName: String) {
    val logger: Logger = LoggerFactory.getLogger(PersistenceTest::class.java)
    val configFile: URL = javaClass.getResource("/${configName}")
    val logConsumer = Slf4jLogConsumer(logger)
    val openBISContainer = GenericContainer(DockerImageName.parse("openbis/debian-openbis:latest"))
        .withExposedPorts(443)
        .withStartupTimeout(Duration.ofSeconds(90))
        .withLogConsumer(logConsumer)
        .waitingFor(Wait.forHealthcheck())
        .waitingFor(Wait.forHttps("/openbis/webapp/openbis-ng-ui/").allowInsecure())
    val json = Json { prettyPrint = true }
    val config = configFile.readText()
    val instanceDTO = json.decodeFromString<InstanceDTO>(config)
    val mapper = InstanceMapper(instanceDTO)
    lateinit var readInstance: InstanceDTO
    lateinit var url: String
    lateinit var openBIS: OpenBIS


    @BeforeTest
    fun setup() {
        logger.info("$configFile")
//        openBISContainer.start()
//        url = "https://" + openBISContainer.getHost() + ":" + openBISContainer.getMappedPort(
//            443
//        )
        openBIS = OpenBIS("https://localhost:8443")
        val log = openBIS.login("admin", "changeit")
    }
}
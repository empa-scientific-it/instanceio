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
import ch.ethz.sis.openbis.generic.asapi.v3.dto.common.search.SearchResult
import ch.ethz.sis.openbis.generic.asapi.v3.dto.experiment.Experiment
import ch.ethz.sis.openbis.generic.asapi.v3.dto.experiment.create.CreateExperimentsOperation
import ch.ethz.sis.openbis.generic.asapi.v3.dto.experiment.create.ExperimentCreation
import ch.ethz.sis.openbis.generic.asapi.v3.dto.project.Project
import ch.ethz.sis.openbis.generic.asapi.v3.dto.project.create.CreateProjectsOperation
import ch.ethz.sis.openbis.generic.asapi.v3.dto.project.create.ProjectCreation
import ch.ethz.sis.openbis.generic.asapi.v3.dto.space.Space
import ch.ethz.sis.openbis.generic.asapi.v3.dto.space.create.CreateSpacesOperation
import ch.ethz.sis.openbis.generic.asapi.v3.dto.space.create.SpaceCreation
import io.mockk.*
import io.mockk.core.ValueClassSupport.boxedValue
import kotlinx.serialization.json.Json
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

open class MockOpenBISTest(configName: String) {

    val json = Json { prettyPrint = true }
    val config = javaClass.getResource("/$configName").readText()

    val instanceDTO = json.decodeFromString<InstanceDTO>(config)
    val instanceEntity = InstanceMapper(instanceDTO).mapToEntity()
    lateinit var openBIS: OpenBIS

    @BeforeTest
    fun setup() {
        openBIS = mockk<OpenBIS>()
        every { openBIS.searchSpaces(any(), any()) } returns SearchResult(listOf(), 0)
        every { openBIS.searchExperiments(any(), any()) } returns SearchResult(listOf(), 0)
        every { openBIS.searchProjects(any(), any()) } returns SearchResult(listOf(), 0)
    }


}
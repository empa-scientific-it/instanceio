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

import ch.ethz.sis.openbis.generic.asapi.v3.dto.common.create.CreateObjectsOperation
import ch.ethz.sis.openbis.generic.asapi.v3.dto.common.create.ICreation
import ch.ethz.sis.openbis.generic.asapi.v3.dto.common.create.IObjectCreation
import ch.ethz.sis.openbis.generic.asapi.v3.dto.common.search.SearchResult
import ch.ethz.sis.openbis.generic.asapi.v3.dto.entitytype.create.IEntityTypeCreation
import ch.ethz.sis.openbis.generic.asapi.v3.dto.experiment.create.CreateExperimentTypesOperation
import ch.ethz.sis.openbis.generic.asapi.v3.dto.experiment.create.CreateExperimentsOperation
import ch.ethz.sis.openbis.generic.asapi.v3.dto.experiment.create.ExperimentCreation
import ch.ethz.sis.openbis.generic.asapi.v3.dto.project.create.CreateProjectsOperation
import ch.ethz.sis.openbis.generic.asapi.v3.dto.project.create.ProjectCreation
import ch.ethz.sis.openbis.generic.asapi.v3.dto.sample.create.CreateSampleTypesOperation
import ch.ethz.sis.openbis.generic.asapi.v3.dto.sample.create.CreateSamplesOperation
import ch.ethz.sis.openbis.generic.asapi.v3.dto.sample.create.SampleCreation
import ch.ethz.sis.openbis.generic.asapi.v3.dto.space.create.CreateSpacesOperation
import ch.ethz.sis.openbis.generic.asapi.v3.dto.space.create.SpaceCreation
import ch.ethz.sis.openbis.generic.asapi.v3.dto.vocabulary.create.VocabularyCreation
import ch.ethz.sis.openbis.generic.asapi.v3.dto.vocabulary.create.VocabularyTermCreation
import io.mockk.every
import kotlin.test.Test
import kotlin.test.assertEquals

/** Test that the creation of entities happens in the correct order
 * and that the correct code is returned.
 * The order should be:
 * 1. Types
 * 2. Space
 * 3. Project
 * 4. Collection
 * 5. Object
 */
class MockCreationTest : MockOpenBISTest("simple_instance_with_objects.json") {


    @Test
    fun testChildren() {
        // Additional mock for the creation of objects and sample types
        every { openBIS.searchSamples(any(), any()) } returns SearchResult(listOf(), 0)
        every { openBIS.searchSampleTypes(any(), any()) } returns SearchResult(listOf(), 0)
        val res = instanceEntity.create(openBIS)
        /* We extract the code using smart casts. It would be nicer if IObjectCreation has a `getCode` method */
        //TODO: use a better way to extract the code. As ETH to add `getCode` to IObjectCreation
        val msg = res.flatMap { it.creations }.map {
            when (it) {
                is SpaceCreation -> it.code
                is SampleCreation -> it.code
                is ExperimentCreation -> it.code
                is ProjectCreation -> it.code
                is VocabularyCreation -> it.code
                is VocabularyTermCreation -> it.code
                is IEntityTypeCreation -> it.code
                else -> "Unknown"
            }
        }
        assertEquals(
            msg,
            listOf("FOT", "YOUR_SPACE_CODE", "YOUR_FIRST_PROJECT_CODE", "YOUR_FIRST_COLLECTION_CODE", "FP145", "FP146")
        )

    }

}
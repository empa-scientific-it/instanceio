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
import ch.empa.openbisio.instance.readInstance
import kotlin.test.Test
import ch.empa.openbisio.identifier.ConcreteIdentifier
import ch.empa.openbisio.instance.InstanceDTO
import ch.empa.openbisio.project.ProjectDTO
import ch.empa.openbisio.space.SpaceDTO
import ch.ethz.sis.openbis.generic.asapi.v3.dto.space.Space

class DTOTest {
    private val configFile = javaClass.getResource("/test.json").readText()
    private val inst = readInstance(configFile)

    @Test
    fun assertSpace() {
        assert(inst.getChildren("YOUR_SPACE_CODE")?.code == "YOUR_SPACE_CODE")
    }
}
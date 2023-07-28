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
import ch.empa.openbisio.instance.InstanceMapper
import ch.ethz.sis.openbis.generic.OpenBIS
import kotlinx.serialization.json.Json
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Tags
import kotlin.test.Test
@Tags
@Tag("IntegrationTest")
class DTOTest : OpenBisContainerTest("simple_instance.json") {
    //private val configFile = javaClass.getResource("/test.json").readText()
    //private val inst = readInstance(configFile)
    val js = Json { prettyPrint = true }

//    @Test
//    fun assertSpace() {
//        assert(inst.getSpace("YOUR_SPACE_CODE")?.code == "YOUR_SPACE_CODE")
//    }
//
//    @Test
//    fun testString(){
//
//        println(js.encodeToString(inst))
//    }

    @Test
    fun dumpTest() {
        val dumped = InstanceDeserializer().dumpInstance(openBIS)
        val instanceEntity = InstanceMapper(dumped).mapToEntity()
        println(instanceEntity.pprint())
        //println(dumped.collectionTypes)
        //println(js.encodeToString(updatedCodes))
    }
}
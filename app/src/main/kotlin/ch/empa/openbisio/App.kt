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

/*
 * This Kotlin source file was generated by the Gradle 'init' task.
 */
package ch.empa.openbisio

import ch.empa.openbisio.instance.InstanceDTO
import ch.empa.openbisio.instance.InstanceDeserializer
import ch.empa.openbisio.instance.InstanceSerializer
import ch.ethz.sis.openbis.generic.OpenBIS
import kotlinx.cli.*
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromStream
import java.io.File


//
//
//fun sampleFetchConfig(): SampleFetchOptions {
//    val sfo = SampleFetchOptions()
//    sfo.withType().withPropertyAssignmentsUsing(assignmentFetchOptions())
//    sfo.withProperties()
//    sfo.withRegistrator()
//    return sfo
//}
//
//fun assignmentFetchOptions(): PropertyAssignmentFetchOptions {
//    val pfo = PropertyAssignmentFetchOptions().apply {
//        this.withRegistrator()
//        this.withPropertyType()
//    }
//    return pfo
//}
//
//fun collectionFetchConfig(): ExperimentFetchOptions {
//    val efo = ExperimentFetchOptions()
//    efo.withType().withPropertyAssignmentsUsing(assignmentFetchOptions())
//    efo.withProperties()
//    efo.withRegistrator()
//
//    efo.withSamplesUsing(sampleFetchConfig())
//    return efo
//}
//
//fun projectFetchConfig(): ProjectFetchOptions {
//    val pfo = ProjectFetchOptions()
//    pfo.withLeader()
//    pfo.withRegistrator()
//    pfo.withExperimentsUsing(collectionFetchConfig())
//    pfo.withSamplesUsing(sampleFetchConfig())
//    return pfo
//}
//
//fun spaceFecthConfig(): SpaceFetchOptions {
//    val sfo = SpaceFetchOptions()
//    sfo.withProjectsUsing(projectFetchConfig())
//    sfo.withSamplesUsing(sampleFetchConfig())
//    sfo.withRegistrator()
//    return sfo
//}
//
//fun sampleTypeFetchConfig(): SampleTypeFetchOptions {
//    val stfo = SampleTypeFetchOptions().apply {
//        this.withPropertyAssignmentsUsing(assignmentFetchOptions())
//    }
//    return stfo
//}
//
//


//fun dumpInstance(service: OpenBISService): Instance {
//    val spaceSearchCriteria = SpaceSearchCriteria().withAndOperator()
//    val spaceFetchConf = spaceFecthConfig()
//    val spaces = service.con.searchSpaces(service.token, spaceSearchCriteria, spaceFetchConf).objects
//    // Get property types
//    val propertyTypeSearchCriteria = PropertyTypeSearchCriteria().withAndOperator()
//    val propertyTypeFecthOptions = PropertyTypeFetchOptions()
//    propertyTypeFecthOptions.withRegistrator()
//    val props = service.con.searchPropertyTypes(service.token, propertyTypeSearchCriteria, propertyTypeFecthOptions).objects
//    // Get object types
//    val sampleTypeSearchCriteria = SampleTypeSearchCriteria().withAndOperator()
//    val sampleTypeFetchOptions = sampleTypeFetchConfig()
//    val sampleTypes = service.con.searchSampleTypes(service.token, sampleTypeSearchCriteria, sampleTypeFetchOptions).objects
//
//    val spRep = Instance(spaces, props, sampleTypes).apply(Instance::updateCodes)
//    return spRep
//}
//
//
//
enum class Mode {
    dump,
    load
}
fun main(args: Array<String>) {

    val parser = ArgParser("example")
    val openbisURL by (parser.argument(ArgType.String))
    val username by parser.argument(ArgType.String)
    val password by parser.argument(ArgType.String)
    val mode by parser.argument(ArgType.Choice<Mode>())
    val ioFile by parser.option(ArgType.String)
    parser.parse(args)
    val service = OpenBIS(openbisURL)
    val token = service.login(username, password)
    val configFile = File(ioFile ?: "./test.json")
    val format = Json { prettyPrint = true }
    when (mode) {
        Mode.dump -> {
            val inst = InstanceDeserializer().dumpInstance(service)

            val res = format.encodeToString(InstanceDTO.serializer(), inst)
            configFile.writeText(res)
        }
        Mode.load -> {
            val serialiser = InstanceSerializer(service)
            val inst = Json.decodeFromStream(InstanceDTO.serializer(), configFile.inputStream())
            serialiser.persist(inst.toEntityWithCodes())
        }
    }
}


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

package ch.empa.openbisio.instance

import ch.empa.openbisio.mappers.OpenbisToDtoMapper
import ch.ethz.sis.openbis.generic.OpenBIS
import ch.ethz.sis.openbis.generic.asapi.v3.dto.common.interfaces.IIdentifierHolder
import ch.ethz.sis.openbis.generic.asapi.v3.dto.dataset.fetchoptions.DataSetTypeFetchOptions
import ch.ethz.sis.openbis.generic.asapi.v3.dto.dataset.search.DataSetTypeSearchCriteria
import ch.ethz.sis.openbis.generic.asapi.v3.dto.experiment.fetchoptions.ExperimentFetchOptions
import ch.ethz.sis.openbis.generic.asapi.v3.dto.experiment.fetchoptions.ExperimentTypeFetchOptions
import ch.ethz.sis.openbis.generic.asapi.v3.dto.experiment.search.ExperimentTypeSearchCriteria
import ch.ethz.sis.openbis.generic.asapi.v3.dto.project.fetchoptions.ProjectFetchOptions
import ch.ethz.sis.openbis.generic.asapi.v3.dto.property.fetchoptions.PropertyAssignmentFetchOptions
import ch.ethz.sis.openbis.generic.asapi.v3.dto.property.fetchoptions.PropertyTypeFetchOptions
import ch.ethz.sis.openbis.generic.asapi.v3.dto.property.search.PropertyTypeSearchCriteria
import ch.ethz.sis.openbis.generic.asapi.v3.dto.sample.fetchoptions.SampleFetchOptions
import ch.ethz.sis.openbis.generic.asapi.v3.dto.sample.fetchoptions.SampleTypeFetchOptions
import ch.ethz.sis.openbis.generic.asapi.v3.dto.sample.search.SampleTypeSearchCriteria
import ch.ethz.sis.openbis.generic.asapi.v3.dto.space.fetchoptions.SpaceFetchOptions
import ch.ethz.sis.openbis.generic.asapi.v3.dto.space.search.SpaceSearchCriteria
import ch.ethz.sis.openbis.generic.asapi.v3.dto.vocabulary.fetchoptions.VocabularyFetchOptions
import ch.ethz.sis.openbis.generic.asapi.v3.dto.vocabulary.search.VocabularySearchCriteria
import kotlinx.serialization.json.Json
import org.mapstruct.factory.Mappers

fun sampleFetchConfig(): SampleFetchOptions {
    val sfo = SampleFetchOptions()
    sfo.withType().withPropertyAssignmentsUsing(assignmentFetchOptions())
    sfo.withProperties()
    sfo.withRegistrator()
    sfo.withChildren()
    sfo.withParents()
    return sfo
}

fun assignmentFetchOptions(): PropertyAssignmentFetchOptions {
    val pfo = PropertyAssignmentFetchOptions().apply {
        this.withRegistrator()
        this.withPropertyType()
    }
    return pfo
}

fun collectionFetchConfig(withSamples: Boolean): ExperimentFetchOptions {
    val efo = ExperimentFetchOptions()
    efo.withType().withPropertyAssignmentsUsing(assignmentFetchOptions())
    efo.withProperties()
    efo.withRegistrator()
    if (withSamples) {
        efo.withSamplesUsing(sampleFetchConfig())
    }
    return efo
}

fun projectFetchConfig(withSamples: Boolean): ProjectFetchOptions {
    val pfo = ProjectFetchOptions()
    pfo.withLeader().apply {
        this.withSpace()
    }
    pfo.withRegistrator()
    pfo.withExperimentsUsing(collectionFetchConfig(withSamples))
    if (withSamples) {
        pfo.withSamplesUsing(sampleFetchConfig())
    }
    return pfo
}

fun spaceFecthConfig(withSamples: Boolean): SpaceFetchOptions {
    val sfo = SpaceFetchOptions()
    if (withSamples) {
        sfo.withSamplesUsing(sampleFetchConfig())
    }
    sfo.withProjectsUsing(projectFetchConfig(withSamples))
    sfo.withRegistrator()
    return sfo
}

fun sampleTypeFetchConfig(): SampleTypeFetchOptions {
    val stfo = SampleTypeFetchOptions().apply {
        this.withPropertyAssignmentsUsing(assignmentFetchOptions())
    }
    return stfo
}

fun vocabularyFetchConfig(): VocabularyFetchOptions {
    val vfo = VocabularyFetchOptions()
    vfo.withTerms().apply {
        this.withRegistrator()
    }
    return vfo
}

fun collectionTypeFetchConfig(): ExperimentTypeFetchOptions {
    val efo = ExperimentTypeFetchOptions()
    efo.withPropertyAssignments().apply { this.withPropertyType() }
    return efo
}


fun dataSetTypeFetchConfig(): DataSetTypeFetchOptions {
    val dfo = DataSetTypeFetchOptions()
    dfo.withPropertyAssignments().apply { this.withPropertyType() }
    return dfo
}


fun propertyTypeFetchOptions(): PropertyTypeFetchOptions {
    val pfo = PropertyTypeFetchOptions()
    pfo.withVocabulary()
    return pfo
}

data class InstanceDeserializer(
    val withSamples: Boolean = true,
    val withProjects: Boolean = true,
    val withSpaces: Boolean = true,
    val withVocabularies: Boolean = true,
    val withCollectionTypes: Boolean = true,
    val withObjectTypes: Boolean = true,
    val withPropertyTypes: Boolean = true,
    val withPersons: Boolean = true,
    val filter: (IIdentifierHolder) -> Boolean = { it -> true }
) {
    fun dumpInstance(service: OpenBIS): InstanceDTO {
        val spaceSearchCriteria = SpaceSearchCriteria().withAndOperator()
        val spaceFetchConf = spaceFecthConfig(withSamples)
        val spaces = service.searchSpaces(spaceSearchCriteria, spaceFetchConf).objects
        // Get property types
        val propertyTypeSearchCriteria = PropertyTypeSearchCriteria().withAndOperator()
        val propertyTypeFecthOptions = propertyTypeFetchOptions()
        propertyTypeFecthOptions.withRegistrator()
        val props = service.searchPropertyTypes(propertyTypeSearchCriteria, propertyTypeFecthOptions).objects
        // Get object types
        val sampleTypeSearchCriteria = SampleTypeSearchCriteria().withAndOperator()
        val sampleTypeFetchOptions = sampleTypeFetchConfig()
        val sampleTypes = service.searchSampleTypes(sampleTypeSearchCriteria, sampleTypeFetchOptions).objects
        val vfo = vocabularyFetchConfig()
        val vocabularySearchCriteria = VocabularySearchCriteria().withAndOperator()
        val voc = service.searchVocabularies(vocabularySearchCriteria, vfo).objects
        val ctf = collectionTypeFetchConfig()
        val collectionTypeSearchCriteria = ExperimentTypeSearchCriteria().withAndOperator()
        val col = service.searchExperimentTypes(collectionTypeSearchCriteria, ctf).objects
        val dst =
            service.searchDataSetTypes(DataSetTypeSearchCriteria().withAndOperator(), dataSetTypeFetchConfig()).objects
        //Perform mapping
        val mapper = Mappers.getMapper(OpenbisToDtoMapper::class.java)
        val mappedSpaces = mapper.spaceListToSpaceDTOList(spaces)
        val mappedVocabularies = mapper.vocabularyListToVocabularyDTOList(voc)
        val mappedObjectTypes = mapper.sampleTypeListToObjectTypeDTOList(sampleTypes)
        val mapppedCollectionTypes = mapper.experimentTypeListToCollectionTypeDTOList(col)
        val mappedPropertyTypes = mapper.propertyTypeListToPropertyTypeDTOList(props)
        val mappedDataSetTypes = mapper.dataSetTypeListToDataSetTypeDTOList(dst)
        //Put everything together
        val spRep = InstanceDTO(
            spaces = mappedSpaces,
            propertyTypes = mappedPropertyTypes,
            objectTypes = mappedObjectTypes,
            collectionTypes = mapppedCollectionTypes,
            vocabularies = mappedVocabularies,
            dataSetTypes = mappedDataSetTypes
        )
        return spRep
    }

}







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

import ch.empa.openbisio.collection.CollectionDTO
import ch.empa.openbisio.collectiontype.CollectionTypeDTO
import ch.empa.openbisio.datasettype.DataSetTypeDTO
import ch.empa.openbisio.datatype.DataTypeDTO
import ch.empa.openbisio.`object`.ObjectDTO
import ch.empa.openbisio.objectype.ObjectTypeDTO
import ch.empa.openbisio.person.PersonDTO
import ch.empa.openbisio.project.ProjectDTO
import ch.empa.openbisio.propertyassignment.PropertyAssignmentDTO
import ch.empa.openbisio.propertytype.PropertyTypeDTO
import ch.empa.openbisio.space.SpaceDTO
import ch.empa.openbisio.vocabulary.VocabularyDTO
import ch.empa.openbisio.vocabulary.VocabularyTermDTO
import ch.ethz.sis.openbis.generic.OpenBIS
import ch.ethz.sis.openbis.generic.asapi.v3.dto.common.interfaces.IIdentifierHolder
import ch.ethz.sis.openbis.generic.asapi.v3.dto.dataset.DataSetType
import ch.ethz.sis.openbis.generic.asapi.v3.dto.dataset.fetchoptions.DataSetTypeFetchOptions
import ch.ethz.sis.openbis.generic.asapi.v3.dto.dataset.search.DataSetTypeSearchCriteria
import ch.ethz.sis.openbis.generic.asapi.v3.dto.experiment.Experiment
import ch.ethz.sis.openbis.generic.asapi.v3.dto.experiment.ExperimentType
import ch.ethz.sis.openbis.generic.asapi.v3.dto.experiment.fetchoptions.ExperimentFetchOptions
import ch.ethz.sis.openbis.generic.asapi.v3.dto.experiment.fetchoptions.ExperimentTypeFetchOptions
import ch.ethz.sis.openbis.generic.asapi.v3.dto.experiment.search.ExperimentTypeSearchCriteria
import ch.ethz.sis.openbis.generic.asapi.v3.dto.person.Person
import ch.ethz.sis.openbis.generic.asapi.v3.dto.project.Project
import ch.ethz.sis.openbis.generic.asapi.v3.dto.project.fetchoptions.ProjectFetchOptions
import ch.ethz.sis.openbis.generic.asapi.v3.dto.property.DataType
import ch.ethz.sis.openbis.generic.asapi.v3.dto.property.PropertyAssignment
import ch.ethz.sis.openbis.generic.asapi.v3.dto.property.PropertyType
import ch.ethz.sis.openbis.generic.asapi.v3.dto.property.fetchoptions.PropertyAssignmentFetchOptions
import ch.ethz.sis.openbis.generic.asapi.v3.dto.property.fetchoptions.PropertyTypeFetchOptions
import ch.ethz.sis.openbis.generic.asapi.v3.dto.property.search.PropertyTypeSearchCriteria
import ch.ethz.sis.openbis.generic.asapi.v3.dto.sample.Sample
import ch.ethz.sis.openbis.generic.asapi.v3.dto.sample.SampleType
import ch.ethz.sis.openbis.generic.asapi.v3.dto.sample.fetchoptions.SampleFetchOptions
import ch.ethz.sis.openbis.generic.asapi.v3.dto.sample.fetchoptions.SampleTypeFetchOptions
import ch.ethz.sis.openbis.generic.asapi.v3.dto.sample.search.SampleTypeSearchCriteria
import ch.ethz.sis.openbis.generic.asapi.v3.dto.space.Space
import ch.ethz.sis.openbis.generic.asapi.v3.dto.space.fetchoptions.SpaceFetchOptions
import ch.ethz.sis.openbis.generic.asapi.v3.dto.space.search.SpaceSearchCriteria
import ch.ethz.sis.openbis.generic.asapi.v3.dto.vocabulary.Vocabulary
import ch.ethz.sis.openbis.generic.asapi.v3.dto.vocabulary.VocabularyTerm
import ch.ethz.sis.openbis.generic.asapi.v3.dto.vocabulary.fetchoptions.VocabularyFetchOptions
import ch.ethz.sis.openbis.generic.asapi.v3.dto.vocabulary.search.VocabularySearchCriteria
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json


fun sampleFetchConfig(): SampleFetchOptions {
    val sfo = SampleFetchOptions()
    sfo.withType().withPropertyAssignmentsUsing(assignmentFetchOptions())
    sfo.withProperties()
    sfo.withRegistrator()
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
    pfo.withLeader()
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

fun Space.toDTO(): SpaceDTO {
    return SpaceDTO(this.code, this.projects.map { it.toDTO() })
}

fun Person.toDTO(): PersonDTO {
    return PersonDTO(
        code = this.permId.permId,
        space = this.space.code,
        email = this.email,
        firstName = this.firstName,
        lastName = this.lastName
    )
}

fun Project.toDTO(): ProjectDTO {
    return ProjectDTO(
        code = this.code,
        collections = this.experiments.map { it.toDTO() },
        description = this.description ?: "",
        leader = this.leader?.toDTO(),
    )
}

fun Sample.toDTO(): ObjectDTO {
    return ObjectDTO(this.code, this.type.code, this.properties)
}

fun Experiment.toDTO(): CollectionDTO {
    val sampleFetched = this.fetchOptions.hasSamples()
    val samples = if (sampleFetched) this.samples.map { it.toDTO() } else listOf()
    return CollectionDTO(this.code, this.type.code, samples)
}

fun Vocabulary.toDTO(): VocabularyDTO {
    return VocabularyDTO(this.code, this.description, this.terms.map { it.toDTO() })
}

fun VocabularyTerm.toDTO(): VocabularyTermDTO {
    return VocabularyTermDTO(this.code, this.label ?: "", this.description ?: "", this.isOfficial ?: false)
}


fun ExperimentType.toDTO(): CollectionTypeDTO {
    return CollectionTypeDTO(this.code, this.description, this.propertyAssignments.map { it.toDTO() })
}

fun DataType.toDTO(): DataTypeDTO {
    when (this) {
        DataType.VARCHAR -> return DataTypeDTO.VARCHAR
        DataType.BOOLEAN -> return DataTypeDTO.BOOLEAN
        DataType.DATE -> return DataTypeDTO.DATE
        DataType.HYPERLINK -> return DataTypeDTO.HYPERLINK
        DataType.INTEGER -> return DataTypeDTO.INTEGER
        DataType.CONTROLLEDVOCABULARY -> return DataTypeDTO.CONTROLLEDVOCABULARY
        DataType.REAL -> return DataTypeDTO.REAL
        DataType.XML -> return DataTypeDTO.XML
        DataType.TIMESTAMP -> return DataTypeDTO.TIMESTAMP
        DataType.MULTILINE_VARCHAR -> return DataTypeDTO.MULTILINE_VARCHAR
        DataType.SAMPLE -> return DataTypeDTO.OBJECT
        DataType.MATERIAL -> return DataTypeDTO.OBJECT
    }

}


fun DataSetType.toDTO(): DataSetTypeDTO {
    return DataSetTypeDTO(this.code, this.description, this.propertyAssignments.map { it.toDTO() })
}

fun PropertyType.toDTO(): PropertyTypeDTO {
    return PropertyTypeDTO(this.code, this.label, this.description, this.dataType.toDTO())
}

fun PropertyAssignment.toDTO(): PropertyAssignmentDTO {
    return PropertyAssignmentDTO(this.section, this.isMandatory, this.propertyType.code)
}

fun SampleType.toDTO(): ObjectTypeDTO {
    return ObjectTypeDTO(
        this.code,
        this.description,
        this.generatedCodePrefix,
        this.propertyAssignments.map { it.toDTO() })
}


data class InstanceDeserializer(
    val withSamples: Boolean = false,
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
        val propertyTypeFecthOptions = PropertyTypeFetchOptions()
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
        //Put everything together
        val spRep = InstanceDTO(
            spaces = spaces.map { it.toDTO() },
            propertyTypes = listOf(),
            objectTypes = sampleTypes.map { it.toDTO() },
            collectionTypes = col.map { it.toDTO() },
            vocabularies = voc.map { it.toDTO() },
            dataSetTypes = dst.map { it.toDTO() })
        return spRep
    }

}


fun readInstance(config: String): InstanceDTO {
    return Json.decodeFromString<InstanceDTO>(config)
}




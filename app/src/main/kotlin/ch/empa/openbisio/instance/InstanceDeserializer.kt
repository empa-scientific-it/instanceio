package ch.empa.openbisio.instance

import ch.empa.openbisio.collection.CollectionDTO
import ch.empa.openbisio.collectiontype.CollectionTypeDTO
import ch.empa.openbisio.`object`.ObjectDTO
import ch.empa.openbisio.objectype.ObjectTypeDTO
import ch.empa.openbisio.person.PersonDTO
import ch.empa.openbisio.project.ProjectDTO
import ch.empa.openbisio.space.SpaceDTO
import ch.empa.openbisio.vocabulary.VocabularyDTO
import ch.empa.openbisio.vocabulary.VocabularyTermDTO
import ch.ethz.sis.openbis.generic.OpenBIS
import ch.ethz.sis.openbis.generic.asapi.v3.dto.experiment.Experiment
import ch.ethz.sis.openbis.generic.asapi.v3.dto.experiment.ExperimentType
import ch.ethz.sis.openbis.generic.asapi.v3.dto.experiment.fetchoptions.ExperimentFetchOptions
import ch.ethz.sis.openbis.generic.asapi.v3.dto.experiment.fetchoptions.ExperimentTypeFetchOptions
import ch.ethz.sis.openbis.generic.asapi.v3.dto.experiment.search.ExperimentTypeSearchCriteria
import ch.ethz.sis.openbis.generic.asapi.v3.dto.person.Person
import ch.ethz.sis.openbis.generic.asapi.v3.dto.project.Project
import ch.ethz.sis.openbis.generic.asapi.v3.dto.project.fetchoptions.ProjectFetchOptions
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

fun collectionFetchConfig(): ExperimentFetchOptions {
    val efo = ExperimentFetchOptions()
    efo.withType().withPropertyAssignmentsUsing(assignmentFetchOptions())
    efo.withProperties()
    efo.withRegistrator()

    efo.withSamplesUsing(sampleFetchConfig())
    return efo
}

fun projectFetchConfig(): ProjectFetchOptions {
    val pfo = ProjectFetchOptions()
    pfo.withLeader()
    pfo.withRegistrator()
    pfo.withExperimentsUsing(collectionFetchConfig())
    pfo.withSamplesUsing(sampleFetchConfig())
    return pfo
}

fun spaceFecthConfig(): SpaceFetchOptions {
    val sfo = SpaceFetchOptions()
    sfo.withProjectsUsing(projectFetchConfig())
    sfo.withSamplesUsing(sampleFetchConfig())
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

fun collectionTypeFetchConfig(): ExperimentTypeFetchOptions{
    val efo = ExperimentTypeFetchOptions()
    efo.withPropertyAssignments()
    return efo
}

fun Space.toDTO(): SpaceDTO {
    return SpaceDTO(this.code, this.projects.map { it.toDTO() })
}

fun Person.toDTO(): PersonDTO {
    return PersonDTO(code = this.permId.permId, space = this.space.code, email = this.email, firstName = this.firstName, lastName = this.lastName)
}

fun Project.toDTO(): ProjectDTO {
    return ProjectDTO(
        code = this.code,
        collections = this.experiments.map { it.toDTO() } ,
        description = this.description ?: "",
        leader = this.leader?.toDTO() ?: PersonDTO("a", "a", "a", "a", "a"),
    )
}

fun Sample.toDTO(): ObjectDTO {
    return ObjectDTO(this.code, this.type.code, this.properties)
}

fun Experiment.toDTO(): CollectionDTO {
    return CollectionDTO(this.code, this.samples.map { it.toDTO() }, this.type.code)
}

fun Vocabulary.toDTO(): VocabularyDTO{
    return VocabularyDTO(this.code, this.description, this.terms.map { it.toDTO() })
}

fun VocabularyTerm.toDTO(): VocabularyTermDTO {
    return VocabularyTermDTO(this.code, this.label ?:"a")
}


fun ExperimentType.toDTO(): CollectionTypeDTO{
    return CollectionTypeDTO(this.code, this.description, listOf())
}

fun SampleType.toDTO(): ObjectTypeDTO {
//    val sections = this.propertyAssignments.map {
//        Triple(it.section, it.permId.propertyTypeId.toString(), it.propertyType.code)
//    }.groupBy { it.first }.mapValues { SectionDTO(it.key, it.value.associate { it -> it.second to it.third }) }.values.toList()
    return ObjectTypeDTO(
        this.code,
        this.description,
        this.generatedCodePrefix,
        listOf())
}


class InstanceDeserializer {
    val withSamples: Boolean = true
    val withProjects: Boolean = true
    val withSpaces: Boolean = true
    val withVocabularies: Boolean = true
    val withCollectionTypes: Boolean = true
    val withObjectTypes: Boolean = true
    val withPropertyTypes: Boolean = true
    val withPersons: Boolean = true
    fun dumpInstance(service: OpenBIS): InstanceDTO {
        val spaceSearchCriteria = SpaceSearchCriteria().withAndOperator()
        val spaceFetchConf = spaceFecthConfig()
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
        val voc = service.searchVocabularies(vocabularySearchCriteria,vfo).objects
        val ctfo = collectionTypeFetchConfig()
        val collectionTypeSearchCriteria = ExperimentTypeSearchCriteria().withAndOperator()
        val col = service.searchExperimentTypes(collectionTypeSearchCriteria, ctfo).objects
        //Put everything together
        val spRep = InstanceDTO(
            spaces = spaces.map { it.toDTO() },
            propertyTypes = listOf(),
            objectTypes = sampleTypes.map { it.toDTO() },
            collectionTypes = col.map { it.toDTO() } ,
            vocabularies = voc.map { it.toDTO() })
        return spRep
    }

}



fun readInstance(config: String): InstanceDTO {
    return Json.decodeFromString<InstanceDTO>(config)
}




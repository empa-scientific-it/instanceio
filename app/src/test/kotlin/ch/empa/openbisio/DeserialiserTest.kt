package ch.empa.openbisio

import ch.empa.openbisio.collection.CollectionDTO
import ch.empa.openbisio.instance.InstanceDTO
import ch.empa.openbisio.project.ProjectDTO
import ch.empa.openbisio.space.SpaceDTO
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import kotlin.test.Test

/**
 * Test the serialisation and deserialisation of the InstanceDTO class. The test checks
 * whether:
 * 1. the deserialised instance is equal to the local instance
 * 2. the serialised instance is equal to the deserialised instance
 */
class DeserialiserTest {

    val localInst = InstanceDTO().withSpace(
        SpaceDTO("YOUR_SPACE_CODE").withProject(
            ProjectDTO("YOUR_FIRST_PROJECT_CODE").withCollection(
                CollectionDTO("YOUR_FIRST_COLLECTION_CODE", type = "COLLECTION")
            )
        )
    )

    val json = Json { prettyPrint = true }
    val config = javaClass.getResource("/simple_instance.json").readText()

    @Test
    fun testRead() {
        val readInst = json.decodeFromString<InstanceDTO>(config)
        assert(readInst == localInst)
    }

    @Test
    fun testWrite() {
        val readInst = json.decodeFromString<InstanceDTO>(config)
        val writeInst = json.encodeToString(InstanceDTO.serializer(), readInst)
        val rereadInst = json.decodeFromString<InstanceDTO>(writeInst)
        assert(readInst == rereadInst && rereadInst == localInst)
    }
}
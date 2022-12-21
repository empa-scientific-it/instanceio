package openbisio.models

import ch.ethz.sis.openbis.generic.asapi.v3.IApplicationServerApi
import ch.ethz.sis.openbis.generic.asapi.v3.dto.common.interfaces.IPermIdHolder
import ch.ethz.sis.openbis.generic.asapi.v3.dto.experiment.fetchoptions.ExperimentFetchOptions
import ch.ethz.sis.openbis.generic.asapi.v3.dto.experiment.search.ExperimentSearchCriteria
import ch.ethz.sis.openbis.generic.asapi.v3.dto.sample.Sample
import ch.ethz.sis.openbis.generic.asapi.v3.dto.sample.create.SampleCreation
import ch.ethz.sis.openbis.generic.asapi.v3.dto.sample.fetchoptions.SampleTypeFetchOptions
import ch.ethz.sis.openbis.generic.asapi.v3.dto.sample.search.SampleTypeSearchCriteria
import kotlinx.serialization.Serializable


@Serializable
class OpenbisObject(
    override val code: String,
    private val type: String,
    override val ancestorsCodes: MutableList<String>?,
    override val children: List<IOpenbisHierarchyObject>? = null,
    val properties: Map<String, String>,
    override val registrator: OpenbisPerson,
    ) : OpenbisCreatable(), IOpenbisHierarchyObject {

    constructor(
        o: Sample
    ) : this(o.code, o.type.code, mutableListOf(), null, o.properties, OpenbisPerson(o.getRegistrator()))


    override fun getFromOpenBIS(connection: IApplicationServerApi, token: String): IPermIdHolder? {
        TODO("Not yet implemented")
    }

    override fun createOperation(connection: IApplicationServerApi, token: String) {
        val typeResult = connection.searchSampleTypes(token, SampleTypeSearchCriteria().apply { this.withCode().thatEquals(type) },
            SampleTypeFetchOptions()
        )
        val experimentResult = connection.searchExperiments(token, ExperimentSearchCriteria().apply { this.withIdentifier().thatEquals(code) },
            ExperimentFetchOptions()
        )
        val sc = SampleCreation().apply {
            this.code = code
            this.experimentId = experimentResult!!.objects[0].permId
            this.typeId = typeResult!!.objects[0].permId
            this.properties = properties
        }
        connection.createSamples(token, listOf(sc))
    }


}
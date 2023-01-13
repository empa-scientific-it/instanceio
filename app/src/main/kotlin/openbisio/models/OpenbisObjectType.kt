package openbisio.models

import ch.ethz.sis.openbis.generic.asapi.v3.IApplicationServerApi
import ch.ethz.sis.openbis.generic.asapi.v3.dto.common.interfaces.IPermIdHolder
import ch.ethz.sis.openbis.generic.asapi.v3.dto.property.fetchoptions.PropertyTypeFetchOptions
import ch.ethz.sis.openbis.generic.asapi.v3.dto.sample.SampleType
import ch.ethz.sis.openbis.generic.asapi.v3.dto.sample.create.SampleTypeCreation
import ch.ethz.sis.openbis.generic.asapi.v3.dto.sample.fetchoptions.SampleTypeFetchOptions
import ch.ethz.sis.openbis.generic.asapi.v3.dto.sample.search.SampleTypeSearchCriteria
import kotlinx.serialization.Serializable

@Serializable
class OpenbisObjectType(
    override val code: String,
    var properties: List<OpenbisPropertyAssignment>,
    override val registrator: OpenbisPerson?
) : OpenbisCreatable() {
    constructor(
        ot: SampleType
    ) : this(
        ot.code,
        ot.propertyAssignments.map { OpenbisPropertyAssignment(it) },
        ot.propertyAssignments.map { OpenbisPerson(it.getRegistrator()) }.elementAtOrNull(0)
    )

    override fun getFromOpenBIS(connection: IApplicationServerApi, token: String): IPermIdHolder? {
        val sc = SampleTypeSearchCriteria().apply { withCode().thatEquals(code) }
        val res = connection.searchSampleTypes(token, sc, SampleTypeFetchOptions())
        return res.objects[0]
    }

    override fun createOperation(connection: IApplicationServerApi, token: String) {
        val stc=  SampleTypeCreation().apply {
            code = code
            properties = properties
        }
        connection.createSampleTypes(token, mutableListOf(stc))
    }
}
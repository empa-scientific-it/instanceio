package openbisio.models

import ch.ethz.sis.openbis.generic.asapi.v3.IApplicationServerApi
import ch.ethz.sis.openbis.generic.asapi.v3.dto.common.interfaces.IPermIdHolder
import ch.ethz.sis.openbis.generic.asapi.v3.dto.sample.SampleType
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
        TODO("Not yet implemented")
    }

    override fun createOperation(connection: IApplicationServerApi, token: String) {
        TODO("Not yet implemented")
    }
}
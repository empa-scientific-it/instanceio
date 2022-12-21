package openbisio.models

import ch.ethz.sis.openbis.generic.asapi.v3.IApplicationServerApi
import ch.ethz.sis.openbis.generic.asapi.v3.dto.common.interfaces.IPermIdHolder
import ch.ethz.sis.openbis.generic.asapi.v3.dto.property.PropertyAssignment
import kotlinx.serialization.Serializable
import openbisio.models.IOpenbisEntity


@Serializable
class OpenbisPropertyAssignment(
    override val code: String,
    val section: String?,
    val mandatory: Boolean,
    override val registrator: OpenbisPerson
) : OpenbisCreatable() {
    constructor(
        pa: PropertyAssignment
    ) : this(
        pa.getPermId().getPropertyTypeId().toString(),
        pa.getSection(),
        pa.isMandatory(),
        OpenbisPerson(pa.getRegistrator())
    )

    override fun getFromOpenBIS(connection: IApplicationServerApi, token: String): IPermIdHolder? {
        TODO("Not yet implemented")
    }

    override fun createOperation(connection: IApplicationServerApi, token: String) {
        TODO("Not yet implemented")
    }
}
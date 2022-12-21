package openbisio.models

import ch.ethz.sis.openbis.generic.asapi.v3.IApplicationServerApi
import ch.ethz.sis.openbis.generic.asapi.v3.dto.common.interfaces.IPermIdHolder
import ch.ethz.sis.openbis.generic.asapi.v3.dto.property.PropertyType
import kotlinx.serialization.Serializable
import openbisio.DataType

@Serializable
class OpenbisPropertyType(
    override val code: String,
    val label: String,
    val description: String,
    val dataType: DataType,
    override val registrator: OpenbisPerson?
) : OpenbisCreatable() {
    constructor(
        pt: PropertyType
    ) : this(pt.code, pt.label, pt.description, DataType(pt.dataType), OpenbisPerson(pt.getRegistrator()))

    override fun getFromOpenBIS(connection: IApplicationServerApi, token: String): IPermIdHolder? {
        TODO("Not yet implemented")
    }

    override fun createOperation(connection: IApplicationServerApi, token: String) {
        TODO("Not yet implemented")
    }
}
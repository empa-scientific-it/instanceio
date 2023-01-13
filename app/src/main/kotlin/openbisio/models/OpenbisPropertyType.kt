package openbisio.models

import ch.ethz.sis.openbis.generic.asapi.v3.IApplicationServerApi
import ch.ethz.sis.openbis.generic.asapi.v3.dto.common.interfaces.IPermIdHolder
import ch.ethz.sis.openbis.generic.asapi.v3.dto.property.PropertyType
import ch.ethz.sis.openbis.generic.asapi.v3.dto.property.create.PropertyTypeCreation
import ch.ethz.sis.openbis.generic.asapi.v3.dto.property.fetchoptions.PropertyTypeFetchOptions
import ch.ethz.sis.openbis.generic.asapi.v3.dto.property.id.PropertyTypePermId
import ch.ethz.sis.openbis.generic.asapi.v3.dto.property.search.PropertyTypeSearchCriteria
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
        val sc = PropertyTypeSearchCriteria().apply {  withCode().thatEquals(code)}
        val res=  connection.searchPropertyTypes(token, sc, PropertyTypeFetchOptions())
        return res.objects[0] ?: null
    }

    override fun createOperation(connection: IApplicationServerApi, token: String) {
        val cr = PropertyTypeCreation().apply {
            code = code
            label = label
            description = description
            dataType = dataType
        }
        connection.createPropertyTypes(token, listOf(cr))
    }
}
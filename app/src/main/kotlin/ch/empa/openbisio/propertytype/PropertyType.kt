package ch.empa.openbisio.propertytype

import ch.empa.openbisio.datatype.DataType
import ch.empa.openbisio.interfaces.ICreatable
import ch.empa.openbisio.person.Person
import ch.empa.openbisio.openbis.OpenBISService
import ch.ethz.sis.openbis.generic.asapi.v3.dto.common.interfaces.IPermIdHolder
import ch.ethz.sis.openbis.generic.asapi.v3.dto.common.operation.IOperation
import ch.ethz.sis.openbis.generic.asapi.v3.dto.property.PropertyType
import ch.ethz.sis.openbis.generic.asapi.v3.dto.property.create.CreatePropertyTypesOperation
import ch.ethz.sis.openbis.generic.asapi.v3.dto.property.create.PropertyTypeCreation
import ch.ethz.sis.openbis.generic.asapi.v3.dto.property.fetchoptions.PropertyTypeFetchOptions
import ch.ethz.sis.openbis.generic.asapi.v3.dto.property.search.PropertyTypeSearchCriteria
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

@Serializable
class PropertyType(
    override val code: String,
    val label: String,
    val description: String,
    val dataType: DataType,
    @Transient val registrator: Person? = null
) : ICreatable {
    constructor(
        pt: PropertyType
    ) : this(pt.code, pt.label, pt.description, DataType(pt.dataType), Person(pt.registrator))

    override fun getFromAS(connection: OpenBISService): IPermIdHolder? {
        val sc = PropertyTypeSearchCriteria().apply {  withCode().thatEquals(code)}
        val res=  connection.con.searchPropertyTypes(connection.token, sc, PropertyTypeFetchOptions())
        return res.objects[0] ?: null
    }

    override fun createOperation(connection: OpenBISService): List<IOperation> {
        val cr = PropertyTypeCreation().apply {
            code = code
            label = label
            description = description
            dataType = dataType
        }
        return listOf(CreatePropertyTypesOperation(listOf(cr)))
    }
}
package ch.empa.openbisio.propertyassignment

import ch.empa.openbisio.datatype.DataType
import ch.empa.openbisio.interfaces.ICreatable
import ch.empa.openbisio.interfaces.RegistratorHolder
import ch.empa.openbisio.openbis.OpenBISService
import ch.empa.openbisio.person.Person
import ch.ethz.sis.openbis.generic.asapi.v3.dto.common.interfaces.IPermIdHolder
import ch.ethz.sis.openbis.generic.asapi.v3.dto.common.operation.IOperation
import ch.ethz.sis.openbis.generic.asapi.v3.dto.property.PropertyAssignment
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

@Serializable
class PropertyAssignment(
    override val code: String,
    val section: String?,
    val mandatory: Boolean,
    val type: DataType,
    @Transient override val registrator: Person? = null
) : ICreatable, RegistratorHolder {
    constructor(
        pa: PropertyAssignment
    ) : this(
        pa.permId.propertyTypeId.toString(),
        pa.section,
        pa.isMandatory,
        type = DataType(pa.propertyType.dataType),
        Person(pa.registrator)
    )

    override fun getFromAS(connection: OpenBISService): IPermIdHolder? {
        TODO("Not yet implemented")
    }

    override fun createOperation(connection: OpenBISService): List<IOperation> {
        return listOf()
    }
}
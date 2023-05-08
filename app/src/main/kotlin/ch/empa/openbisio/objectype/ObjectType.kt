package ch.empa.openbisio.objectype

import ch.empa.openbisio.interfaces.Creatable
import ch.empa.openbisio.person.Person
import ch.empa.openbisio.propertyassignment.PropertyAssignment
import ch.empa.openbisio.openbis.OpenBISService
import ch.ethz.sis.openbis.generic.asapi.v3.dto.common.interfaces.IPermIdHolder
import ch.ethz.sis.openbis.generic.asapi.v3.dto.common.operation.IOperation
import ch.ethz.sis.openbis.generic.asapi.v3.dto.sample.SampleType
import ch.ethz.sis.openbis.generic.asapi.v3.dto.sample.create.CreateSampleTypesOperation
import ch.ethz.sis.openbis.generic.asapi.v3.dto.sample.create.SampleTypeCreation
import ch.ethz.sis.openbis.generic.asapi.v3.dto.sample.fetchoptions.SampleTypeFetchOptions
import ch.ethz.sis.openbis.generic.asapi.v3.dto.sample.search.SampleTypeSearchCriteria
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

@Serializable
class ObjectType(
    override val code: String,
    var properties: List<PropertyAssignment>,
    @Transient val registrator: Person? = null
) : Creatable() {
    constructor(
        ot: SampleType
    ) : this(
        ot.code,
        ot.propertyAssignments.map { PropertyAssignment(it) },
        ot.propertyAssignments.map { Person(it.registrator) }.elementAtOrNull(0)
    )

    override fun getFromAS(connection: OpenBISService): IPermIdHolder? {
        val sc = SampleTypeSearchCriteria().apply { withCode().thatEquals(code) }
        val res = connection.con.searchSampleTypes(connection.token, sc, SampleTypeFetchOptions())
        return res.objects[0]
    }

    override fun createOperation(connection: OpenBISService): List<IOperation> {
        val stc=  SampleTypeCreation().apply {
            code = code
        }
        return listOf(CreateSampleTypesOperation(mutableListOf(stc)))
    }
}
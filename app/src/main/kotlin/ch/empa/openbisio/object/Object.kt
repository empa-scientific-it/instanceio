package ch.empa.openbisio.`object`

import ch.empa.openbisio.interfaces.*
import ch.empa.openbisio.openbis.OpenBISService
import ch.empa.openbisio.person.Person
import ch.ethz.sis.openbis.generic.asapi.v3.IApplicationServerApi
import ch.ethz.sis.openbis.generic.asapi.v3.dto.common.interfaces.IPermIdHolder
import ch.ethz.sis.openbis.generic.asapi.v3.dto.common.operation.IOperation
import ch.ethz.sis.openbis.generic.asapi.v3.dto.experiment.fetchoptions.ExperimentFetchOptions
import ch.ethz.sis.openbis.generic.asapi.v3.dto.experiment.search.ExperimentSearchCriteria
import ch.ethz.sis.openbis.generic.asapi.v3.dto.sample.Sample
import ch.ethz.sis.openbis.generic.asapi.v3.dto.sample.SampleType
import ch.ethz.sis.openbis.generic.asapi.v3.dto.sample.create.CreateSamplesOperation
import ch.ethz.sis.openbis.generic.asapi.v3.dto.sample.create.SampleCreation
import ch.ethz.sis.openbis.generic.asapi.v3.dto.sample.fetchoptions.SampleFetchOptions
import ch.ethz.sis.openbis.generic.asapi.v3.dto.sample.fetchoptions.SampleTypeFetchOptions
import ch.ethz.sis.openbis.generic.asapi.v3.dto.sample.search.SampleSearchCriteria
import ch.ethz.sis.openbis.generic.asapi.v3.dto.sample.search.SampleTypeSearchCriteria
import ch.ethz.sis.openbis.generic.asapi.v3.dto.space.id.SpacePermId
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

@Serializable
class Object(
    override val code: String,
    private val type: String,
    @Transient override val ancestorCodes: ArrayDeque<String> = ArrayDeque(listOf()),
    override val children: MutableList<CreatableHierarchyComponent>? = null,
    override val properties: Map<String, String>?,
    @Transient override val registrator: Person? = null,
) : CreatableHierarchyComponent, PropertyHolder, RegistratorHolder {

    constructor(
        o: Sample
    ) : this(o.code, o.type.code, ArrayDeque(listOf()), null, o.properties, Person(o.registrator))

    fun getType(connection: IApplicationServerApi, token: String): SampleType {
        val typeResult = connection.searchSampleTypes(
            token, SampleTypeSearchCriteria().apply { this.withCode().thatEquals(type) },
            SampleTypeFetchOptions()
        )
        return typeResult.objects[0]
    }

    override fun getFromAS(connection: OpenBISService): IPermIdHolder? {
        val sc = SampleSearchCriteria().apply { withCode().thatEquals(code) }.withAndOperator()
            .apply { withProject().withCode().thatEquals(ancestorCodes[2]) }
        val res = connection.con.searchSamples(connection.token, sc, SampleFetchOptions())
        return res.objects[0]
    }


    override val identifier: HierarchyIdentifier
        get() = TODO("Not yet implemented")

    override fun createOperation(connection: OpenBISService): List<IOperation> {
        val typeResult = connection.con.searchSampleTypes(
            connection.token, SampleTypeSearchCriteria().apply { this.withCode().thatEquals(type) },
            SampleTypeFetchOptions()
        )
        val experimentResult = connection.con.searchExperiments(
            connection.token, ExperimentSearchCriteria().apply { this.withIdentifier().thatEquals(code) },
            ExperimentFetchOptions()
        )
        val sc = SampleCreation().apply {
            this.code = code
            this.experimentId = experimentResult!!.objects[0].permId
            this.typeId = typeResult!!.objects[0].permId
            this.properties = properties
            this.spaceId = SpacePermId(identifier.getAncestor()!!.getAncestor()!!.identifier)
        }
        return listOf(CreateSamplesOperation(sc))
    }


}
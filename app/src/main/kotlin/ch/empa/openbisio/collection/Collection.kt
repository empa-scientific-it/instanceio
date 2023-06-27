package ch.empa.openbisio.collection

import ch.empa.openbisio.identifier.ConcreteIdentifier
import ch.empa.openbisio.interfaces.*
import ch.empa.openbisio.openbis.OpenBISService
import ch.empa.openbisio.person.Person
import ch.ethz.sis.openbis.generic.asapi.v3.dto.common.interfaces.IPermIdHolder
import ch.ethz.sis.openbis.generic.asapi.v3.dto.common.operation.IOperation
import ch.ethz.sis.openbis.generic.asapi.v3.dto.entitytype.id.EntityTypePermId
import ch.ethz.sis.openbis.generic.asapi.v3.dto.experiment.Experiment
import ch.ethz.sis.openbis.generic.asapi.v3.dto.experiment.create.CreateExperimentsOperation
import ch.ethz.sis.openbis.generic.asapi.v3.dto.experiment.create.ExperimentCreation
import ch.ethz.sis.openbis.generic.asapi.v3.dto.experiment.fetchoptions.ExperimentFetchOptions
import ch.ethz.sis.openbis.generic.asapi.v3.dto.experiment.search.ExperimentSearchCriteria
import ch.ethz.sis.openbis.generic.asapi.v3.dto.project.id.ProjectIdentifier
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import ch.empa.openbisio.`object`.Object as Object1

@Serializable
data class Collection(
    override val code: String,
    @Transient override val ancestorCodes: ArrayDeque<String> = ArrayDeque(listOf()),
    val type: String,
    @SerialName("samples") override val children: MutableList<Object1>? = null,
    @Transient override val registrator: Person? = null,
    override val properties: Map<String, String>? = null,
) : CreatableHierarchyComponent, PropertyHolder, RegistratorHolder {
    constructor(
        c: Experiment,
        includeSamples: Boolean = false,
    ) : this(
        c.code,
        ArrayDeque(listOf()),
        c.type.code,
        if (includeSamples) c.samples.map { Object1(it) }.toMutableList() else null,
        Person(c.registrator),
        c.properties)


    override val identifier: ConcreteIdentifier.CollectionIdentifier
        get() = ConcreteIdentifier.CollectionIdentifier(ancestorCodes + code)


    override fun createOperation(connection: OpenBISService): List<IOperation> {
        val ec = ExperimentCreation().also {
            it.code = code
            it.projectId = ProjectIdentifier(identifier.getAncestor().identifier)
            it.typeId = EntityTypePermId(type)
        }
        return listOf(CreateExperimentsOperation(ec))
    }

    override fun getFromAS(connection: OpenBISService): IPermIdHolder? {
        val so = ExperimentSearchCriteria().apply { withIdentifier().thatEquals(identifier.identifier) }
        val res = connection.con.searchExperiments(connection.token, so, ExperimentFetchOptions())
        return if (res.totalCount > 0) {
            res.objects[0]
        } else {
            null
        }
    }


}
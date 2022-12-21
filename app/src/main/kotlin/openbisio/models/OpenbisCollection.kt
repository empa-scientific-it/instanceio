package openbisio.models

import ch.ethz.sis.openbis.generic.asapi.v3.IApplicationServerApi
import ch.ethz.sis.openbis.generic.asapi.v3.dto.common.interfaces.IPermIdHolder
import ch.ethz.sis.openbis.generic.asapi.v3.dto.experiment.Experiment
import ch.ethz.sis.openbis.generic.asapi.v3.dto.experiment.create.*
import kotlinx.serialization.Serializable

@Serializable
class OpenbisCollection(
    override val code: String,
    override val ancestorsCodes: MutableList<String>?,
    val type: String,
    override var children: List<OpenbisObject>?,
    override val registrator: OpenbisPerson?
) : OpenbisCreatable(), IOpenbisHierarchyObject {
    constructor(
        c: Experiment
    ) : this(c.code, mutableListOf(), c.type.code, c.samples.map { OpenbisObject(it) }, OpenbisPerson(c.getRegistrator()))


    override fun createOperation(connection: IApplicationServerApi, token: String) {
        val ec = ExperimentCreation().apply {
            this.code = code
        }
    }

    override fun getFromOpenBIS(connection: IApplicationServerApi, token: String): IPermIdHolder? {
        TODO("Not yet implemented")
    }


}
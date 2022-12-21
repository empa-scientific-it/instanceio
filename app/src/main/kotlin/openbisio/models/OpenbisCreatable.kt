package openbisio.models

import ch.ethz.sis.openbis.generic.asapi.v3.IApplicationServerApi
import ch.ethz.sis.openbis.generic.asapi.v3.dto.common.interfaces.IPermIdHolder


interface IOpenbisEntity {
    val code: String
    val registrator: OpenbisPerson?
    fun getFromOpenBIS(connection: IApplicationServerApi, token: String): IPermIdHolder?
    fun createOperation(connection: IApplicationServerApi, token: String)
    fun exists(connection: IApplicationServerApi, token: String): Boolean
    fun create(connection: IApplicationServerApi, token: String)
}

interface IOpenbisHierarchyObject: IOpenbisEntity {
    val ancestorsCodes: MutableList<String>?
    val children: List<IOpenbisHierarchyObject>?
}




abstract class OpenbisCreatable : IOpenbisEntity {
    abstract override val code: String
    abstract override val registrator: OpenbisPerson?

    override fun exists(connection: IApplicationServerApi, token: String): Boolean {
        return getFromOpenBIS(connection, token) != null
    }

    override fun create(connection: IApplicationServerApi, token: String)  {
        if (!exists(connection, token))  createOperation(connection, token)
    }

}


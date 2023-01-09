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

interface IOpenbisHierarchyObject : IOpenbisEntity {
    val ancestorsCodes: MutableList<String>?
    val children: List<IOpenbisHierarchyObject>?
    fun addAncestors()
}


abstract class OpenbisCreatable : IOpenbisEntity {
    abstract override val code: String
    abstract override val registrator: OpenbisPerson?
    override fun exists(connection: IApplicationServerApi, token: String): Boolean {
        return getFromOpenBIS(connection, token) != null
    }

    override fun create(connection: IApplicationServerApi, token: String) {
        if (!exists(connection, token)) createOperation(connection, token)
    }

}


abstract class OpenbisIdentifiedObject : OpenbisCreatable(), IOpenbisHierarchyObject {
    override fun addAncestors() {
        if (children == null) return else {
            children?.forEach { it ->
                it.ancestorsCodes?.let { it1 -> ancestorsCodes?.let { it2 -> it.ancestorsCodes?.addAll(it2) } };
                it.ancestorsCodes?.let { it2 -> it2.add(code) };
                it.addAncestors()
            }

        }


    }
}


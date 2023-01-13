package openbisio.models

import ch.ethz.sis.openbis.generic.asapi.v3.IApplicationServerApi
import ch.ethz.sis.openbis.generic.asapi.v3.dto.common.interfaces.IPermIdHolder
import kotlin.text.Regex
/**
 * Interface representing openBIS entities
 * that can be searched and created
 */
interface IOpenbisEntity {
    val code: String
    val registrator: OpenbisPerson?
    fun getFromOpenBIS(connection: IApplicationServerApi, token: String): IPermIdHolder?
    fun createOperation(connection: IApplicationServerApi, token: String)
    fun exists(connection: IApplicationServerApi, token: String): Boolean
    fun create(connection: IApplicationServerApi, token: String)
}


/**
 * Interface representing objects located
 * in the openBIS hierarchy. These objects *might* have children
 * and can have parents. The full path to the object is stored in ancestorCodes
 * @property ancestorsCodes a list of codes of the objects ancestors
 * @property children a (possibly empty) list of children of the object in the openBIS tree
 */
interface IOpenbisHierarchyObject : IOpenbisEntity {
    val ancestorsCodes: MutableList<String>?
    val children: List<IOpenbisHierarchyObject>?

    /**
     * When this method is called (after the openBIS tree is built), it will push the codes of the
     * ancestors in the lists. This way the object identifier is constructed
     */
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

private val pathPattern = "(?:(?:\/)([^\/]+))?"
class OpenbisIdentifier(val identifier: String) {
    init {
        val matcher = Regex(pathPattern)
        if(!matcher.matches(identifier)) throw IllegalArgumentException("Invalid identifier")
    }
}

abstract class OpenbisIdentifiedObject : OpenbisCreatable(), IOpenbisHierarchyObject {
    abstract override val children: List<OpenbisIdentifiedObject>?
    override fun addAncestors() {
        if (children == null) return else {
            children?.forEach { it ->
                it.ancestorsCodes?.let { it1 -> ancestorsCodes?.let { it2 -> it.ancestorsCodes?.addAll(it2) } };
                it.ancestorsCodes?.let { it2 -> it2.add(code) };
                it.addAncestors()
            }

        }

    }
    val identifier: String get() {return "/${ancestorsCodes!!.joinToString("/")}/${code}"}

    fun createHierarchy(connection: IApplicationServerApi, token: String) {
        println("creating ${identifier} with class ${this::class}")
        create(connection, token)
        children?.map { it.createHierarchy(connection, token) }

    }


}


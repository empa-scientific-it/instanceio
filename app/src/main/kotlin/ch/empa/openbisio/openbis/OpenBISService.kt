package ch.empa.openbisio.openbis

import ch.ethz.sis.openbis.generic.asapi.v3.IApplicationServerApi
import ch.systemsx.cisd.common.spring.HttpInvokerUtils
import java.net.URL

class OpenBISService(val path: URL) {

    val con =
        HttpInvokerUtils.createServiceStub(
            IApplicationServerApi::class.java,
            path.toString() + IApplicationServerApi.SERVICE_URL,
            10000
        )
    var token: String? = null
    fun connect(user: String, password: String) {
        val resp = con.login(user, password)
        token = resp
    }
}
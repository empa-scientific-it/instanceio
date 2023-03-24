package openbisio
import openbisio.OpenBISService
import java.net.URL
import kotlin.test.Test
import kotlin.test.assertContains


class OpenBISServiceTest {
    val serviceURL = "https://localhost:8443/openbis/openbis"
    val userName = "admin"
    val password = "changeit"
    val service = OpenBISService(URL(serviceURL)).apply { connect(userName, password) }
    val configFile = javaClass.getResource("/test.json").readText()
    @Test
    fun testConnect(){

        assert(service.token != null)
    }

    @Test
    fun testDeserialise(){
        val inst = ch.empa.openbisio.dumpInstance(service)
        assertContains( inst.children?.map{it.code}.orEmpty(), "ELN_SETTINGS")
    }

    @Test
    fun testCreation(){
        val instance = ch.empa.openbisio.readInstance(configFile)
        InstanceCreator(instance).create(service)
    }


}
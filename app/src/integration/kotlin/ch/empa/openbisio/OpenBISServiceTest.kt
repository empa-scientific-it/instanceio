package ch.empa.openbisio
import ch.empa.openbisio.instance.Instance
import ch.empa.openbisio.instance.InstanceCreator
import ch.empa.openbisio.space.Space
import ch.empa.openbisio.openbis.OpenBISService
import java.net.URL
import kotlin.test.Test
import kotlin.test.assertContains


class OpenBISServiceTest {
    private val serviceURL = "https://localhost:8443/openbis/openbis"
    private val userName = "admin"
    private val password = "changeit"
    private val service = OpenBISService(URL(serviceURL)).apply { connect(userName, password) }
    private val configFile = javaClass.getResource("/test.json").readText()
    @Test
    fun testConnect() = assert(service.token != null)

    @Test
    fun testDeserialize(){
        //val inst = dumpInstance(service)
        //assertContains(inst.children?.map{it.code}.orEmpty(), "ELN_SETTINGS")
    }

    @Test
    fun testSpaceCreation(){
        val instance = Instance(children = mutableListOf(Space("TEST")))
        InstanceCreator(instance).create(service)
        //val inst = dumpInstance(service)
        //assertContains(inst.children?.map{it.code}.orEmpty(), "TEST")
    }


}
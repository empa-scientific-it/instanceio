package ch.empa.openbisio

import ch.empa.openbisio.openbis.OpenBISService
import ch.ethz.sis.openbis.generic.asapi.v3.dto.service.CustomASServiceExecutionOptions
import ch.ethz.sis.openbis.generic.asapi.v3.dto.service.id.CustomASServiceCode
import java.net.URL
import kotlin.test.Test


class ExcelExportTest {
    private val serviceURL = "https://localhost:8443/openbis/openbis"
    private val userName = "admin"
    private val password = "changeit"
    private val service = OpenBISService(URL(serviceURL)).apply { connect(userName, password) }
    private val configFile = javaClass.getResource("/test.json").readText()

    @Test
    fun testConnect() = assert(service.token != null)

    @Test
    fun testExport() {
        val props = CustomASServiceExecutionOptions()
            .withParameter("method", "export")
            .withParameter("file_name", "metadata")
            .withParameter(
                "ids",
                arrayOf(
                    CustomASServiceExecutionOptions().withParameter("exportable_kind", "SPACE")
                        .withParameter("perm_id", "DEFAULT").withParameter("type_perm_id", "SPACE ").parameters
                )
            )
            .withParameter("export_properties", CustomASServiceExecutionOptions().parameters)
            .withParameter("export_referred_master_data", true)
            .withParameter("text_formatting", "PLAIN")
            .withParameter("compatible_with_import", true)
        println(props.parameters)
        val serviceResult = service.con.executeCustomASService(service.token, CustomASServiceCode("xls-export"), props)


    }


}
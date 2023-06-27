/*
 * Copyright 2023 Simone Baffelli
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package ch.empa.openbisio

import ch.ethz.sis.openbis.generic.asapi.v3.IApplicationServerApi
import ch.ethz.sis.openbis.generic.asapi.v3.dto.service.CustomASServiceExecutionOptions
import ch.ethz.sis.openbis.generic.asapi.v3.dto.service.id.CustomASServiceCode
import ch.systemsx.cisd.common.spring.HttpInvokerUtils
import java.util.List
import java.util.Map
import org.python.core.PyDictionary
import kotlin.test.Test


class TestExcelService {
    private  val serviceURL = "https://localhost:8443/openbis/openbis"
    private  val userName = "admin"
    private  val password = "changeit"
    @Test
    fun testExport() {
        val service = HttpInvokerUtils.createServiceStub(
            IApplicationServerApi::class.java,
            serviceURL + IApplicationServerApi.SERVICE_URL,
            1000
        )
        val token = service.login(userName, password)
        val props = CustomASServiceExecutionOptions()
        props
            .withParameter("method", "export")
            .withParameter("file_name", "metadata")
            .withParameter(
                "export_fields",
                Map.of("TYPE", Map.of("SPACE", List.of(Map.of("type", "perm_id", "id", "DEFAULT"))))
            )
            .withParameter("export_referred_master_data", true)
            .withParameter("text_formatting", "PLAIN")
            .withParameter("compatible_with_import", true)
        val serviceResult = service.executeCustomASService(token, CustomASServiceCode("xls-export"), props)
        //println(serviceResult)
    }
}

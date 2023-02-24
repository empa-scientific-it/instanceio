package openbisio.models

import ch.ethz.sis.openbis.generic.asapi.v3.dto.common.id.IObjectId
import openbisio.OpenBISService

interface IOperation {
    fun execute(con: OpenBISService)
    fun rollback(con: OpenBISService)
}
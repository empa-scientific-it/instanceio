package openbisio.models

import openbisio.OpenBISService

interface IOperation {
    fun execute(con: OpenBISService)
    fun rollback(con: OpenBISService)
}
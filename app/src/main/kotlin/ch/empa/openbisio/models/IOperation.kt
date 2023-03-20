package ch.empa.openbisio.models

import ch.empa.openbisio.OpenBISService

interface IOperation {
    fun execute(con: OpenBISService)
    fun rollback(con: OpenBISService)
}
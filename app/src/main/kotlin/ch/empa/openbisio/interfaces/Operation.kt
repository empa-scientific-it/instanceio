package ch.empa.openbisio.interfaces

import ch.empa.openbisio.openbis.OpenBISService

interface Operation {
    fun execute(con: OpenBISService)
    fun rollback(con: OpenBISService)
}
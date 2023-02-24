package openbisio.models

import openbisio.OpenBISService

//class CompositeCommand(val children: List<ICommand>) : ICommand {
//
//    override fun execute(con: OpenBISService) {
//        children?.map{it.execute(con)}
//    }
//
//    override fun rollback(con: OpenBISService) {
//        children?.map{it.rollback(con)}
//    }
//
//}
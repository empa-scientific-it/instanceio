package ch.empa.openbisio.instance

import ch.ethz.sis.openbis.generic.asapi.v3.dto.operation.SynchronousOperationExecutionOptions
import ch.empa.openbisio.openbis.OpenBISService

class InstanceCreator(val instance: Instance) {
    fun create(service: OpenBISService){
        val creations = instance.createOperation(service)
        if (creations.isNotEmpty()) {
            val res = service.con.executeOperations(service.token, creations.toList(), SynchronousOperationExecutionOptions()
                .apply { this.isExecuteInOrder = true })

        }

    }
}
package openbisio

import ch.ethz.sis.openbis.generic.asapi.v3.dto.operation.SynchronousOperationExecutionOptions
import openbisio.models.Instance

class InstanceCreator(val instance: Instance) {
    fun create(service: OpenBISService){
        val creations = instance.createOperation(service)
        println(creations.map { println(it) })

        val res = service.con.executeOperations(service.token, creations.toMutableList(), SynchronousOperationExecutionOptions().apply { this.isExecuteInOrder = true })
    }
}
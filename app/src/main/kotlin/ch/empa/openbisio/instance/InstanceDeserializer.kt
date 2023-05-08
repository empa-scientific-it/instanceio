package ch.empa.openbisio.instance

import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import java.io.File


fun readInstance(config: String): InstanceDTO {
    return Json.decodeFromString<InstanceDTO>(config)
}

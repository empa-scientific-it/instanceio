package ch.empa.openbisio.datatype

import ch.ethz.sis.openbis.generic.asapi.v3.dto.property.DataType
import kotlinx.serialization.Serializable

@JvmInline
@Serializable
value class DataType(private val t: DataType)
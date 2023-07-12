package ch.empa.openbisio.datasettype

import ch.empa.openbisio.interfaces.Identifier

@JvmInline value class DataSetTypeIdentifier(override val identifier: String): Identifier

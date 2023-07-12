package ch.empa.openbisio.person

import ch.empa.openbisio.interfaces.CreatableEntity
import ch.empa.openbisio.interfaces.Identifier

@JvmInline value class PersonIdentifier(override val identifier: String): Identifier

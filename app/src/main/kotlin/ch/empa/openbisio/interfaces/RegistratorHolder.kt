package ch.empa.openbisio.interfaces

import ch.empa.openbisio.person.Person
import kotlinx.serialization.Transient

interface RegistratorHolder {
    @Transient  val registrator: Person?

}
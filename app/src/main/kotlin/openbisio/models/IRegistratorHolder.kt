package openbisio.models

import kotlinx.serialization.Transient

interface IRegistratorHolder {
    @Transient  val registrator: OpenbisPerson?

}
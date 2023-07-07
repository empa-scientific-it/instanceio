package ch.empa.openbisio.person

import ch.empa.openbisio.interfaces.Entity
import ch.empa.openbisio.interfaces.Identifier
import ch.ethz.sis.openbis.generic.OpenBIS
import ch.ethz.sis.openbis.generic.asapi.v3.dto.common.create.ICreation
import ch.ethz.sis.openbis.generic.asapi.v3.dto.common.interfaces.IPermIdHolder
import ch.ethz.sis.openbis.generic.asapi.v3.dto.person.Person
import ch.ethz.sis.openbis.generic.asapi.v3.dto.person.create.PersonCreation
import ch.ethz.sis.openbis.generic.asapi.v3.dto.space.id.SpacePermId
import jakarta.mail.internet.InternetAddress
import kotlinx.serialization.Serializable

@Serializable
class PersonEntity(override val dto: PersonDTO, override val identifier: List<String>): Entity {
    override fun get(connection: OpenBIS): IPermIdHolder? {
        TODO("Not yet implemented")
    }

    override fun persist(): ICreation {
        val pc = PersonCreation().apply {
            this.userId = dto.code
            this.spaceId = SpacePermId(dto.space)
        }
        return pc
    }

    override fun openBISIdentifier(): Identifier {
        TODO("Not yet implemented")
    }

}
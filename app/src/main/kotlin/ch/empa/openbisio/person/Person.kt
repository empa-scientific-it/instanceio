package ch.empa.openbisio.person

import ch.ethz.sis.openbis.generic.asapi.v3.dto.person.Person
import jakarta.mail.internet.InternetAddress
import kotlinx.serialization.Serializable

@Serializable
data class Person(
    val userId: String,
    val firstName: String?,
    val lastName: String?,
    @Serializable(with = ch.empa.openbisio.InternetAddressAsStringSerializer::class) val email: InternetAddress?
) {
    constructor(
        userId: String,
        firstName: String?,
        lastName: String,
        email: String
    ) : this(userId, firstName, lastName, if (email != "") InternetAddress(email) else null)

    constructor(p: Person) : this(p.userId, p.firstName, p.lastName, p.email)
}
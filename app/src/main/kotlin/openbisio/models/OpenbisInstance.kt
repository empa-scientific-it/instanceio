package openbisio.models

import ch.ethz.sis.openbis.generic.asapi.v3.IApplicationServerApi
import ch.ethz.sis.openbis.generic.asapi.v3.dto.property.PropertyType
import ch.ethz.sis.openbis.generic.asapi.v3.dto.sample.SampleType
import kotlinx.serialization.Serializable

@Serializable
data class OpenbisInstance(
    var spaces: List<OpenbisSpace>?,
    var users: List<OpenbisPerson>?,
    var openbisPropertyTypes: List<OpenbisPropertyType>?,
    var objectTypes: List<OpenbisObjectType>?
) {
    constructor(
        sp: List<ch.ethz.sis.openbis.generic.asapi.v3.dto.space.Space>,
        pt: List<PropertyType>,
        st: List<SampleType>
    ) : this(sp.map { OpenbisSpace(it) }, null, pt.map { OpenbisPropertyType(it) }, st.map { OpenbisObjectType(it) })

    fun updateCodes(){
        spaces?.map{it.addAncestors()}
    }

    fun create(service: IApplicationServerApi, token: String){

        openbisPropertyTypes?.map { it.create(service, token) }
        objectTypes?.map{it.create(service, token)}
        spaces?.map {
            it.createHierarchy(service, token)
        }

    }
}
package openbisio.models

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
}
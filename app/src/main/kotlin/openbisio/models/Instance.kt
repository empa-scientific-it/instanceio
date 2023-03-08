/*
 * Copyright (c) 2023. Simone Baffelli
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package openbisio.models

import ch.ethz.sis.openbis.generic.asapi.v3.dto.common.interfaces.IPermIdHolder
import ch.ethz.sis.openbis.generic.asapi.v3.dto.common.operation.IOperation
import ch.ethz.sis.openbis.generic.asapi.v3.dto.property.PropertyType
import ch.ethz.sis.openbis.generic.asapi.v3.dto.sample.SampleType
import kotlinx.serialization.Contextual
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import openbisio.OpenBISService

@Serializable
data class Instance(
    override val code: String = "/",
    @Contextual override val ancestorCodes: ArrayDeque<String> = ArrayDeque(listOf<String>()),
    @SerialName("spaces") override var children: MutableList<out Space>?,
    var users: List<OpenbisPerson>? = null,
    @SerialName("property_types")  var openbisPropertyTypes: List<openbisio.models.PropertyType>? = null,
    @SerialName("object_types") var objectTypes: List<ObjectType>? = null
): IHierarchyComponent {
    constructor(
        sp: List<ch.ethz.sis.openbis.generic.asapi.v3.dto.space.Space>,
        pt: List<PropertyType>,
        st: List<SampleType>
    ) : this("", ArrayDeque(listOf<String>()) , sp.map { Space(it) }.toMutableList(), null, pt.map { PropertyType(it) }, st.map { ObjectType(it) })

    init {
        updateCodes()
    }



    override fun getFromAS(connection: OpenBISService): IPermIdHolder? {
        return null
    }

    override val identifier: HierarchyIdentifier
        get() = ConcreteIdentifier.InstanceIdentifier()

    override fun createOperation(connection: OpenBISService): List<IOperation> {
        val propertyCreations = openbisPropertyTypes?.flatMap { it.create(connection) }
        val typeCreation = objectTypes?.flatMap{it.create(connection)}
        val spaceCreations = children?.flatMap{it.createHierarchy(connection)}.orEmpty()
        println(spaceCreations)
        return (propertyCreations.orEmpty() + typeCreation.orEmpty() + spaceCreations)
    }



    override fun exists(connection: OpenBISService): Boolean {
        return false
    }
}
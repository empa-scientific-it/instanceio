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

package ch.empa.openbisio.instance

import ch.empa.openbisio.identifier.ConcreteIdentifier
import ch.empa.openbisio.interfaces.*
import ch.empa.openbisio.objectype.ObjectType
import ch.ethz.sis.openbis.generic.asapi.v3.dto.common.interfaces.IPermIdHolder
import ch.ethz.sis.openbis.generic.asapi.v3.dto.common.operation.IOperation
import ch.ethz.sis.openbis.generic.asapi.v3.dto.property.PropertyType
import ch.ethz.sis.openbis.generic.asapi.v3.dto.sample.SampleType
import kotlinx.serialization.Contextual
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ch.empa.openbisio.openbis.OpenBISService
import ch.empa.openbisio.person.Person
import ch.empa.openbisio.space.Space
import ch.ethz.sis.openbis.generic.asapi.v3.dto.vocabulary.Vocabulary

class Instance(
    override val code: String = "/",
    override val ancestorCodes: ArrayDeque<String> = ArrayDeque(listOf<String>()),
    override var children: MutableList<out Space>?,
    var users: List<Person>? = null,
    var openbisPropertyTypes: List<ch.empa.openbisio.propertytype.PropertyType>? = null,
    var objectTypes: List<ObjectType>? = null,
    var vocabularies: List<Vocabulary>? = null
): HierarchyComponent, DomainObject {
    constructor(
        sp: List<ch.ethz.sis.openbis.generic.asapi.v3.dto.space.Space>,
        pt: List<PropertyType>,
        st: List<SampleType>,
        cv: List<Vocabulary>
    ) : this("", ArrayDeque(listOf<String>()) , sp.map { Space(it) }.toMutableList(), null, pt.map {
        ch.empa.openbisio.propertytype.PropertyType(
            it
        )
    }, st.map { ObjectType(it) })

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
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

import ch.ethz.sis.openbis.generic.asapi.v3.IApplicationServerApi

abstract class IdentifiedObject : ICreatable, IHierarchyObject {
    abstract override val children: List<IdentifiedObject>?
    override fun addAncestors() {
        if (children == null) return else {
            children?.forEach { it ->
                it.ancestorsCodes?.let { it1 -> ancestorsCodes?.let { it2 -> it.ancestorsCodes?.addAll(it2) } };
                it.ancestorsCodes?.let { it2 -> it2.add(code) };
                it.addAncestors()
            }

        }

    }
    override val identifier: HierarchyIdentifier get() {return HierarchyIdentifier(ancestorsCodes.orEmpty() + code)}

    fun createHierarchy(connection: IApplicationServerApi, token: String) {
        println("creating ${identifier} with class ${this::class}")
        create(connection, token)
        children?.map { it.createHierarchy(connection, token) }

    }


}
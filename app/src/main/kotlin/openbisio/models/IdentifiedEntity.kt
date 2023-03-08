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

/**
* Abstract class implementing all the common methods for entities that possess
 * an identifier and belong to the openBIS hierarchy
**/
/*
abstract class IdentifiedEntity : ICreatable {
    open val ancestorCodes: MutableList<String>? = null
     val children: MutableList<IdentifiedEntity>? = null

    override fun updateCodes() {
        if (children == null) return else {
            children?.forEach { it ->
                it?.ancestorCodes?.add(it.code)
                it.updateCodes()
            }

        }

    }
    val identifier: HierarchyIdentifier get() {return HierarchyIdentifier(ancestorCodes.orEmpty() + code)}


}*/

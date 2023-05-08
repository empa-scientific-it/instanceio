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

package ch.empa.openbisio.interfaces

import kotlinx.serialization.Transient

/**
 * Interface representing objects located
 * in the openBIS hierarchy. These objects *might* have children
 * and can have parents. The full path to the object is stored in ancestorCodes
 * @property ancestorsCodes a list of codes of the objects ancestors
 * @property children a (possibly empty) list of children of the object in the openBIS tree
 */
interface HierarchyEntity : ICreatable {
    @Transient val ancestorsCodes: MutableList<String>?
    val children: List<HierarchyEntity>?
    val identifier: HierarchyIdentifier

    /**
     * When this method is called (after the openBIS tree is built), it will push the codes of the
     * ancestors in the lists. This way the object identifier is constructed
     */
    fun addAncestors()
}
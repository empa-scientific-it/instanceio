/*
 * Copyright 2023 Simone Baffelli
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package ch.empa.openbisio.identifier

import ch.empa.openbisio.interfaces.HierarchyIdentifier
import ch.empa.openbisio.interfaces.Identifier
import kotlinx.serialization.Serializable
import kotlin.collections.Collection


sealed class ConcreteIdentifier(members: Collection<String>,  maxSize: Int) : HierarchyIdentifier(members, maxSize) {
    class InstanceIdentifier : ConcreteIdentifier(members = listOf("/"), 1) {
        override fun getAncestor(): Identifier? {
            return null
        }

        override fun space(): Identifier? {
            return null
        }

        override fun project(): Identifier? {
            return null
        }

        override fun collection(): Identifier? {
            return null
        }

        override fun sample(): Identifier? {
            return null
        }

        override val identifier: String
            get() = "/"
    }

    class SpaceIdentifier(private val members: Collection<String>) : HierarchyIdentifier(members, 2) {
        override fun getAncestor(): InstanceIdentifier {
            return InstanceIdentifier()
        }

        override fun space(): Identifier? {
            return InstanceIdentifier()
        }

        override fun project(): Identifier? {
            return null
        }

        override fun collection(): Identifier? {
            return null
        }

        override fun sample(): Identifier? {
            return null
        }
    }

    class ProjectIdentifier(private val members: Collection<String>) : HierarchyIdentifier(members, 3) {
        override fun getAncestor(): SpaceIdentifier {
            return SpaceIdentifier(members.take(maxSize - 1))
        }

        override fun space(): Identifier? {
            return getAncestor()
        }

        override fun project(): Identifier? {
            return this
        }

        override fun collection(): Identifier? {
            return null
        }

        override fun sample(): Identifier? {
            return null
        }
    }

    class CollectionIdentifier(private val members: Collection<String>) : HierarchyIdentifier(members, 4) {
        override fun getAncestor(): ProjectIdentifier {
            return ProjectIdentifier(members.take(maxSize - 1))
        }

        override fun space(): Identifier? {
            return project()?.space()
        }

        override fun project(): Identifier? {
            return getAncestor()
        }

        override fun collection(): Identifier? {
            return this
        }

        override fun sample(): Identifier? {
            return null
        }
    }

    class SampleIdentifier(private val members: Collection<String>) : HierarchyIdentifier(members, 4) {
        override fun getAncestor(): Identifier? {
            return CollectionIdentifier(members.take(maxSize - 1))
        }

        override fun space(): Identifier? {
            return project()?.space()
        }

        override fun project(): Identifier? {
            return getAncestor()
        }

        override fun collection(): Identifier? {
            return getAncestor()
        }

        override fun sample(): Identifier? {
           return this
        }
    }

}
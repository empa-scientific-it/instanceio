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

import ch.empa.openbisio.interfaces.HierarchicalIdentifier

abstract class HierarchyIdentifier(val members: Collection<String>, val maxSize: Int) : HierarchicalIdentifier {
    init {
        if (members.size > maxSize) {
            throw IllegalArgumentException("This identifier ${this::class} can only have $maxSize components")
        }

    }

    private val components = members.map { it.replace("/", "") }

    override val identifier = components.joinToString(separator = "/", prefix = "", postfix = "")


    override fun getCode(): String {
        return members.last()
    }

}
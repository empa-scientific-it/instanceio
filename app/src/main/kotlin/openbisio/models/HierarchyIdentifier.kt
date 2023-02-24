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

import kotlin.collections.Collection

abstract class HierarchyIdentifier(private val members: Collection<String>, private val maxSize: Int) : IIdentifier {
    init {
        if(members.size != maxSize){ throw IllegalArgumentException("This identifier can only have ${maxSize} component")}

    }
    override val identifier = members.joinToString(separator = "/")


    override fun getCode(): String {
        return members.last()
    }

}
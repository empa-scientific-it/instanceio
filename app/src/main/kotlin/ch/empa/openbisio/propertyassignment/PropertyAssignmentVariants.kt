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

package ch.empa.openbisio.propertyassignment

import ch.empa.openbisio.propertytype.PropertyTypeDTO
import kotlinx.serialization.Serializable

@Serializable
sealed class PropertyAssignmentVariants {
    @Serializable
    data class NominalAssignment(val name: String) : PropertyAssignmentVariants()
    @Serializable
    data class LocalAssignment(val propertyType: PropertyTypeDTO) : PropertyAssignmentVariants()
}

//object PropertyAssignmentVariantsDeserializer : JsonContentPolymorphicSerializer<Payment>(Payment::class) {
//    override fun selectDeserializer(content: JsonElement) = when {
//        "reason" in content.jsonObject -> RefundedPayment.serializer()
//        else -> SuccessfulPayment.serializer()
//    }
//}
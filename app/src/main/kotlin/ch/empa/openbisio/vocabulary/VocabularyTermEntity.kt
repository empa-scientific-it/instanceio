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

package ch.empa.openbisio.vocabulary

import ch.empa.openbisio.interfaces.AssignmentEntity
import ch.empa.openbisio.interfaces.IdentifiedEntity
import ch.ethz.sis.openbis.generic.asapi.v3.dto.vocabulary.create.VocabularyTermCreation

data class VocabularyTermEntity(
    override val identifier: VocabularyTermIdentifier,
    val label: String,
    val official: Boolean,
    val description: String
) : IdentifiedEntity, AssignmentEntity {


    override fun persist(): VocabularyTermCreation {
        return VocabularyTermCreation().apply {
            this.code = this@VocabularyTermEntity.identifier.identifier
            this.label = this@VocabularyTermEntity.label
            this.isOfficial = this@VocabularyTermEntity.official
            this.description = this@VocabularyTermEntity.description
        }
    }


}
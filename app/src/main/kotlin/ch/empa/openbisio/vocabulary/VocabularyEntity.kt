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

import ch.empa.openbisio.interfaces.CreatableEntity
import ch.empa.openbisio.interfaces.IdentifiedEntity
import ch.ethz.sis.openbis.generic.OpenBIS
import ch.ethz.sis.openbis.generic.asapi.v3.dto.common.operation.IOperation
import ch.ethz.sis.openbis.generic.asapi.v3.dto.vocabulary.create.CreateVocabulariesOperation
import ch.ethz.sis.openbis.generic.asapi.v3.dto.vocabulary.create.VocabularyCreation
import ch.ethz.sis.openbis.generic.asapi.v3.dto.vocabulary.delete.DeleteVocabulariesOperation
import ch.ethz.sis.openbis.generic.asapi.v3.dto.vocabulary.delete.VocabularyDeletionOptions
import ch.ethz.sis.openbis.generic.asapi.v3.dto.vocabulary.fetchoptions.VocabularyFetchOptions
import ch.ethz.sis.openbis.generic.asapi.v3.dto.vocabulary.id.VocabularyPermId
import ch.ethz.sis.openbis.generic.asapi.v3.dto.vocabulary.search.VocabularySearchCriteria

data class VocabularyEntity(
    override val identifier: VocabularyIdentifier,
    val description: String,
    val terms: List<VocabularyTermEntity> = listOf()
) : CreatableEntity, IdentifiedEntity {

    override fun persist(): List<CreateVocabulariesOperation> {
        val vc = VocabularyCreation().apply {
            this.code = identifier.identifier
            this.description = this@VocabularyEntity.description
            this.terms = this@VocabularyEntity.terms.map { it.persist() }
        }
        return listOf(CreateVocabulariesOperation(listOf(vc)))
    }

    override fun exists(service: OpenBIS): Boolean {
        val sc = VocabularySearchCriteria().apply {
            this.withCode().thatEquals(identifier.identifier)
        }
        val res = service.searchVocabularies(sc, VocabularyFetchOptions())
        return res.totalCount > 0
    }

    override fun remove(): List<IOperation> {
        return listOf(
            DeleteVocabulariesOperation(
                listOf(VocabularyPermId(identifier.identifier)),
                VocabularyDeletionOptions()
            )
        )
    }


}
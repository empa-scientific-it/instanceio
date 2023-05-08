package ch.empa.openbisio.vocabulary

import ch.empa.openbisio.interfaces.ICreatable
import ch.empa.openbisio.openbis.OpenBISService
import ch.ethz.sis.openbis.generic.asapi.v3.dto.common.operation.IOperation
import ch.ethz.sis.openbis.generic.asapi.v3.dto.vocabulary.VocabularyTerm
import ch.ethz.sis.openbis.generic.asapi.v3.dto.vocabulary.create.CreateVocabulariesOperation
import ch.ethz.sis.openbis.generic.asapi.v3.dto.vocabulary.create.VocabularyCreation
import ch.ethz.sis.openbis.generic.asapi.v3.dto.vocabulary.fetchoptions.VocabularyTermFetchOptions
import ch.ethz.sis.openbis.generic.asapi.v3.dto.vocabulary.search.VocabularyTermSearchCriteria
import kotlinx.serialization.Serializable

@Serializable
data class VocabularyTerm(override val code: String, val description: String) : ICreatable {
    override fun createOperation(connection: OpenBISService): List<IOperation> {
        return listOf(CreateVocabulariesOperation(VocabularyCreation()))
    }

    override fun getFromAS(connection: OpenBISService): VocabularyTerm? {
        val so = VocabularyTermSearchCriteria().apply {
            withCode().thatEquals(code)
        }
        val fo = VocabularyTermFetchOptions()
        val res = connection.con.searchVocabularyTerms(connection.token, so, fo)
        return if (res.totalCount > 0) res.objects[0] else null
    }
}
package ch.empa.openbisio.vocabulary

import kotlinx.serialization.Serializable

@Serializable
data class Vocabulary(val vocabulary: List<VocabularyTerm>) {
}
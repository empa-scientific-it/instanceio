package openbisio.models

import kotlinx.serialization.Serializable

@Serializable
sealed class OpenbisPropertyValue {
    @Serializable
    class TextualValueOpenbis(val value: String) : OpenbisPropertyValue()

    @Serializable
    class NumericValueOpenbis(val value: Double) : OpenbisPropertyValue()

    @Serializable
    class BooleanValueOpenbis(val value: Boolean) : OpenbisPropertyValue()
}
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

package ch.empa.openbisio.datatype

import ch.ethz.sis.openbis.generic.asapi.v3.dto.property.DataType
import kotlinx.serialization.Serializable

@Serializable
enum class DataTypeDTO {
    BOOLEAN,
    INTEGER,
    REAL,
    DATE,
    TIMESTAMP,
    OBJECT,
    VARCHAR,
    MULTILINE_VARCHAR,
    HYPERLINK,
    XML,
    CONTROLLEDVOCABULARY;

    fun toOpenBISDataType(): DataType {
        val res = when (this) {
            BOOLEAN -> DataType.BOOLEAN
            INTEGER -> DataType.INTEGER
            REAL -> DataType.REAL
            DATE -> DataType.DATE
            TIMESTAMP -> DataType.TIMESTAMP
            OBJECT -> DataType.SAMPLE
            VARCHAR -> DataType.VARCHAR
            MULTILINE_VARCHAR -> DataType.MULTILINE_VARCHAR
            HYPERLINK -> DataType.HYPERLINK
            XML -> DataType.XML
            CONTROLLEDVOCABULARY -> DataType.CONTROLLEDVOCABULARY
        }
        return res
    }
}

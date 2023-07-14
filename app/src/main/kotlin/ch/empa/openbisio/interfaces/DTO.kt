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

package ch.empa.openbisio.interfaces

/**
 * Interface for DTOs. They are used as data exchange for serialising
 * and deserialising all sorts of openBIS data. The only method they should
 * support is toEntity(), which converts them to an entity, which in turn can be stored to openBIS.
 */
interface DTO {
    fun toEntity(): CreatableEntity
}
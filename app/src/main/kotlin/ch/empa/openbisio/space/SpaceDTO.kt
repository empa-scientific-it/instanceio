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

package ch.empa.openbisio.space

import ch.empa.openbisio.hierarchy.HierarchicalDTO
import ch.empa.openbisio.interfaces.CodeHolder
import ch.empa.openbisio.interfaces.Tree
import ch.empa.openbisio.project.ProjectDTO
import kotlinx.serialization.Serializable

@Serializable
data class SpaceDTO(
    override val code: String,
    val projects: List<ProjectDTO> = listOf(),
    val description: String = ""
) :
    HierarchicalDTO, Tree<HierarchicalDTO>, CodeHolder {


    override fun value(): HierarchicalDTO {
        return this
    }

    override fun hasChildren(): Boolean {
        return projects.isNotEmpty()
    }

    override fun children(): Collection<Tree<HierarchicalDTO>> {
        return projects
    }

    override fun updateCode(code: String): HierarchicalDTO {
        return this.copy(code = code)
    }


    fun withProject(projects: ProjectDTO): SpaceDTO {
        return this.copy(projects = this.projects + projects)
    }

    fun getProject(code: String): ProjectDTO {
        return projects.first { it.code == code }
    }
}

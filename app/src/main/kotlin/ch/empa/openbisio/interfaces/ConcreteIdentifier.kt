package ch.empa.openbisio.interfaces

import kotlinx.serialization.Serializable
import kotlin.collections.Collection

@Serializable
sealed class ConcreteIdentifier {
    class InstanceIdentifier : HierarchyIdentifier(members = listOf("/"), 1) {
        override fun getAncestor(): Identifier? {
            return null
        }

        override val identifier: String
            get() = "/"
    }

    class SpaceIdentifier(private val members: Collection<String>) : HierarchyIdentifier(members, 2) {
        override fun getAncestor(): InstanceIdentifier {
            return InstanceIdentifier()
        }
    }

    class ProjectIdentifier(private val members: Collection<String>) : HierarchyIdentifier(members, 3) {
        override fun getAncestor(): SpaceIdentifier {
            return SpaceIdentifier(members.take(maxSize - 1))
        }
    }

    class CollectionIdentifier(private val members: Collection<String>) : HierarchyIdentifier(members, 4) {
        override fun getAncestor(): ProjectIdentifier {
            return ProjectIdentifier(members.take(maxSize - 1))
        }
    }

    class SampleIdentifier(private val members: Collection<String>) : HierarchyIdentifier(members, 4) {
        override fun getAncestor(): ProjectIdentifier {
            return ProjectIdentifier(members.take(maxSize - 1))
        }
    }

}
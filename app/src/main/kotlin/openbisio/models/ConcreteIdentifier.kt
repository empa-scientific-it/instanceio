package openbisio.models

import kotlinx.serialization.Serializable
import kotlin.collections.Collection

@Serializable
sealed class ConcreteIdentifier(val members: Collection<String>) {
    class InstanceIdentifier(): HierarchyIdentifier(members = listOf(), 1){
        override fun getAncestor(): IIdentifier? {
            return null
        }
    }
    class SpaceIdentifier(val members: Collection<String>) : HierarchyIdentifier(members, 2) {
        override fun getAncestor(): InstanceIdentifier? {
            return InstanceIdentifier()
        }
    }

    class ProjectIdentifier(val members: Collection<String>) : HierarchyIdentifier(members, 3) {
        override fun getAncestor(): SpaceIdentifier? {
            return SpaceIdentifier(members)
        }
    }

    class CollectionIdentifier(val members: Collection<String>) : HierarchyIdentifier(members, 4) {
        override fun getAncestor(): ProjectIdentifier? {
            return ProjectIdentifier(members)
        }
    }

    class SampleIdentifier(val members: Collection<String>) : HierarchyIdentifier(members, 4) {
        override fun getAncestor(): ProjectIdentifier? {
            return ProjectIdentifier(members)
        }
    }

}
pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}
rootProject.name = "SavlukovApp"
include(":app")
include(":core:domain")
include(":core:data")
include(":core:ui")
include(":core:testing")
include(":features:home")
include(":features:catalog")
include(":features:product")
include(":features:stories")
include(":features:navigation")
include(":features:ar")
include(":features:wheel")

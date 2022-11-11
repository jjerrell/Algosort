pluginManagement {
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
    }
}
dependencyResolutionManagement {
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "Algosort"
include(":app")

// for stuff in dev or just something which shouldn't be included
val excludedProjects: List<String> = listOf("app", "buildSrc")

/**
 * Finds nested projects and returns a list of project-qualified names (i.e. 'feature:home'
 */
fun getProjects(): List<String> = fileTree(rootDir) {
    include("*/**/build.gradle*")
    exclude(excludedProjects)
}.map {
    // converts the project root relative path to a colon-separated path
    ":${relativePath(it.parent).replace(File.separatorChar, ':')}"
}

getProjects().forEach {
    include(it)
}

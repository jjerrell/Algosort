import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

buildscript {
    repositories {
        google()
        mavenCentral()
    }

    dependencies {
//        classpath(libs.android.gradlePlugin)
//        classpath(libs.android.gradlePlugin)
//        classpath(libs.kotlin.gradlePlugin)
//        classpath(libs.hiltAndroid.gradlePlugin)
//        classpath(libs.gradleMavenPublishPlugin)
//        classpath(libs.dokka)
//        classpath(libs.metalavaGradle)
//        classpath(libs.affectedmoduledetector)
    }
}
plugins {
    alias(libs.plugins.spotless)
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.hilt.android) apply false
}

val composeSnapshot = libs.versions.composesnapshot.getOrElse("-")

allprojects {
    repositories {
        google()
        mavenCentral()

        // TODO: Figure this out if snapshot is ever of interest
        if (composeSnapshot.length > 1) {
            maven(uri("https://androidx.dev/snapshots/builds/$composeSnapshot/artifacts/repository/"))
        }
    }
}

subprojects {
    apply { plugin("com.diffplug.spotless") }

    spotless {
        kotlin {
            target("**/*.kt")
            ktlint(libs.versions.ktlint.get())
            licenseHeader("/* (C) Jerrell, Jacob 2022 */")
//            licenseHeaderFile = rootProject.file("spotless/copyright.txt")
        }

        groovyGradle {
            target("**/*.gradle")
            greclipse().configFile(rootProject.file("spotless/greclipse.properties"))
            licenseHeader("/* (C) Jerrell, Jacob 2022 */", "(buildscript|apply|import|plugins)")
//            licenseHeaderFile = rootProject.file("spotless/copyright.txt")
        }
    }

    configurations.configureEach {
        resolutionStrategy.eachDependency {
            // Make sure that we're using the Android version of Guava
            if (requested.group == "com.google.guava"
                && requested.module.name == "guava"
                && requested.version.orEmpty().contains("jre")) {
                useVersion(requested.version.orEmpty().replace("jre", "android"))
            }
        }
    }

    tasks.withType<KotlinCompile>().configureEach {
        kotlinOptions {
            // Treat all Kotlin warnings as errors
            allWarningsAsErrors = true
            // Set JVM target to 1.8
            jvmTarget = "1.8"
            // Allow use of @OptIn
            freeCompilerArgs.plus("-opt-in=kotlin.RequiresOptIn")
            // Enable default methods in interfaces
            freeCompilerArgs.plus("-Xjvm-default=all")
        }
    }

    // Read in the signing.properties file if it is exists
//    def signingPropsFile = rootProject.file("release/signing.properties")
//    if (signingPropsFile.exists()) {
//        def localProperties = new Properties()
//        signingPropsFile.withInputStream { is -> localProperties.load(is) }
//        localProperties.each { prop ->
//            if (prop.key == "signing.secretKeyRingFile") {
//                // If this is the key ring, treat it as a relative path
//                project.ext.set(prop.key, rootProject.file(prop.value).absolutePath)
//            } else {
//                project.ext.set(prop.key, prop.value)
//            }
//        }
//    }

    // If we have a POM Artifact ID, we should generate API files
//    if (project.hasProperty("POM_ARTIFACT_ID")) {
//        apply plugin: "me.tylerbwong.gradle.metalava"
//
//        metalava {
//            filename = "api/current.api"
//            reportLintsAsErrors = true
//        }
//    }

    // Must be afterEvaluate or else com.vanniktech.maven.publish will overwrite our
    // dokka and version configuration.
//    afterEvaluate {
//        if (tasks.findByName("dokkaHtmlPartial") == null) {
//            // If dokka isn't enabled on this module, skip
//            return
//        }
//
//        tasks.named("dokkaHtmlPartial") {
//            dokkaSourceSets.configureEach {
//                reportUndocumented.set(true)
//                skipEmptyPackages.set(true)
//                skipDeprecated.set(true)
//                jdkVersion.set(8)
//
//                // Add Android SDK packages
//                noAndroidSdkLink.set(false)
//
//                // Add samples from :sample module
//                samples.from(rootProject.file("sample/src/main/java/"))
//
//                // AndroidX + Compose docs
//                externalDocumentationLink {
//                    url.set(new URL("https://developer.android.com/reference/"))
//                    packageListUrl.set(new URL("https://developer.android.com/reference/androidx/package-list"))
//                }
//                externalDocumentationLink {
//                    url.set(new URL("https://developer.android.com/reference/kotlin/"))
//                    packageListUrl.set(new URL("https://developer.android.com/reference/kotlin/androidx/package-list"))
//                }
//
//                sourceLink {
//                    localDirectory.set(project.file("src/main/java"))
//                    // URL showing where the source code can be accessed through the web browser
//                    remoteUrl.set(new URL("https://github.com/google/accompanist/blob/main/${project.name}/src/main/java"))
//                    // Suffix which is used to append the line number to the URL. Use #L for GitHub
//                    remoteLineSuffix.set("#L")
//                }
//            }
//        }
//    }

    afterEvaluate {
        val composeSnapshot = libs.versions.composesnapshot.get()
        if (composeSnapshot.length > 1) {
            // We're depending on a Jetpack Compose snapshot, update the library version name
            // to indicate it's from a Compose snapshot
            val versionName = this.project.properties["VERSION_NAME"].toString()
            if (versionName.contains("SNAPSHOT")) {
                version = versionName.replace("-SNAPSHOT", ".compose-${composeSnapshot}-SNAPSHOT")
            }
        }

        if (!(version.toString()).endsWith("SNAPSHOT")) {
            // If we're not a SNAPSHOT library version, we fail the build if we're relying on
            // any snapshot dependencies
            configurations.configureEach {
                dependencies.configureEach {
                    if (this !is ProjectDependency) {
                        val depVersion = this.version
                        if (depVersion != null && depVersion.endsWith("SNAPSHOT")) {
                            throw(IllegalArgumentException("Using SNAPSHOT dependency with non-SNAPSHOT library version: $this"))
                        }
                    }
                }
            }
        }
    }
}
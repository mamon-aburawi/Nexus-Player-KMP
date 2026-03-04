@file:OptIn(ExperimentalWasmDsl::class)
@file:Suppress("DEPRECATION")


import com.android.build.api.dsl.androidLibrary
import com.vanniktech.maven.publish.JavadocJar
import com.vanniktech.maven.publish.KotlinMultiplatform
import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.android.kotlin.multiplatform.library)
    alias(libs.plugins.vanniktech.mavenPublish)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.composeMultiplatform)
}


compose.resources {
    generateResClass = always
    publicResClass = false // Set to False for library-only access.
    packageOfResClass = "io.github.mamon.nexus.resources"
}


kotlin {
    jvm()
    @Suppress("UnstableApiUsage")
    androidLibrary {
        namespace = "io.github.mamon-aburawi"
        compileSdk = libs.versions.android.compileSdk.get().toInt()
        minSdk = libs.versions.android.minSdk.get().toInt()

        withJava() // enable java compilation support
        withHostTestBuilder {}.configure {}
        withDeviceTestBuilder {
            sourceSetTreeName = "test"
        }

        compilations.configureEach {
            compilerOptions.configure {
                jvmTarget.set(
                    JvmTarget.JVM_11
                )
            }
        }
    }
    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach {
        it.binaries.framework {
            baseName = "nexus"
            isStatic = true
        }
    }


    js(IR) {
        browser()
        binaries.executable()
    }


    wasmJs {
        browser()
        binaries.executable()
    }

    sourceSets {
        commonMain.dependencies {
            implementation(libs.compose.runtime)
            implementation(libs.compose.foundation)
            implementation(libs.compose.material3)
            implementation(libs.androidx.lifecycle.viewmodelCompose)
            implementation(libs.androidx.lifecycle.runtimeCompose)
            implementation(libs.compose.ui)
            implementation(libs.compose.uiToolingPreview)
            implementation(libs.compose.components.resources)



            // for media player
            implementation(libs.compose.mediaPlayer)


        }

        androidMain.dependencies {
            implementation(libs.androidx.activity.compose)
            implementation(libs.androidx.emoji2)
            implementation(libs.androidx.emoji2.views.helper)
            implementation(libs.androidcontextprovider) // for context access any were in android


            // tooling preview
            implementation(libs.compose.uiTooling)
            implementation(libs.compose.uiToolingPreview)
            implementation(libs.androidx.customview.poolingcontainer)

        }

        jvmMain.dependencies {

        }

        iosMain.dependencies {
        }

        webMain.dependencies {
            implementation(libs.kotlinx.browser)
            implementation(libs.compose.ui)

        }

    }



}


group = "io.github.mamon-aburawi" // this group name in maven central repository
version = "1.0.1" // version of library

mavenPublishing {

    configure(
        KotlinMultiplatform(
            javadocJar = JavadocJar.Empty(),
            sourcesJar = true,
            androidVariantsToPublish = listOf("release","debug"),
        )
    )


    coordinates(
        groupId = group.toString(),
        version = version.toString(),
        artifactId = "nexus-player-kmp"
    )

    pom {
        name = "Nexus Player KMP"
        description = "A high-performance, Kotlin Multiplatform (KMP) video player library designed for Compose Multiplatform. Providing a seamless media experience across Android, iOS, Desktop (JVM), and Web (Wasm), this library simplifies cross-platform video integration with a unified API."
        inceptionYear = "2026"
        url = "https://github.com/mamon-aburawi/Nexus-Player-KMP"
        licenses {
            license {
                name = "MIT License"
                url = "https://opensource.org/licenses/MIT"
            }
        }
        developers {
            developer {
                name = "Mamon Aburawi"
                email = "mamon.aburawi@gmail.com"
            }
        }
        scm {
            url = "https://github.com/mamon-aburawi/Nexus-Player-KMP"
        }
    }

    publishToMavenCentral()

    signAllPublications()
}

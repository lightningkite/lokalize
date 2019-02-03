import org.gradle.api.publish.maven.MavenPom
import org.gradle.api.publish.maven.internal.publication.DefaultMavenPublication
import org.gradle.internal.os.OperatingSystem
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.jetbrains.kotlin.gradle.dsl.KotlinTargetContainerWithPresetFunctions
import org.jetbrains.kotlin.gradle.plugin.KotlinSourceSet
import java.net.URI
import java.util.Properties
import kotlin.collections.ArrayList


/*
 * LIBRARY STUFF
*/

class Platform(
        val name: String,
        val publishName: String = name.toLowerCase(),
        val parent: Platform? = null,
        val children: ArrayList<Platform> = ArrayList(),
        val impliedDependencySetup: DependencyHandler.() -> Unit = {},
        val worksOnMyPlatform: () -> Boolean = { true },
        val configure: (KotlinTargetContainerWithPresetFunctions.() -> Unit)? = null
) {
    fun child(
            name: String,
            impliedDependencySetup: DependencyHandler.() -> Unit = {},
            worksOnMyPlatform: () -> Boolean = { true },
            configure: (KotlinTargetContainerWithPresetFunctions.() -> Unit)? = null
    ): Platform {
        val newPlat = Platform(
                name = name,
                parent = this,
                impliedDependencySetup = impliedDependencySetup,
                worksOnMyPlatform = { this.worksOnMyPlatform() && worksOnMyPlatform() },
                configure = configure
        )
        children.add(newPlat)
        return newPlat
    }

    companion object {
        val common = Platform("common", publishName = "metadata", impliedDependencySetup = {
            add("commonMainImplementation", "org.jetbrains.kotlin:kotlin-stdlib-common")
            add("commonTestImplementation", "org.jetbrains.kotlin:kotlin-test-annotations-common")
            add("commonTestImplementation", "org.jetbrains.kotlin:kotlin-test-common")
        })
        /**/val native = common.child("native")
        val posix = native.child("posix", worksOnMyPlatform = { OperatingSystem.current().isMacOsX || OperatingSystem.current().isLinux || OperatingSystem.current().isUnix })
        val apple = posix.child("apple", worksOnMyPlatform = { OperatingSystem.current().isMacOsX })
        val ios = apple.child("ios")
        val iosX64 = ios.child("iosX64") { iosX64() }
        val iosArm32 = ios.child("iosArm32") { iosArm32() }
        val iosArm64 = ios.child("iosArm64") { iosArm64() }
        val macosX64 = apple.child("macosX64") { macosX64() }
        val linux = posix.child("linux", worksOnMyPlatform = { OperatingSystem.current().isLinux })
        val linuxX64 = linux.child("linuxX64") { linuxX64() }
        val linuxArm32Hfp = linux.child("linuxArm32Hfp") { linuxArm32Hfp() }
        val linuxMips32 = linux.child("linuxMips32") { linuxMips32() }
        val linuxMipsel32 = linux.child("linuxMipsel32") { linuxMipsel32() }
        val androidNative = posix.child("androidNative", worksOnMyPlatform = { OperatingSystem.current().isLinux || OperatingSystem.current().isMacOsX })
        val androidNativeArm32 = androidNative.child("androidNativeArm32") { androidNativeArm32() }
        val androidNativeArm64 = androidNative.child("androidNativeArm64") { androidNativeArm64() }
        val mingwX64 = native.child("mingwX64", worksOnMyPlatform = { OperatingSystem.current().isWindows }) { mingwX64() }
        /**/val nonNative = common.child("nonNative")
        val jvm = nonNative.child("jvm", impliedDependencySetup = {
            add("jvmMainImplementation", "org.jetbrains.kotlin:kotlin-stdlib-jdk8")
            add("jvmTestImplementation", "org.jetbrains.kotlin:kotlin-test")
            add("jvmTestImplementation", "org.jetbrains.kotlin:kotlin-test-junit")
        }) { jvm() }
        val jvmDesktop = jvm.child("jvmDesktop") { jvm("jvmDesktop") }
        val android = jvm.child("android") { android() }
        val js = nonNative.child("js", impliedDependencySetup = {
            add("jsMainImplementation", "org.jetbrains.kotlin:kotlin-stdlib-js")
            add("jsTestImplementation", "org.jetbrains.kotlin:kotlin-test-js")
        }) {
            js {
                compilations.all {
                    kotlinOptions {
                        languageVersion = "1.3"
                        sourceMap = true
                        metaInfo = true
                        moduleKind = "umd"
                    }
                }
            }
        }
    }
}

class DependencyAddition(val dependencies: DependencyHandler, val type: String, val group: String = "", val artifactStart: String = "", val version: String) {
    fun Platform.special(dependencyString: String) {
        if (!this.worksOnMyPlatform()) return
        dependencies.add("${this.name}$type", dependencyString)
    }

    fun Platform.expected() {
        if (!this.worksOnMyPlatform()) return
        dependencies.add("${this.name}$type", "$group:$artifactStart-${this.publishName}:$version")
    }

    fun Platform.expectedWithChildren() {
        if (!this.worksOnMyPlatform()) return
        if (this.configure != null) expected()
        else {
            children.forEach {
                it.expectedWithChildren()
            }
            if (this == Platform.common) {
                expected()
            }
        }
    }
}

fun DependencyHandler.mainImplementation(group: String, artifactStart: String, version: String, action: DependencyAddition.() -> Unit) = DependencyAddition(this, "MainImplementation", group, artifactStart, version).apply(action)
fun DependencyHandler.testImplementation(group: String, artifactStart: String, version: String, action: DependencyAddition.() -> Unit) = DependencyAddition(this, "TestImplementation", group, artifactStart, version).apply(action)
fun DependencyHandler.mainApi(group: String, artifactStart: String, version: String, action: DependencyAddition.() -> Unit) = DependencyAddition(this, "MainApi", group, artifactStart, version).apply(action)
fun DependencyHandler.testApi(group: String, artifactStart: String, version: String, action: DependencyAddition.() -> Unit) = DependencyAddition(this, "TestApi", group, artifactStart, version).apply(action)

fun KotlinMultiplatformExtension.releaseFor(vararg platforms: Platform) = platforms.forEach { releaseFor(it) }
fun KotlinMultiplatformExtension.releaseFor(platform: Platform) {
    if (platform.configure != null) {
        platform.configure.invoke(this)
        setupSourceSets(platform)
    } else platform.children.forEach {
        releaseFor(it)
    }
    platform.impliedDependencySetup.invoke(dependencies)
}

fun KotlinMultiplatformExtension.setupSourceSets(platform: Platform) {
    var childMain: KotlinSourceSet? = null
    var childTest: KotlinSourceSet? = null
    generateSequence(platform) { it.parent }.forEach {
        childMain = sourceSets.maybeCreate("${it.name}Main").also {
            childMain?.dependsOn(it)
        }
        childTest = sourceSets.maybeCreate("${it.name}Test").also {
            childTest?.dependsOn(it)
        }
    }
}

fun PublishingExtension.appendToPoms(action: MavenPom.() -> Unit) {
    publications.asSequence()
            .mapNotNull { it as? MavenPublication }
            .map { it.pom }
            .forEach { with(it, action) }
}

fun MavenPom.github(owner: String, repositoryName: String) {
    url.set("https://github.com/$owner/$repositoryName")
    scm {
        url.set("https://github.com/$owner/$repositoryName.git")
    }
    issueManagement {
        system.set("GitHub")
        url.set("https://github.com/$owner/$repositoryName/issues")
    }
}

fun MavenPom.licenseMIT() {
    licenses {
        license {
            name.set("MIT License")
            url.set("http://www.opensource.org/licenses/mit-license.php")
            distribution.set("repo")
        }
    }
}

fun RepositoryHandler.bintray(
        organization: String,
        repository: String
): MavenArtifactRepository? {
    val locals = Properties().apply {
        generateSequence(project.rootDir) { it.parentFile }
                .take(3)
                .toList()
                .asReversed()
                .asSequence()
                .mapNotNull { File(it, "local.properties").takeIf { it.exists() } }
                .forEach { load(it.inputStream()) }
    }
    val username = locals.getProperty("bintrayUser") ?: System.getenv("BINTRAY_USER") ?: return null
    val key = locals.getProperty("bintrayKey") ?: locals.getProperty("bintrayApiKey")
    ?: System.getenv("BINTRAY_API_KEY") ?: return null
    return maven {
        name = "Bintray"
        credentials {
            this.username = username
            this.password = key
        }
        url = URI("https://api.bintray.com/maven/$organization/$repository/${project.name}")
    }
}

afterEvaluate {
    publishing.publications.asSequence()
//            .filter { it.name == "metadata" }
            .mapNotNull { it as? DefaultMavenPublication }
            .forEach {
                it.setModuleDescriptorGenerator(null)
            }
}


/*
 * PROJECT STUFF
*/

plugins {
    kotlin("multiplatform") version "1.3.20"
    `maven-publish`
}

repositories {
    mavenLocal()
    mavenCentral()
    maven("https://dl.bintray.com/lightningkite/com.lightningkite.krosslin")
}


val versions = Properties().apply {
    load(project.file("versions.properties").inputStream())
}

group = "com.lightningkite"
version = versions.getProperty(project.name)

kotlin {
    releaseFor(Platform.common)
}

dependencies {
    mainImplementation("com.lightningkite", "kommon", versions.getProperty("kommon")) {
        Platform.common.expectedWithChildren()
    }
}

publishing {
    repositories {
        bintray(
                organization = "lightningkite",
                repository = "com.lightningkite.krosslin"
        )
    }

    this.appendToPoms {
        github("lightningkite", project.name)
        licenseMIT()
        developers {
            developer {
                id.set("UnknownJoe796")
                name.set("Joseph Ivie")
                email.set("joseph@lightningkite.com")
                timezone.set("America/Denver")
                roles.set(listOf("architect", "developer"))
                organization.set("Lightning Kite")
                organizationUrl.set("http://www.lightningkite.com")
            }
        }
    }
}
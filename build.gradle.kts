import com.lightningkite.konvenience.gradle.*
import java.util.Properties

plugins {
    kotlin("multiplatform") version "1.3.21"
    `maven-publish`
}

buildscript {
    repositories {
        mavenLocal()
        maven("https://dl.bintray.com/lightningkite/com.lightningkite.krosslin")
    }
    dependencies {
        classpath("com.lightningkite:konvenience:+")
    }
}
apply(plugin = "com.lightningkite.konvenience")

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

project.doNotPublishMetadata()
kotlin {

    sources(tryTargets = KTarget.allExceptAndroid - KTarget.wasm32) {
        main {
            dependency(standardLibrary)
            dependency(projectOrMavenDashPlatform("com.lightningkite", "kommon", versions.getProperty("kommon")))
        }
        test {
            dependency(testing)
            dependency(testingAnnotations)
        }
        isIos.sources {}
        isJs.sources {}
        isJvm.sources {}
        isMingwX64.sources {}
        (isPosix and !isIos).sources("posix") {}
    }
}

publishing {
    repositories {
        bintray(
                project = project,
                organization = "lightningkite",
                repository = "com.lightningkite.krosslin"
        )
    }

    appendToPoms {
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


/*import com.lightningkite.konvenience.gradle.*
import java.util.Properties

plugins {
    kotlin("multiplatform") version "1.3.21"
    `maven-publish`
    id("com.lightningkite.konvenience") version "+"
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
    sources(KTarget.common) {
        main {
            mpp(standardLibrary)
            mpp(projectOrMavenDashPlatform("com.ivieleague", "kommon", versions.getProperty("kommon")))
        }
        test {
            mpp(testing)
            mpp(testingAnnotations)
        }
        KTarget.ios.sources{}
        KTarget.js.sources{}
        KTarget.jvm.sources{}
        KTarget.mingwX64.sources {}
        KTarget.posix.without(KTarget.ios).sources {}
    }
}

publishing {
    repositories {
        bintray(
                project = project,
                organization = "lightningkite",
                repository = "com.lightningkite.krosslin"
        )
    }

    appendToPoms {
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


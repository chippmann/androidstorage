include(
    ":androidstorage-api",
    ":androidstorage-common",
    ":androidstorage-dagger-di"
)
rootProject.name = "AndroidStorage"

gradle.allprojects {
    buildscript {
        repositories {
            mavenLocal()
            mavenCentral()
            jcenter()
            google()
        }
    }
    repositories {
        mavenLocal()
        mavenCentral()
        jcenter()
        google()
    }
}
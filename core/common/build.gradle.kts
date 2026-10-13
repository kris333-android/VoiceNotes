plugins {
    alias(libs.plugins.kotlin.jvm)
}

kotlin {
    jvmToolchain(17)
}

dependencies {
    implementation("javax.inject:javax.inject:1")
    testImplementation(libs.junit)
}

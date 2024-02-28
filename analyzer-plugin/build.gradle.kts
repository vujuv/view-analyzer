plugins {
    `kotlin-dsl`
    `java-gradle-plugin`
    `maven-publish`
}

dependencies {
    implementation("com.android.tools.build:gradle:8.1.3")
    compileOnly("org.ow2.asm:asm-commons:9.2")
}

gradlePlugin {
    plugins {
        create("viewAnalyzerPlugin") {
            id = "com.vuj.view-analyzer-plugin"
            implementationClass = "com.vuj.analyzer.plugin.ViewAnalyzerPlugin"
        }
    }
}

group = "com.vuj.view-analyzer-plugin"
version = "1.0.0"

publishing {
    publications {
        create<MavenPublication>("maven") {
            from(components["java"])
        }
    }

    repositories {
        maven {
            url = uri("../repo/view-analyzer-plugin")
        }
    }
}
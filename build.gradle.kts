import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm")
    id("org.jetbrains.compose")
    antlr
}

group = "fun.vari"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    maven ("https://maven.aliyun.com/nexus/content/groups/public/")
    google()
}

dependencies {
    // Note, if you develop a library, you should use compose.desktop.common.
    // compose.desktop.currentOs should be used in launcher-sourceSet
    // (in a separate module for demo project and in testMain).
    // With compose.desktop.common you will also lose @Preview functionality
    implementation(compose.desktop.currentOs)
    testImplementation(kotlin("test"))

    implementation("org.scilab.forge:jlatexmath:1.0.7")
    //implementation(kotlin("reflect"))

    val voyagerVersion = "1.0.0-rc07"
    implementation("cafe.adriel.voyager:voyager-navigator:$voyagerVersion")
    implementation("cafe.adriel.voyager:voyager-bottom-sheet-navigator:$voyagerVersion")
    implementation("cafe.adriel.voyager:voyager-tab-navigator:$voyagerVersion")
    implementation("cafe.adriel.voyager:voyager-transitions:$voyagerVersion")
    antlr("org.antlr:antlr4:4.13.1")

}



tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "17"
    dependsOn(tasks.generateGrammarSource)
}

tasks.generateGrammarSource {
    arguments = arguments  +
            listOf("-visitor", "-long-messages") +
            listOf( "-package", "fun.vari.tigrazul.tree")
    outputDirectory =  File("build/generated-src/antlr/main/fun/vari/tigrazul/tree")
}



compose.desktop {
    application {
        mainClass = "fun.vari.tigrazul.MainKt"
        javaHome = System.getenv("JAVA_HOME")
        nativeDistributions {
            //includeAllModules = true
            modules("java.instrument", "jdk.unsupported")
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi,TargetFormat.Exe, TargetFormat.Deb)
            packageName = "Tigrazul"
            packageVersion = "1.0.0"
            description = "Tigrazul"
            copyright = "© 2024 XiLaiTL. All rights reserved."
            vendor = "BIT"
            //licenseFile.set(project.file("LICENSE.txt"))
            windows{
                iconFile.set(project.file("src/main/resources/icon/icon.ico"))
                menuGroup = "Tigrazul"
            }


        }
    }
}



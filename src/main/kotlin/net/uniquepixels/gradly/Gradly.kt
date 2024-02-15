package net.uniquepixels.gradly

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.provider.Property
import java.io.File

interface GradlyConfig {
    val projectId: Property<String>
}

class Gradly : Plugin<Project> {

    override fun apply(target: Project) {

        target.plugins.apply(Gradly::class.java)

        val extension = target.extensions.create("projectId", GradlyConfig::class.java)

        val resourcesDir = target.path.plus("src/main/resources")

        val defaultLang = File(resourcesDir, "translations.properties")
        val networkLang = File(resourcesDir, "translations_de.properties")


        target.tasks.create("generateTranslations") {

            if (defaultLang.exists().not())
                defaultLang.createNewFile()

            if (networkLang.exists().not())
                networkLang.createNewFile()

            println("Translations successfully created!")

        }

        target.tasks.create("updateTranslations") {

            val projectId = extension.projectId.get()

            println("Translations successfully updated!")

        }

    }
}
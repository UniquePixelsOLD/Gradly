package net.uniquepixels.gradly

import com.google.gson.Gson
import okhttp3.OkHttpClient
import okhttp3.Request
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.provider.Property
import java.io.File
import java.io.FileOutputStream
import java.io.ObjectOutputStream
import java.util.*


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
            makeResponse(projectId, target)
            println("Translations successfully updated!")

        }

    }

    private fun makeResponse(projectId: String, target: Project) {

        val client = OkHttpClient()

        val request = Request.Builder()
            .url("http://api.uniquepixels.net/translations/all")
            .addHeader("projectId", projectId)
            .addHeader("Authorization", "Bearer " + System.getenv("AUTH_TOKEN"))
            .build()

        val response = client.newCall(request).execute()

        val rawJson = response.body!!.string()

        val projectTranslations = Gson().fromJson(rawJson, List::class.java)

        val resourcesDir = target.path.plus("src/main/resources")

        projectTranslations.map {
            it as ProjectTranslation
        }.forEach {
            if (it.locale == Locale.ENGLISH) {
                val translationFile = File(resourcesDir, "translations.properties")

                this.loadDataInFile(it.fileContent, translationFile)
            }
        }

    }

    private fun loadDataInFile(map: HashMap<String, String>, file: File) {
        val oos = ObjectOutputStream(FileOutputStream(file))
        oos.writeObject(map)
        oos.close()
    }
}
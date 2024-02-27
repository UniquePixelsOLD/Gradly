package net.uniquepixels.gradly

import java.util.Locale

data class ProjectTranslation(
    val projectId: String, val translator: List<String>, val fileContent: HashMap<String, String>, val locale: Locale
)

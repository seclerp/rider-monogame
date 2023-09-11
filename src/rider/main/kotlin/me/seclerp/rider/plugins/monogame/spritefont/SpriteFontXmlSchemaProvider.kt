package me.seclerp.rider.plugins.monogame.spritefont

import com.intellij.javaee.ExternalResourceManagerEx
import com.intellij.openapi.module.Module
import com.intellij.openapi.util.Key
import com.intellij.openapi.util.ModificationTracker
import com.intellij.openapi.vfs.VfsUtil
import com.intellij.psi.PsiFile
import com.intellij.psi.PsiManager
import com.intellij.psi.util.CachedValue
import com.intellij.psi.util.CachedValueProvider
import com.intellij.psi.util.CachedValuesManager
import com.intellij.psi.xml.XmlFile
import com.intellij.xml.XmlSchemaProvider
import com.jetbrains.rd.platform.util.getLogger
import com.jetbrains.rd.platform.util.getOrCreateUserData

class SpriteFontXmlSchemaProvider : XmlSchemaProvider() {
    companion object {
        private val logger = getLogger<SpriteFontXmlSchemaProvider>()
        const val SCHEMA_LOCATION = "/xmlSchema/SpriteFont.xsd"
        const val FILE_EXTENSION = "spritefont"
    }

    private val schemaFileMapKey: Key<MutableMap<String, CachedValue<XmlFile>>> = Key.create(
        "RIDER_SCHEMAS_FILE_MAP_KEY:" + javaClass.name)
    private var schemas: MutableMap<String, CachedValue<XmlFile>>? = null

    override fun getSchema(url: String, module: Module?, baseFile: PsiFile): XmlFile? {
        logger.info("map $schemaFileMapKey")
        val location = SCHEMA_LOCATION
        val project = baseFile.project
        logger.info("Project: ${project.name}")

        schemas = schemas ?: project.getOrCreateUserData(schemaFileMapKey) { mutableMapOf() }

        val cachedValue = schemas?.get(location)
        if (cachedValue != null) {
            logger.info("Returning cached schema for location: $location")
            return cachedValue.value
        }

        val schema = CachedValuesManager.getManager(project).createCachedValue(object : CachedValueProvider<XmlFile> {
            override fun compute(): CachedValueProvider.Result<XmlFile> {
                logger.info("Computing value for cached schema")
                val resource = javaClass.getResource(location)!!
                val fileByURL = VfsUtil.findFileByURL(resource)
                if (fileByURL == null) {
                    logger.error("xsd file '$location' not found")
                    return CachedValueProvider.Result(null, ModificationTracker.EVER_CHANGED)
                }

                val psiFile = PsiManager.getInstance(project).findFile(fileByURL)
                if (psiFile !is XmlFile){
                    logger.warn("PSI file is not XML file")
                    return CachedValueProvider.Result(null, ModificationTracker.EVER_CHANGED)
                }

                logger.info("PSI file null: ${false}")
                val manager = ExternalResourceManagerEx.getInstanceEx()
                val externalResourcesTracker = ModificationTracker { manager.getModificationCount(project) }
                return CachedValueProvider.Result.create(psiFile, psiFile, externalResourcesTracker)
            }
        }, false)

        logger.info("Added schema to cache for location: $location")
        schemas?.put(location, schema)
        return schema.value
    }

    override fun isAvailable(file: XmlFile): Boolean {
        return file.originalFile.virtualFile?.extension.equals(FILE_EXTENSION, true)
    }
}
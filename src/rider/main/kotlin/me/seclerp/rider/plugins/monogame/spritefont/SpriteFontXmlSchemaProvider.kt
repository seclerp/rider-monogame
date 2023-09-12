package me.seclerp.rider.plugins.monogame.spritefont

import com.intellij.javaee.ExternalResourceManagerEx
import com.intellij.openapi.module.Module
import com.intellij.openapi.util.ModificationTracker
import com.intellij.openapi.vfs.VfsUtil
import com.intellij.psi.PsiFile
import com.intellij.psi.PsiManager
import com.intellij.psi.util.CachedValue
import com.intellij.psi.util.CachedValueProvider
import com.intellij.psi.util.CachedValuesManager
import com.intellij.psi.xml.XmlFile
import com.intellij.xml.XmlSchemaProvider
import com.intellij.openapi.diagnostic.logger

class SpriteFontXmlSchemaProvider : XmlSchemaProvider() {
    companion object {
        private val logger = logger<SpriteFontXmlSchemaProvider>()
        const val SCHEMA_LOCATION = "/xmlSchema/SpriteFont.xsd"
        const val FILE_EXTENSION = "spritefont"
    }

    private var schema: CachedValue<XmlFile>? = null

    override fun getSchema(url: String, module: Module?, baseFile: PsiFile): XmlFile? {
        logger.info("Schema location: $SCHEMA_LOCATION")
        val project = baseFile.project
        logger.info("Project: ${project.name}")

        if (schema != null) {
            logger.info("Returning cached schema for location: $SCHEMA_LOCATION")
            return schema?.value
        }

        schema = CachedValuesManager.getManager(project).createCachedValue(object : CachedValueProvider<XmlFile> {
            override fun compute(): CachedValueProvider.Result<XmlFile> {
                logger.info("Computing value for cached schema")
                val resource = javaClass.getResource(SCHEMA_LOCATION)!!
                val fileByURL = VfsUtil.findFileByURL(resource)
                if (fileByURL == null) {
                    logger.error("xsd file '$SCHEMA_LOCATION' not found")
                    return CachedValueProvider.Result(null, ModificationTracker.EVER_CHANGED)
                }

                val psiFile = PsiManager.getInstance(project).findFile(fileByURL)
                if (psiFile !is XmlFile) {
                    logger.warn("PSI file is not XML file")
                    return CachedValueProvider.Result(null, ModificationTracker.EVER_CHANGED)
                }

                logger.info("PSI file null: ${false}")
                val manager = ExternalResourceManagerEx.getInstanceEx()
                val externalResourcesTracker = ModificationTracker { manager.getModificationCount(project) }
                return CachedValueProvider.Result.create(psiFile, psiFile, externalResourcesTracker)
            }
        }, false)

        logger.info("Added schema to cache for location: $SCHEMA_LOCATION")
        return schema?.value
    }

    override fun isAvailable(file: XmlFile): Boolean {
        return file.originalFile.virtualFile?.extension.equals(FILE_EXTENSION, true)
    }
}
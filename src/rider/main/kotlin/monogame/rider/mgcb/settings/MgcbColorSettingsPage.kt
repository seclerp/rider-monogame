package monogame.rider.mgcb.settings

import com.intellij.openapi.editor.colors.TextAttributesKey
import com.intellij.openapi.fileTypes.SyntaxHighlighter
import com.intellij.openapi.options.colors.AttributesDescriptor
import com.intellij.openapi.options.colors.ColorDescriptor
import com.intellij.openapi.options.colors.ColorSettingsPage
import monogame.rider.mgcb.MgcbSyntaxHighlighter
import javax.swing.Icon

class MgcbColorSettingsPage : ColorSettingsPage {
    override fun getAttributeDescriptors(): Array<AttributesDescriptor> = DESCRIPTORS

    override fun getColorDescriptors(): Array<ColorDescriptor> = ColorDescriptor.EMPTY_ARRAY

    override fun getDisplayName(): String = "MGCB"

    override fun getIcon(): Icon? = null

    override fun getHighlighter(): SyntaxHighlighter = MgcbSyntaxHighlighter()

    override fun getDemoText(): String = """# Directories
/outputDir:bin/foo
/intermediateDir:obj/foo

/rebuild

# Build a texture
/importer:TextureImporter
/processor:TextureProcessor
/processorParam:ColorKeyEnabled=false
/build:Textures\wood.png
/build:Textures\metal.png
/build:Textures\plastic.png"""

    override fun getAdditionalHighlightingTagToDescriptorMap(): MutableMap<String, TextAttributesKey>? = null

    companion object {
        private val DESCRIPTORS = arrayOf(
            AttributesDescriptor("Key", MgcbSyntaxHighlighter.KEY),
            AttributesDescriptor("Separator", MgcbSyntaxHighlighter.SEPARATOR),
            AttributesDescriptor("Value", MgcbSyntaxHighlighter.VALUE),
            AttributesDescriptor("Bad value", MgcbSyntaxHighlighter.BAD_CHARACTER)
        )
    }
}
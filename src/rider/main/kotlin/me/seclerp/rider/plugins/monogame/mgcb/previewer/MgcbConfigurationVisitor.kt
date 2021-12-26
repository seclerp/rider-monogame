package me.seclerp.rider.plugins.monogame.mgcb.previewer

import me.seclerp.rider.plugins.monogame.mgcb.psi.MgcbOption
import me.seclerp.rider.plugins.monogame.mgcb.psi.MgcbVisitor
import me.seclerp.rider.plugins.monogame.mgcb.psi.getKey
import me.seclerp.rider.plugins.monogame.mgcb.psi.getValue

class MgcbConfigurationVisitor : MgcbVisitor() {
    val configuration = MgcbConfiguration()
    val currentProcessorParams = mutableMapOf<String, String>()

    var currentBuildEntry = BuildEntry()

    override fun visitOption(option: MgcbOption) {
        super.visitOption(option)
        val optionValue = option.getValue()

        when (option.getKey()) {
            "outputDir" -> configuration.outputDir = optionValue
            "intermediateDir" -> configuration.intermediateDir = optionValue
            "rebuild" -> configuration.rebuild = true
            "clean" -> configuration.clean = true
            "incremental" -> configuration.incremental = true
            "reference" -> configuration.references.add(optionValue!!)
            "platform" -> configuration.platform = optionValue
            "profile" -> configuration.profile = optionValue
            "config" -> configuration.config = optionValue
            "compress" -> configuration.compress = true

            "importer" -> currentBuildEntry.importer = optionValue
            "processor" -> {
                currentProcessorParams.clear()
                currentBuildEntry.processor = optionValue
            }
            "processorParam" -> {
                val paramKey = optionValue!!.substringBefore("=")
                val paramValue = optionValue.substringAfter("=")

                currentProcessorParams[paramKey] = paramValue
            }
            "build" -> {
                val sourcePath = optionValue!!.substringBefore(";")
                val destinationPath = optionValue.substringAfter(";", "")

                currentBuildEntry.contentFilepath = sourcePath
                if (destinationPath != "") {
                    currentBuildEntry.destinationFilepath = destinationPath
                }
                currentBuildEntry.processorParams = currentProcessorParams.toMap()
                configuration.buildEntries.add(currentBuildEntry)
                currentBuildEntry = BuildEntry()
            }
            "launchdebugger" -> configuration.launchDebugger = true
        }
    }
}
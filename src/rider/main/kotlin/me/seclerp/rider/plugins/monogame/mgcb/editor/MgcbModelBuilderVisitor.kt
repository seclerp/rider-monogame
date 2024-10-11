package me.seclerp.rider.plugins.monogame.mgcb.editor

import me.seclerp.rider.plugins.monogame.mgcb.psi.*

class MgcbModelBuilderVisitor : MgcbVisitor() {
    val resultingModel = MgcbIntermediateModel()
    private val currentProcessorParams = mutableMapOf<String, String>()

    private var currentBuildEntry = BuildEntry()

    override fun visitOption(option: MgcbOption) {
        super.visitOption(option)
        val optionValue = option.getValue()

        when (option.getKey()) {
            "/outputDir" -> resultingModel.outputDir = optionValue
            "/intermediateDir" -> resultingModel.intermediateDir = optionValue
            "/rebuild" -> resultingModel.rebuild = true
            "/clean" -> resultingModel.clean = true
            "/incremental" -> resultingModel.incremental = true
            "/reference" -> resultingModel.references.add(optionValue!!)
            "/platform" -> resultingModel.platform = optionValue
            "/profile" -> resultingModel.profile = optionValue
            "/config" -> resultingModel.config = optionValue
            "/compress" -> resultingModel.compress = true

            "/importer" -> currentBuildEntry.importer = optionValue
            "/processor" -> {
                currentProcessorParams.clear()
                currentBuildEntry.processor = optionValue
            }
            "/processorParam" -> {
                val paramKey = optionValue!!.substringBefore("=")
                val paramValue = optionValue.substringAfter("=")

                currentProcessorParams[paramKey] = paramValue
            }
            "/build" -> {
                val sourcePath = optionValue!!.substringBefore(";")
                val destinationPath = optionValue.substringAfter(";", "")

                currentBuildEntry.contentFilepath = sourcePath
                if (destinationPath != "") {
                    currentBuildEntry.destinationFilepath = destinationPath
                }
                currentBuildEntry.processorParams = currentProcessorParams.toMap()
                resultingModel.buildEntries.add(currentBuildEntry)
                currentBuildEntry = BuildEntry(
                    importer = currentBuildEntry.importer,
                    processor = currentBuildEntry.processor
                )
            }
            "/launchdebugger" -> resultingModel.launchDebugger = true
        }
    }
}
package me.seclerp.rider.plugins.monogame.mgcb.previewer

data class MgcbModel(
    // Specifies the directory where all content is written.
    // Defaults to the current working directory.
    var outputDir: String? = null,
    // Specifies the directory where all intermediate files are written.
    // Defaults to the current working directory.
    var intermediateDir: String? = null,
    // Force a full rebuild of all content.
    var rebuild: Boolean = false,
    // Delete all previously built content and intermediate files.
    // Only the /intermediateDir and /outputDir need to be defined for clean to do its job.
    var clean: Boolean = false,
    // Only build content that changed since the last build.
    var incremental: Boolean = false,
    // An optional parameter which adds an assembly reference which contains importers, processors, or writers needed during content building.
    var references: MutableList<String> = mutableListOf(),
    // Set the target platform for this build. It must be a member of the TargetPlatform enum
    var platform: String? = null,
    // Set the target graphics profile for this build. It must be a member of the GraphicsProfile enum
    var profile: String? = null,
    // The optional build configuration name from the build system. This is sometimes used as a hint in content processors.
    var config: String? = null,
    // Uses LZ4 compression to compress the contents of the XNB files
    var compress: Boolean = false,
    // Instructs the content builder to build the specified content file using the previously set switches and options
    var buildEntries: MutableList<BuildEntry> = mutableListOf(),
    // Allows a debugger to attach to the MGCB executable before content is built
    var launchDebugger: Boolean = false
)

data class BuildEntry(
    // Source path to the asset to build
    var contentFilepath: String? = null,
    // Destination path of the asset to build to
    var destinationFilepath: String? = null,
    // An optional parameter which defines the class name of the content importer for reading source content
    var importer: String? = null,
    // An optional parameter which defines the class name of the content processor for processing imported content
    var processor: String? = null,
    // An optional parameter which defines a parameter name and value to set on a content processor
    var processorParams: Map<String, String> = mapOf(),
)
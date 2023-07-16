using System;
using System.Linq;
using JetBrains.Annotations;
using JetBrains.ProjectModel.NuGet.DotNetTools;

namespace Rider.Plugins.MonoGame.Mgcb;

public static class MgcbEditorCommandNameResolver
{
    // TODO: Replace with global tool version lookup using project.assets.json and DotNetSettings.xml
    public static string Resolve([NotNull] GlobalToolCacheEntry tool) => tool.ToolName switch
    {
        KnownDotNetTools.MgcbEditor => KnownDotNetToolsCommands.MgcbEditor,
        KnownDotNetTools.MgcbEditorWindows => KnownDotNetToolsCommands.MgcbEditorWindows,
        KnownDotNetTools.MgcbEditorLinux => KnownDotNetToolsCommands.MgcbEditorLinux,
        KnownDotNetTools.MgcbEditorMac => KnownDotNetToolsCommands.MgcbEditorMac,
        var other => other
    };

    public static string Resolve([NotNull] LocalTool tool) => tool.Commands
        .Select(command => command.Name)
        .FirstOrDefault() ?? tool.PackageId;
}
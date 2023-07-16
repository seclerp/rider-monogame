using System;
using System.Linq;
using System.Runtime.InteropServices;
using JetBrains.Annotations;
using JetBrains.ProjectModel.NuGet.DotNetTools;
using NuGet.Versioning;
using Rider.Plugins.MonoGame.Extensions;

namespace Rider.Plugins.MonoGame.Mgcb;

public static class MgcbEditorToolResolver
{
    private static readonly string PlatformSpecificToolName = ResolvePlatformSpecificToolName();

    [CanBeNull]
    public static LocalTool Resolve([NotNull] DotNetToolLocalCache cache)
    {
        var platformSpecificTool = cache.GetLocalTool(PlatformSpecificToolName);
        var platformSpecificToolRef = platformSpecificTool?.Let(VersionedTool.Create);

        var platformAgnosticTool = cache.GetLocalTool(KnownDotNetTools.MgcbEditor);
        var platformAgnosticToolRef = platformAgnosticTool?.Let(VersionedTool.Create);

        return Resolve(platformSpecificToolRef, platformAgnosticToolRef)?.Tool;
    }

    [CanBeNull]
    public static GlobalToolCacheEntry Resolve([NotNull] DotNetToolGlobalCache cache)
    {
        var platformSpecificTool = cache.GetGlobalTool(PlatformSpecificToolName)?.FirstOrDefault();
        var platformSpecificToolRef = platformSpecificTool?.Let(VersionedTool.Create);

        var platformAgnosticTool = cache.GetGlobalTool(KnownDotNetTools.MgcbEditor)?.FirstOrDefault();
        var platformAgnosticToolRef = platformAgnosticTool?.Let(VersionedTool.Create);

        return Resolve(platformSpecificToolRef, platformAgnosticToolRef)?.Tool;
    }

    [CanBeNull]
    private static VersionedTool<TTool> Resolve<TTool>(
        [CanBeNull] VersionedTool<TTool> platformSpecificVersionedTool,
        [CanBeNull] VersionedTool<TTool> platformAgnosticVersionedTool)
    {
        switch (platformSpecificTool: platformSpecificVersionedTool, platformAgnosticTool: platformAgnosticVersionedTool)
        {
            // If we have only platform-specific (i.e. mgcb-editor-mac) tool, use it
            case (not null, null):
            {
                return platformSpecificVersionedTool;
            }
            // If we have only platform-agnostic tool (i.e. mgcb-editor)
            case (null, not null):
            {
                // We need to check if it's only a bootstrapper (>=3.8.1) or an editor itself (<=3.8.0)
                // If it's a bootsrapper, return null as it requires a platform-specific tool that we checked for before
                if (platformAgnosticVersionedTool.Version >= KnownMgcbVersions.Version381)
                {
                    return null;
                }

                // If it's an editor itself, use it
                return platformAgnosticVersionedTool;
            }
            // If we have both, specific one always has the priority because it's always newer by default
            case (not null, not null):
            {
                return platformSpecificVersionedTool;
            }
            // Nothing at all, nothing to choose from.
            case (null, null):
            {
                return null;
            }
        }

        throw new ArgumentOutOfRangeException($"{nameof(platformSpecificVersionedTool)} and {nameof(platformAgnosticVersionedTool)}");
    }

    private static string ResolvePlatformSpecificToolName()
    {
        if (RuntimeInformation.IsOSPlatform(OSPlatform.Windows))
            return KnownDotNetTools.MgcbEditorWindows;

        if (RuntimeInformation.IsOSPlatform(OSPlatform.Linux))
            return KnownDotNetTools.MgcbEditorLinux;

        if (RuntimeInformation.IsOSPlatform(OSPlatform.OSX))
            return KnownDotNetTools.MgcbEditorMac;

        throw new NotImplementedException("Unsupported Operating System");
    }

    private record VersionedTool<TTool>(
        NuGetVersion Version,
        [CanBeNull] TTool Tool
    );

    private static class VersionedTool
    {
        public static VersionedTool<GlobalToolCacheEntry> Create(GlobalToolCacheEntry tool) =>
            new(tool.Version, tool);

        public static VersionedTool<LocalTool> Create(LocalTool tool) =>
            new(NuGetVersion.Parse(tool.Version), tool);
    }

}
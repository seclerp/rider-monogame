using JetBrains.Collections.Viewable;
using JetBrains.Lifetimes;
using JetBrains.ProjectModel;
using JetBrains.ProjectModel.NuGet.DotNetTools;
using JetBrains.Rd.Base;
using JetBrains.RdBackend.Common.Features;
using JetBrains.Util;
using Rider.Plugins.MonoGame.Mgcb;

namespace Rider.Plugins.MonoGame;

[SolutionComponent]
public class MonoGameRdModelHost
{
    public MonoGameRdModelHost(
        Lifetime lifetime,
        ISolution solution,
        MgcbToolsTracker toolsTracker,
        ILogger logger)
    {
        var model = solution.GetProtocolSolution().GetMonoGameRiderModel();
        BindGlobalToolset(toolsTracker.MgcbEditorGlobalToolset, model.MgcbEditorGlobalToolset, lifetime);
        toolsTracker.MgcbEditorProjectsToolset.View(lifetime, (projectLifetime, project, toolProperty) =>
        {
            projectLifetime.Bracket(
                () =>
                {
                    var projectToolset = new MgcbEditorToolset();
                    BindLocalToolset(toolProperty, projectToolset, projectLifetime);
                    model.MgcbEditorProjectsToolsets.Add(project.Guid, projectToolset);
                },
                () =>
                {
                    Unset(model.MgcbEditorProjectsToolsets[project.Guid]);
                    model.MgcbEditorProjectsToolsets.Remove(project.Guid);
                });
        });
    }

    private void BindGlobalToolset(MgcbToolset<GlobalToolCacheEntry> source, MgcbEditorToolset target, Lifetime lifetime)
    {
        source.MgcbEditor.FlowInto(lifetime, target.Editor, cacheEntry =>
            MapGlobalTool(KnownDotNetTools.MgcbEditor, cacheEntry));
        source.MgcbEditorWindows.FlowInto(lifetime, target.Editor, cacheEntry =>
            MapGlobalTool(KnownDotNetTools.MgcbEditorWin, cacheEntry));
        source.MgcbEditorLinux.FlowInto(lifetime, target.Editor, cacheEntry =>
            MapGlobalTool(KnownDotNetTools.MgcbEditorLinux, cacheEntry));
        source.MgcbEditorMac.FlowInto(lifetime, target.Editor, cacheEntry =>
            MapGlobalTool(KnownDotNetTools.MgcbEditorMac, cacheEntry));
    }

    private void BindLocalToolset(MgcbToolset<LocalTool> source, MgcbEditorToolset target, Lifetime lifetime)
    {
        source.MgcbEditor.FlowInto(lifetime, target.Editor, cacheEntry =>
            MapLocalTool(KnownDotNetTools.MgcbEditor, cacheEntry));
        source.MgcbEditorWindows.FlowInto(lifetime, target.Editor, cacheEntry =>
            MapLocalTool(KnownDotNetTools.MgcbEditorWin, cacheEntry));
        source.MgcbEditorLinux.FlowInto(lifetime, target.Editor, cacheEntry =>
            MapLocalTool(KnownDotNetTools.MgcbEditorLinux, cacheEntry));
        source.MgcbEditorMac.FlowInto(lifetime, target.Editor, cacheEntry =>
            MapLocalTool(KnownDotNetTools.MgcbEditorMac, cacheEntry));
    }

    private static ToolDefinition MapGlobalTool(string expectedId, GlobalToolCacheEntry tool) => tool switch
    {
        null => new ToolDefinition(expectedId, string.Empty, ToolKind.None),
        var other => new ToolDefinition(other.ToolName, other.Version.ToString(), ToolKind.Global)
    };

    private static ToolDefinition MapLocalTool(string expectedId, LocalTool tool) => tool switch
    {
        null => new ToolDefinition(expectedId, string.Empty, ToolKind.None),
        var other => new ToolDefinition(other.PackageId, other.Version, ToolKind.Local)
    };

    private static void Unset(MgcbEditorToolset toolset)
    {
        toolset.Editor.SetValue(new ToolDefinition(KnownDotNetTools.MgcbEditor, string.Empty, ToolKind.None));
        toolset.EditorWindows.SetValue(new ToolDefinition(KnownDotNetTools.MgcbEditorWin, string.Empty, ToolKind.None));
        toolset.EditorLinux.SetValue(new ToolDefinition(KnownDotNetTools.MgcbEditorLinux, string.Empty, ToolKind.None));
        toolset.EditorMac.SetValue(new ToolDefinition(KnownDotNetTools.MgcbEditorMac, string.Empty, ToolKind.None));
    }
}
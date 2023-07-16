using System;
using System.Linq;
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
        MgcbToolsetTracker toolsetTracker,
        ILogger logger)
    {
        var model = solution.GetProtocolSolution().GetMonoGameRiderModel();

        BindGlobalToolset(toolsetTracker.MgcbGlobalToolset, model.MgcbGlobalToolset, lifetime);
        BindLocalToolset(toolsetTracker.MgcbSolutionToolset, model.MgcbSolutionToolset, lifetime);
        BindProjectsToolsets(toolsetTracker.MgcbProjectsToolset, model.MgcbProjectsToolsets, lifetime);
    }

    private void BindGlobalToolset(MgcbToolset<GlobalToolCacheEntry> source, MgcbEditorToolset target, Lifetime lifetime)
    {
        source.Editor.FlowInto(lifetime, target.Editor, cacheEntry =>
            MapGlobalTool(KnownDotNetTools.MgcbEditor, cacheEntry));
    }

    private void BindLocalToolset(MgcbToolset<LocalTool> source, MgcbEditorToolset target, Lifetime lifetime)
    {
        source.Editor.FlowInto(lifetime, target.Editor, cacheEntry =>
            MapLocalTool(KnownDotNetTools.MgcbEditor, cacheEntry));
    }

    private void BindProjectsToolsets(IViewableMap<IProject,MgcbToolset<LocalTool>> source, IViewableMap<Guid,MgcbEditorToolset> target, Lifetime lifetime)
    {
        source.View(lifetime, (projectLifetime, project, toolProperty) =>
        {
            projectLifetime.Bracket(
                () =>
                {
                    var projectToolset = new MgcbEditorToolset();
                    BindLocalToolset(toolProperty, projectToolset, projectLifetime);
                    target.Add(project.Guid, projectToolset);
                },
                () =>
                {
                    Unset(target[project.Guid]);
                    target.Remove(project.Guid);
                });
        });
    }

    private static ToolDefinition MapGlobalTool(string expectedId, GlobalToolCacheEntry tool) => tool switch
    {
        null => new ToolDefinition(expectedId, string.Empty, string.Empty),
        var other => new ToolDefinition(other.ToolName, MgcbEditorCommandNameResolver.Resolve(other), other.Version.ToString())
    };

    private static ToolDefinition MapLocalTool(string expectedId, LocalTool tool) => tool switch
    {
        null => new ToolDefinition(expectedId, string.Empty, string.Empty),
        var other => new ToolDefinition(other.PackageId, MgcbEditorCommandNameResolver.Resolve(other), other.Version)
    };

    private static void Unset(MgcbEditorToolset toolset)
    {
        toolset.Editor.SetValue(new ToolDefinition(KnownDotNetTools.MgcbEditor, string.Empty, string.Empty));
    }
}
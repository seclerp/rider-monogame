using System;
using JetBrains.Annotations;
using JetBrains.Collections.Viewable;
using JetBrains.Lifetimes;
using JetBrains.ProjectModel;
using JetBrains.ProjectModel.NuGet.DotNetTools;
using JetBrains.Rd.Base;
using JetBrains.RdBackend.Common.Features;
using JetBrains.ReSharper.Feature.Services.Protocol;
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
            cacheEntry is not null ? MapGlobalTool(cacheEntry) : null);
    }

    private void BindLocalToolset(MgcbToolset<LocalTool> source, MgcbEditorToolset target, Lifetime lifetime)
    {
        source.Editor.FlowInto(lifetime, target.Editor, cacheEntry =>
            cacheEntry is not null ? MapLocalTool(cacheEntry) : null);
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

    [NotNull]
    private static ToolDefinition MapGlobalTool([NotNull] GlobalToolCacheEntry tool) =>
        new (tool.ToolName, MgcbEditorCommandNameResolver.Resolve(tool), tool.Version.ToString());

    [NotNull]
    private static ToolDefinition MapLocalTool([NotNull] LocalTool tool) =>
        new (tool.PackageId, MgcbEditorCommandNameResolver.Resolve(tool), tool.Version);

    private static void Unset(MgcbEditorToolset toolset)
    {
        toolset.Editor.SetValue(null);
    }
}
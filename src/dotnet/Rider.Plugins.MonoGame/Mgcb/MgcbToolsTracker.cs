using System.Collections.Generic;
using System.Linq;
using JetBrains.Application.FileSystemTracker;
using JetBrains.Application.Threading;
using JetBrains.Collections.Viewable;
using JetBrains.DataFlow;
using JetBrains.Lifetimes;
using JetBrains.ProjectModel;
using JetBrains.ProjectModel.NuGet.DotNetTools;
using JetBrains.ProjectModel.ProjectsHost.SolutionHost.Impl;
using JetBrains.Util;
using Rider.Plugins.MonoGame.Extensions;

namespace Rider.Plugins.MonoGame.Mgcb;

/// <summary>
/// MonoGame MGCB tools aka mgcb-editor could be installed in different places:<br/>
/// - globally on user's machine (3.8.0 approach)<br/>
/// - locally in solution (3.8.0 approach)<br/>
/// - locally in project (3.8.1 approach)<br/>
/// So we need to track all these places.
/// </summary>
[SolutionComponent]
public class MgcbToolsTracker
{
    public IProperty<GlobalToolCacheEntry> MgcbEditorGlobalTool;
    public IProperty<LocalTool> MgcbEditorSolutionTool;
    public IViewableMap<IProject, IProperty<LocalTool>> MgcbEditorProjectTools;

    private IDictionary<IProject, ProjectDotnetToolsTracker> _projectToolsTrackers = new Dictionary<IProject, ProjectDotnetToolsTracker>();

    public MgcbToolsTracker(
        ISolution solution,
        IViewableProjectsCollection projectsCollection,
        Lifetime lifetime,
        SolutionDotnetToolsTracker solutionToolsTracker,
        IFileSystemTracker fileSystemTracker,
        IShellLocks locks,
        ILogger logger,
        ISolutionToolset toolset)
    {
        MgcbEditorGlobalTool = solutionToolsTracker.DotNetToolCache.Select(
            lifetime,
            nameof(MgcbToolsTracker),
            cache => cache?.ToolGlobalCache?.GetGlobalTool(KnownDotNetTools.MgcbEditor)?.FirstOrDefault());

        MgcbEditorSolutionTool = solutionToolsTracker.DotNetToolCache.Select(
            lifetime,
            nameof(MgcbToolsTracker),
            cache => cache?.ToolLocalCache?.GetLocalTool(KnownDotNetTools.MgcbEditor));

        MgcbEditorProjectTools = new ViewableMap<IProject, IProperty<LocalTool>>();

        projectsCollection.Projects.View(
            lifetime,
            (projectLifetime, project) =>
            {
                projectLifetime.Bracket(
                    () =>
                    {
                        var tracker = new ProjectDotnetToolsTracker(projectLifetime, project,
                            fileSystemTracker, locks,
                            logger, toolset);
                        _projectToolsTrackers.Add(project, tracker);
                        MgcbEditorProjectTools.Add(project, tracker.DotNetToolCache.Select(
                            lifetime,
                            nameof(MgcbToolsTracker),
                            cache => cache?.ToolLocalCache?.GetLocalTool(KnownDotNetTools.MgcbEditor)));
                    },
                    () =>
                    {
                        _projectToolsTrackers.Remove(project);
                        MgcbEditorProjectTools[project].SetValue(null);
                        MgcbEditorProjectTools.Remove(project);
                    });
            });
    }
}
using System.Collections.Generic;
using JetBrains.Application.FileSystemTracker;
using JetBrains.Application.Parts;
using JetBrains.Application.Threading;
using JetBrains.Collections.Viewable;
using JetBrains.DataFlow;
using JetBrains.Lifetimes;
using JetBrains.ProjectModel;
using JetBrains.ProjectModel.NuGet.DotNetTools;
using JetBrains.Util;
using Rider.Plugins.MonoGame.Extensions;

namespace Rider.Plugins.MonoGame.Mgcb.Tools;

/// <summary>
/// MonoGame MGCB tools aka mgcb-editor could be installed in different places:<br/>
/// - globally on user's machine (3.8.0 approach)<br/>
/// - locally in solution (3.8.0 approach)<br/>
/// - locally in project (3.8.1 approach)<br/>
/// So we need to track all these places.
/// </summary>
[SolutionComponent(Instantiation.ContainerAsyncPrimaryThread)]
public class MgcbToolsetTracker
{
    public MgcbToolset<GlobalToolCacheEntry> MgcbGlobalToolset;
    public MgcbToolset<LocalTool> MgcbSolutionToolset;
    public IViewableMap<IProject, MgcbToolset<LocalTool>> MgcbProjectsToolset;

    private IDictionary<IProject, ProjectDotnetToolsTracker> _projectToolsTrackers = new Dictionary<IProject, ProjectDotnetToolsTracker>();

    public MgcbToolsetTracker(
        IViewableProjectsCollection projectsCollection,
        Lifetime lifetime,
        SolutionDotnetToolsTracker solutionToolsTracker,
        IFileSystemTracker fileSystemTracker,
        IShellLocks locks,
        ILogger logger,
        ISolutionToolset toolset)
    {
        // while (!Debugger.IsAttached) Thread.Sleep(1000);
        MgcbGlobalToolset = CreateGlobalToolset(solutionToolsTracker);
        MgcbSolutionToolset = CreateSolutionToolset(solutionToolsTracker);
        MgcbProjectsToolset = new ViewableMap<IProject, MgcbToolset<LocalTool>>();

        projectsCollection.Projects.View(
            lifetime,
            (projectLifetime, project) =>
            {
                if (project.Kind != ProjectItemKind.PROJECT || project.ProjectFile == null)
                    return;

                projectLifetime.Bracket(
                    () =>
                    {
                        var tracker = new ProjectDotnetToolsTracker(projectLifetime, project,
                            fileSystemTracker, locks,
                            logger, toolset);
                        _projectToolsTrackers.Add(project, tracker);
                        MgcbProjectsToolset.Add(project, CreateProjectToolset(tracker));
                    },
                    () =>
                    {
                        _projectToolsTrackers.Remove(project);
                        MgcbProjectsToolset[project].Unset();
                        MgcbProjectsToolset.Remove(project);
                    });
            });
    }

    private MgcbToolset<GlobalToolCacheEntry> CreateGlobalToolset(SolutionDotnetToolsTracker solutionToolsTracker) =>
        new()
        {
            Editor = ObserveGlobalTool(solutionToolsTracker),
        };

    private MgcbToolset<LocalTool> CreateSolutionToolset(SolutionDotnetToolsTracker solutionToolsTracker) =>
        new()
        {
            Editor = ObserveLocalTool(solutionToolsTracker),
        };

    private MgcbToolset<LocalTool> CreateProjectToolset(ProjectDotnetToolsTracker projectToolsTracker) =>
        new()
        {
            Editor = ObserveLocalTool(projectToolsTracker),
        };

    private IProperty<GlobalToolCacheEntry> ObserveGlobalTool(NuGetDotnetToolsTrackerBase toolsTracker) =>
        toolsTracker.DotNetToolCache.Select(
            nameof(MgcbToolsetTracker),
            cache => cache?.ToolGlobalCache?.Let(MgcbEditorToolResolver.Resolve));

    private IProperty<LocalTool> ObserveLocalTool(NuGetDotnetToolsTrackerBase toolsTracker) =>
        toolsTracker.DotNetToolCache.Select(
            nameof(MgcbToolsetTracker),
            cache => cache?.ToolLocalCache?.Let(MgcbEditorToolResolver.Resolve));
}
using System.Collections.Generic;
using System.Linq;
using JetBrains.Application.FileSystemTracker;
using JetBrains.Application.Threading;
using JetBrains.Collections.Viewable;
using JetBrains.DataFlow;
using JetBrains.Lifetimes;
using JetBrains.ProjectModel;
using JetBrains.ProjectModel.NuGet.DotNetTools;
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
    public MgcbToolset<GlobalToolCacheEntry> MgcbEditorGlobalToolset;
    public MgcbToolset<LocalTool> MgcbEditorSolutionToolset;
    public IViewableMap<IProject, MgcbToolset<LocalTool>> MgcbEditorProjectsToolset;

    private IDictionary<IProject, ProjectDotnetToolsTracker> _projectToolsTrackers = new Dictionary<IProject, ProjectDotnetToolsTracker>();

    public MgcbToolsTracker(
        IViewableProjectsCollection projectsCollection,
        Lifetime lifetime,
        SolutionDotnetToolsTracker solutionToolsTracker,
        IFileSystemTracker fileSystemTracker,
        IShellLocks locks,
        ILogger logger,
        ISolutionToolset toolset)
    {
        MgcbEditorGlobalToolset = CreateGlobalToolset(solutionToolsTracker, lifetime);
        MgcbEditorSolutionToolset = CreateSolutionToolset(solutionToolsTracker, lifetime);
        MgcbEditorProjectsToolset = new ViewableMap<IProject, MgcbToolset<LocalTool>>();

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
                        MgcbEditorProjectsToolset.Add(project, CreateProjectToolset(tracker, projectLifetime));
                    },
                    () =>
                    {
                        _projectToolsTrackers.Remove(project);
                        MgcbEditorProjectsToolset[project].Unset();
                        MgcbEditorProjectsToolset.Remove(project);
                    });
            });
    }

    private MgcbToolset<GlobalToolCacheEntry> CreateGlobalToolset(SolutionDotnetToolsTracker solutionToolsTracker, Lifetime solutionLifetime)
    {
        return new MgcbToolset<GlobalToolCacheEntry>
        {
            MgcbEditor = ObserveGlobalTool(KnownDotNetTools.MgcbEditor, solutionToolsTracker, solutionLifetime),
            MgcbEditorWindows = ObserveGlobalTool(KnownDotNetTools.MgcbEditorWin, solutionToolsTracker, solutionLifetime),
            MgcbEditorLinux = ObserveGlobalTool(KnownDotNetTools.MgcbEditorLinux, solutionToolsTracker, solutionLifetime),
            MgcbEditorMac = ObserveGlobalTool(KnownDotNetTools.MgcbEditorMac, solutionToolsTracker, solutionLifetime)
        };
    }

    private MgcbToolset<LocalTool> CreateSolutionToolset(SolutionDotnetToolsTracker solutionToolsTracker, Lifetime solutionLifetime)
    {
        return new MgcbToolset<LocalTool>
        {
            MgcbEditor = ObserveLocalTool(KnownDotNetTools.MgcbEditor, solutionToolsTracker, solutionLifetime),
            MgcbEditorWindows = ObserveLocalTool(KnownDotNetTools.MgcbEditorWin, solutionToolsTracker, solutionLifetime),
            MgcbEditorLinux = ObserveLocalTool(KnownDotNetTools.MgcbEditorLinux, solutionToolsTracker, solutionLifetime),
            MgcbEditorMac = ObserveLocalTool(KnownDotNetTools.MgcbEditorMac, solutionToolsTracker, solutionLifetime)
        };
    }

    private MgcbToolset<LocalTool> CreateProjectToolset(ProjectDotnetToolsTracker projectToolsTracker, Lifetime projectLifetime)
    {
        return new MgcbToolset<LocalTool>
        {
            MgcbEditor = ObserveLocalTool(KnownDotNetTools.MgcbEditor, projectToolsTracker, projectLifetime),
            MgcbEditorWindows = ObserveLocalTool(KnownDotNetTools.MgcbEditorWin, projectToolsTracker, projectLifetime),
            MgcbEditorLinux = ObserveLocalTool(KnownDotNetTools.MgcbEditorLinux, projectToolsTracker, projectLifetime),
            MgcbEditorMac = ObserveLocalTool(KnownDotNetTools.MgcbEditorMac, projectToolsTracker, projectLifetime)
        };
    }

    private IProperty<GlobalToolCacheEntry> ObserveGlobalTool(string packageId, NuGetDotnetToolsTrackerBase toolsTracker, Lifetime solutionLifetime)
    {
        return toolsTracker.DotNetToolCache.Select(
            solutionLifetime,
            nameof(MgcbToolsTracker),
            cache => cache?.ToolGlobalCache?.GetGlobalTool(packageId)?.FirstOrDefault());
    }

    private IProperty<LocalTool> ObserveLocalTool(string packageId, NuGetDotnetToolsTrackerBase toolsTracker, Lifetime solutionLifetime)
    {
        return toolsTracker.DotNetToolCache.Select(
            solutionLifetime,
            nameof(MgcbToolsTracker),
            cache => cache?.ToolLocalCache?.GetLocalTool(packageId));
    }
}
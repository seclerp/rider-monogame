using System.Collections.Generic;
using System.Diagnostics;
using System.Linq;
using System.Threading;
using JetBrains.Application.FileSystemTracker;
using JetBrains.Application.Threading;
using JetBrains.Collections.Viewable;
using JetBrains.DataFlow;
using JetBrains.Lifetimes;
using JetBrains.ProjectModel;
using JetBrains.ProjectModel.NuGet.DotNetTools;
using JetBrains.ReSharper.Psi.Impl.CodeStyle;
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
        MgcbGlobalToolset = CreateGlobalToolset(solutionToolsTracker, lifetime);
        MgcbSolutionToolset = CreateSolutionToolset(solutionToolsTracker, lifetime);
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
                        MgcbProjectsToolset.Add(project, CreateProjectToolset(tracker, projectLifetime));
                    },
                    () =>
                    {
                        _projectToolsTrackers.Remove(project);
                        MgcbProjectsToolset[project].Unset();
                        MgcbProjectsToolset.Remove(project);
                    });
            });
    }

    private MgcbToolset<GlobalToolCacheEntry> CreateGlobalToolset(SolutionDotnetToolsTracker solutionToolsTracker, Lifetime solutionLifetime)
    {
        return new MgcbToolset<GlobalToolCacheEntry>
        {
            Editor = ObserveGlobalTool(solutionToolsTracker, solutionLifetime),
        };
    }

    private MgcbToolset<LocalTool> CreateSolutionToolset(SolutionDotnetToolsTracker solutionToolsTracker, Lifetime solutionLifetime)
    {
        return new MgcbToolset<LocalTool>
        {
            Editor = ObserveLocalTool(solutionToolsTracker, solutionLifetime),
        };
    }

    private MgcbToolset<LocalTool> CreateProjectToolset(ProjectDotnetToolsTracker projectToolsTracker, Lifetime projectLifetime)
    {
        return new MgcbToolset<LocalTool>
        {
            Editor = ObserveLocalTool(projectToolsTracker, projectLifetime),
        };
    }

    private IProperty<GlobalToolCacheEntry> ObserveGlobalTool(NuGetDotnetToolsTrackerBase toolsTracker, Lifetime solutionLifetime)
    {
        return toolsTracker.DotNetToolCache.Select(
            solutionLifetime,
            nameof(MgcbToolsetTracker),
            cache => cache?.ToolGlobalCache?.Let(MgcbEditorToolResolver.Resolve));
    }

    private IProperty<LocalTool> ObserveLocalTool(NuGetDotnetToolsTrackerBase toolsTracker, Lifetime solutionLifetime)
    {
        return toolsTracker.DotNetToolCache.Select(
            solutionLifetime,
            nameof(MgcbToolsetTracker),
            cache => cache?.ToolLocalCache?.Let(MgcbEditorToolResolver.Resolve));
    }
}
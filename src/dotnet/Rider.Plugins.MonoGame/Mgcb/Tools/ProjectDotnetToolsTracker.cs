using JetBrains.Application.FileSystemTracker;
using JetBrains.Application.Threading;
using JetBrains.Lifetimes;
using JetBrains.ProjectModel;
using JetBrains.ProjectModel.NuGet.DotNetTools;
using JetBrains.Util;

namespace Rider.Plugins.MonoGame.Mgcb.Tools;

public class ProjectDotnetToolsTracker(
    Lifetime lifetime,
    IProject project,
    IFileSystemTracker fileSystemTracker,
    IShellLocks locks,
    ILogger logger,
    ISolutionToolset solutionToolset)
    : NuGetDotnetToolsTrackerBase(lifetime, project.Location, fileSystemTracker, locks, logger, solutionToolset);
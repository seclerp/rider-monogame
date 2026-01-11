using System.Linq;
using JetBrains.ProjectModel;
using JetBrains.ProjectModel.NuGet.Packaging;

namespace Rider.Plugins.MonoGame.Extensions;

public static class ProjectExtensions
{
    public static bool IsMonoGameProject(this IProject project)
    {
        var tracker = project.GetSolution().GetComponent<NuGetPackageReferenceTracker>();
        return tracker.GetInstalledPackages(project)
            .Any(package => package.PackageIdentity.Id.StartsWith("MonoGame.Framework."));
    }
}

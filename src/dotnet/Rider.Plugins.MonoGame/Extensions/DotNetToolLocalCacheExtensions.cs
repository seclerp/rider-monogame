using System.Linq;
using JetBrains.Annotations;
using JetBrains.ProjectModel.NuGet.DotNetTools;

namespace Rider.Plugins.MonoGame.Extensions;

public static class DotNetToolLocalCacheExtensions
{
    [CanBeNull]
    public static LocalTool GetLocalTool(this DotNetToolLocalCache cache, string packageId) =>
        cache.GetAllLocalTools().FirstOrDefault(tool => tool.PackageId == packageId);
}
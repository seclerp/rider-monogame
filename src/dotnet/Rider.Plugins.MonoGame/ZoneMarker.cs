using JetBrains.Application.BuildScript.Application.Zones;
using JetBrains.ProjectModel.NuGet;

namespace Rider.Plugins.MonoGame
{
    [ZoneMarker]
    public class ZoneMarker : IRequire<IMonoGameRiderZone>
    {
    }
}
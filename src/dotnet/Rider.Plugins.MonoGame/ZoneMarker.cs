using JetBrains.Application.BuildScript.Application.Zones;

namespace MonoGame.Rider
{
    [ZoneMarker]
    public class ZoneMarker : IRequire<IMonoGameRiderZone>
    {
    }
}
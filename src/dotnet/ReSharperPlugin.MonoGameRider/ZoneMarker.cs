using JetBrains.Application.BuildScript.Application.Zones;

namespace ReSharperPlugin.MonoGameRider
{
    [ZoneMarker]
    public class ZoneMarker : IRequire<IMonoGameRiderZone>
    {
    }
}
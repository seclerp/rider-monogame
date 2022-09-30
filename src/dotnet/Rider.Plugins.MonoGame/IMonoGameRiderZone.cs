using JetBrains.Application.BuildScript.Application.Zones;
using JetBrains.ProjectModel.NuGet;
using JetBrains.ReSharper.Feature.Services.Daemon;
using JetBrains.ReSharper.Psi;
using JetBrains.ReSharper.Psi.CSharp;

namespace Rider.Plugins.MonoGame
{
    [ZoneDefinition]
    // [ZoneDefinitionConfigurableFeature("Title", "Description", IsInProductSection: false)]
    public interface IMonoGameRiderZone : IPsiLanguageZone,
        IRequire<ILanguageCSharpZone>,
        IRequire<DaemonZone>,
        IRequire<INuGetZone>,
        IRequire<ISinceClr4HostZone>
    {
    }
}
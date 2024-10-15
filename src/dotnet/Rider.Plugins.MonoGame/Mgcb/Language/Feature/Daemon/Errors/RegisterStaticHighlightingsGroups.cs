using JetBrains.ReSharper.Feature.Services.Daemon;

using Rider.Plugins.MonoGame.Resources;

namespace Rider.Plugins.MonoGame.Mgcb.Language.Feature.Daemon.Errors
{
    [RegisterStaticHighlightingsGroup(typeof(Strings), nameof(Strings.MgcbErrors), true)]
    public class MgcbErrors
    {
    }

    [RegisterStaticHighlightingsGroup(typeof(Strings), nameof(Strings.MgcbWarnings), true)]
    public class MgcbWarnings
    {
    }
}
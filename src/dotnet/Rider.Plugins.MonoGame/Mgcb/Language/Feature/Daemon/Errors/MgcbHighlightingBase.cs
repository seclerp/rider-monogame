namespace Rider.Plugins.MonoGame.Mgcb.Language.Feature.Daemon.Errors
{
    public abstract class MgcbHighlightingBase
    {
        // ErrorsGen makes IsValid override if we specify a base class
        public abstract bool IsValid();
    }
}
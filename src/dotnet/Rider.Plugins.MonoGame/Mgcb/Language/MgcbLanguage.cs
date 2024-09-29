using JetBrains.ReSharper.Psi;

namespace Rider.Plugins.MonoGame.Mgcb.Language;

[LanguageDefinition(Name)]
public class MgcbLanguage : KnownLanguage
{
    public const string Name = "MgcbReSharper";
    public const string PresentableName = "MGCB (ReSharper)";

    public static MgcbLanguage Instance { get; private set; }

    protected MgcbLanguage() : base(Name, PresentableName)
    {
    }
}
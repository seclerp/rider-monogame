using JetBrains.Annotations;
using JetBrains.ReSharper.Psi;

namespace Rider.Plugins.MonoGame.Effect.Psi;

[LanguageDefinition(Name)]
public class EffectLanguage : KnownLanguage
{
    public new const string Name = "MGFX";

    [CanBeNull, UsedImplicitly]
    public static EffectLanguage Instance { get; private set; }

    public EffectLanguage()
        : base(Name, "MGFX")
    {
    }
}
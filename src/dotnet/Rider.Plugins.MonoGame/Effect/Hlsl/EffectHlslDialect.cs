using JetBrains.ReSharper.Psi.Cpp.Language;

namespace Rider.Plugins.MonoGame.Effect.Hlsl;

public class EffectHlslDialect : CppHLSLDialect
{
    public EffectHlslDialect(bool isMachine64Bit, CppLanguageKind languageKind = CppLanguageKind.HLSL) : base(isMachine64Bit, languageKind)
    {
    }
}
using JetBrains.ReSharper.Psi.Cpp.Language;

namespace Rider.Plugins.MonoGame.Extensions;

public class EffectHlslDialect : CppHLSLDialect
{
    public EffectHlslDialect() : base(true, CppLanguageKind.HLSL)
    {
    }
}

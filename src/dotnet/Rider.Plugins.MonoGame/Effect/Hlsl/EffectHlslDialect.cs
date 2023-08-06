using JetBrains.ReSharper.Psi.Cpp.Language;

namespace Rider.Plugins.MonoGame.Effect.Hlsl;

public class EffectHlslDialect : CppHLSLDialect
{
    public EffectHlslDialect() : base(true)
    {
    }
}
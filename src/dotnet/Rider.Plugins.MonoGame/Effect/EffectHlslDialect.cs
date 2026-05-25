using JetBrains.ReSharper.Psi.Cpp.Language;

namespace Rider.Plugins.MonoGame.Effect;

public class EffectHlslDialect() : CppHLSLDialect(true)
{
  public override bool HasEffectTechnique => true;
}

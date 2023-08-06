using System.Collections.Generic;
using JetBrains.ReSharper.Psi;
using JetBrains.ReSharper.Psi.Cpp.Caches;

namespace Rider.Plugins.MonoGame.Effect.Hlsl;

public static class EffectCppFileLocationProvider
{
    public static IEnumerable<CppFileLocation> GetCppFileLocations(
        IPsiSourceFile sourceFile)
    {
        // TODO: Extend if needed
        yield return new CppFileLocation(sourceFile);
    }
}
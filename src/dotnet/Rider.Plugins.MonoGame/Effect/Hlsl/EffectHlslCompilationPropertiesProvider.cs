using JetBrains.ProjectModel;
using JetBrains.ReSharper.Psi.Cpp.Caches;
using JetBrains.ReSharper.Psi.Cpp.Util;

namespace Rider.Plugins.MonoGame.Effect.Hlsl;

[SolutionComponent]
public class EffectHlslCompilationPropertiesProvider : ICppCompilationPropertiesProvider
{
    public CppCompilationProperties GetCompilationProperties(IProject project, IProjectFile projectFile, CppFileLocation rootFile,
        CppGlobalSymbolCache globalCache)
    {

    }
}
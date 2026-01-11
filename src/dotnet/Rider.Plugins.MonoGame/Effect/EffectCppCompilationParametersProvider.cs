using JetBrains.ProjectModel;
using JetBrains.ReSharper.Psi.Cpp.Caches;
using JetBrains.ReSharper.Psi.Cpp.Language;
using JetBrains.ReSharper.Psi.Cpp.Util;
using Rider.Plugins.MonoGame.Extensions;

namespace Rider.Plugins.MonoGame.Effect;

[SolutionComponent]
public class EffectCppCompilationParametersProvider
{
    public EffectHlslDialect EffectHlslDialect = new();

    public CppCompilationProperties GetCompilationProperties(IProject project, IProjectFile projectFile, CppFileLocation rootFile,
        CppGlobalSymbolCache globalCache)
    {
        if (project.IsDotNetCoreProject() && rootFile.Location.ExtensionWithDot is CppProjectFileType.FX_EXTENSION or CppProjectFileType.FXH_EXTENSION)
        {
            return CreateProperties(EffectHlslDialect);
        }

        return null;
    }

    private static CppCompilationProperties CreateProperties(CppHLSLDialect dialect)
    {
        return new CppCompilationProperties
        {
            OverridenDialect = dialect,
            LanguageKind = dialect.LanguageKind,
        };
    }
}

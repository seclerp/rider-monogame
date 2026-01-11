using JetBrains.ProjectModel;
using JetBrains.ProjectModel.Properties.VCXProj;
using JetBrains.ReSharper.Psi.Cpp.Caches;
using JetBrains.ReSharper.Psi.Cpp.Language;
using JetBrains.ReSharper.Psi.Cpp.Util;

namespace Rider.Plugins.MonoGame.Effect;

[CppCompilationPropertiesProvider(Priority = CppCompilationPropertiesProviderAttribute.NORMAL_PRIORITY)]
public class EffectCppCompilationPropertiesProvider : ICppCompilationPropertiesProvider
{
    public EffectHlslDialect EffectHlslDialect = new();

    public CppCompilationProperties GetCompilationProperties(IProject project, IProjectFile projectFile, CppFileLocation rootFile,
        CppGlobalSymbolCache globalCache, CppIntelliSenseInfo intelliSenseInfo)
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

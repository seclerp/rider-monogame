using JetBrains.ProjectModel;
using JetBrains.ProjectModel.Properties.VCXProj;
using JetBrains.ReSharper.Psi.Cpp.Caches;
using JetBrains.ReSharper.Psi.Cpp.Symbols;
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

    private static CppCompilationProperties CreateProperties(EffectHlslDialect dialect)
    {
        return new CppCompilationProperties
        {
            OverridenDialect = dialect,
            LanguageKind = dialect.LanguageKind,
            PredefinedMacros =
            {
                CppPPDefineSymbolUtil.CreatePredefinedSymbol("MGFX", "1"),
                CppPPDefineSymbolUtil.CreatePredefinedSymbol("HLSL", "1"),
                CppPPDefineSymbolUtil.CreatePredefinedSymbol("SM4", "1"),

                // DX9-era legacy texture type aliases (HLSL is case-insensitive)
                CppPPDefineSymbolUtil.CreatePredefinedSymbol("texture", "Texture2D"),
                CppPPDefineSymbolUtil.CreatePredefinedSymbol("texture1D", "Texture1D"),
                CppPPDefineSymbolUtil.CreatePredefinedSymbol("texture2D", "Texture2D"),
                CppPPDefineSymbolUtil.CreatePredefinedSymbol("texture3D", "Texture3D"),
                CppPPDefineSymbolUtil.CreatePredefinedSymbol("textureCUBE", "TextureCube"),
            }
        };
    }
}

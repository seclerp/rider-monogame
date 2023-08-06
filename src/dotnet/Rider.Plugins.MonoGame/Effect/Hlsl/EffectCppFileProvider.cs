using System.Collections.Generic;
using JetBrains.Application;
using JetBrains.ProjectModel;
using JetBrains.ReSharper.Feature.Services.Cpp.Caches;
using JetBrains.ReSharper.Psi.Cpp.Caches;
using JetBrains.ReSharper.Psi.Modules;
using Rider.Plugins.MonoGame.Effect.ProjectModel;

namespace Rider.Plugins.MonoGame.Effect.Hlsl;

/// <summary>
/// This component is responsible for locating HLSL (C++) related text ranges (injection ranges) in a MGFX file.
/// </summary>
[SolutionComponent]
public class EffectCppFileProvider : ICppInitialFilesProvider
{
    private readonly PsiModules _psiModules;

    public EffectCppFileProvider(PsiModules psiModules)
    {
        _psiModules = psiModules;
    }

    public IEnumerable<CppFileLocation> GetCppFileLocations()
    {
        foreach (var module in _psiModules.GetSourceModules())
        {
            if (module.ContainingProjectModule is IProject project && project.IsVCXMiscProjectInVs2015())
                continue;

            foreach (var f in module.SourceFiles)
            {
                Interruption.Current.CheckAndThrow();

                if (f.IsValid() && f.LanguageType.Is<EffectProjectFileType>())
                {
                    foreach (var cppFileLocation in EffectCppFileLocationProvider.GetCppFileLocations(f))
                    {
                        yield return cppFileLocation;
                    }
                }
            }
        }
    }
}
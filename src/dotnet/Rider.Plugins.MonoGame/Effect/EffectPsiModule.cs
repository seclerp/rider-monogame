#nullable enable
using System.Collections.Generic;
using JetBrains.Metadata.Reader.API;
using JetBrains.ProjectModel;
using JetBrains.ReSharper.Psi;
using JetBrains.ReSharper.Psi.Cpp.Language;
using JetBrains.ReSharper.Psi.Impl;
using JetBrains.ReSharper.Psi.Modules;
using JetBrains.Util;
using JetBrains.Util.Dotnet.TargetFrameworkIds;

namespace Rider.Plugins.MonoGame.Effect;

public class EffectPsiModule : UserDataHolder, IPsiModule
{
    private readonly ISolution _mySolution;
    private readonly string _myPersistentId;

    public EffectPsiModule(ISolution solution, string name, TargetFrameworkId targetFrameworkId)
    {
        _mySolution = solution;
        _myPersistentId = "MonoGameEffectModule:" + name;
        TargetFrameworkId = targetFrameworkId;
        Files = new Dictionary<IProjectFile, PsiProjectFile>();
    }

    public string Name => "MonoGameEffectModule";

    public string DisplayName => "MonoGame Effect module";

    public TargetFrameworkId TargetFrameworkId { get; }

    public ISolution GetSolution() => _mySolution;

    public Dictionary<IProjectFile, PsiProjectFile> Files { get; }

    public IEnumerable<IPsiSourceFile> GetPsiSourceFileFor(IProjectFile projectFile)
    {
        if (Files.TryGetValue(projectFile, out var file))
            return new[] { file };
        return EmptyList<IPsiSourceFile>.InstanceList;
    }

    public PsiLanguageType? PsiLanguage => CppLanguage.Instance;

    public ProjectFileType? ProjectFileType => KnownProjectFileType.Instance;

    public IEnumerable<IPsiModuleReference> GetReferences(
        IModuleReferenceResolveContext? moduleReferenceResolveContext)
    {
        return EmptyList<IPsiModuleReference>.InstanceList;
    }

    public IModule? ContainingProjectModule => null;

    public IEnumerable<IPsiSourceFile> SourceFiles => Files.Values;

    public IPsiServices GetPsiServices()
    {
        return _mySolution.GetPsiServices();
    }

    public ICollection<PreProcessingDirective> GetAllDefines()
    {
        return EmptyList<PreProcessingDirective>.InstanceList;
    }

    public bool IsValid()
    {
        return true;
    }

    public string GetPersistentID()
    {
        return _myPersistentId;
    }
}

using System.Collections.Generic;
using JetBrains.DocumentManagers;
using JetBrains.ProjectModel;
using JetBrains.ReSharper.Psi;
using JetBrains.ReSharper.Psi.Impl;
using JetBrains.ReSharper.Psi.Modules;
using JetBrains.Util;
using JetBrains.Util.DataStructures.Collections;

namespace Rider.Plugins.MonoGame.Effect;

public class EffectModuleHandlerAndPsiDecorator : DelegatingProjectPsiModuleHandler, IPsiModuleDecorator
{
    private readonly IList<IPsiModule> _myAllModules;
    private readonly DocumentManager _myDocumentManager;
    private readonly EffectPsiModule _myPsiModule;

    public EffectModuleHandlerAndPsiDecorator(
        EffectPsiModule psiModule,
        IProjectPsiModuleHandler handler)
        : base(handler)
    {
        _myAllModules = new List<IPsiModule>(base.GetAllModules());
        _myAllModules.Add(psiModule);

        _myPsiModule = psiModule;
        var solution = psiModule.GetSolution();
        _myDocumentManager = solution.GetComponent<DocumentManager>();
    }

    public IEnumerable<IPsiModuleReference> OverrideModuleReferences(IEnumerable<IPsiModuleReference> references)
    {
        return references;
    }

    public override IList<IPsiModule> GetAllModules()
    {
        return _myAllModules;
    }

    public IEnumerable<IPsiSourceFile> OverrideSourceFiles(IEnumerable<IPsiSourceFile> files)
    {
        return files;
    }

    public override IEnumerable<IPsiSourceFile> GetPsiSourceFilesFor(IProjectFile projectFile)
    {
        var extension = projectFile.Location.ExtensionWithDot;
        if (!CppProjectFileType.ALL_HLSL_EXTENSIONS.Contains(extension) &&
            !CppProjectFileType.FX_EXTENSION.Equals(extension) &&
            !CppProjectFileType.FXH_EXTENSION.Equals(extension))
            return base.GetPsiSourceFilesFor(projectFile);

        if (_myPsiModule.Files.TryGetValue(projectFile, out var psiFile))
            return FixedList.Of(psiFile);

        return EmptyList<IPsiSourceFile>.Instance;
    }

    public override void OnProjectFileChanged(IProjectFile projectFile, VirtualFileSystemPath oldLocation,
        PsiModuleChange.ChangeType changeType,
        PsiModuleChangeBuilder changeBuilder)
    {
        var extension = VirtualFileSystemPath.TryParse(projectFile.Name, InteractionContext.SolutionContext).ExtensionWithDot;
        if (!CppProjectFileType.ALL_HLSL_EXTENSIONS.Contains(extension) &&
            !CppProjectFileType.FX_EXTENSION.Equals(extension) &&
            !CppProjectFileType.FXH_EXTENSION.Equals(extension))
        {
            base.OnProjectFileChanged(projectFile, oldLocation, changeType, changeBuilder);
            return;
        }

        if (changeType == PsiModuleChange.ChangeType.Removed)
        {
            if (_myPsiModule.Files.TryGetValue(projectFile, out var psiFile))
            {
                _myPsiModule.Files.Remove(projectFile);
                changeBuilder.AddFileChange(psiFile, PsiModuleChange.ChangeType.Removed);
            }
        }
        else if (changeType == PsiModuleChange.ChangeType.Added)
        {
            var sourceFile = new PsiProjectFile(_myPsiModule,
                projectFile,
                (file, sf) => GetFileProperties(sf),
                (file, sf) => _myPsiModule.Files.ContainsKey(file),
                _myDocumentManager,
                BaseHandler.PrimaryModule.GetResolveContextEx(projectFile));

            _myPsiModule.Files.Add(projectFile, sourceFile);
            changeBuilder.AddFileChange(sourceFile, PsiModuleChange.ChangeType.Added);
        }
        else if (changeType == PsiModuleChange.ChangeType.Modified)
        {
            if (_myPsiModule.Files.TryGetValue(projectFile, out var psiFile))
                changeBuilder.AddFileChange(psiFile, PsiModuleChange.ChangeType.Modified);
        }
    }

    private EffectPsiFileProperties GetFileProperties(IPsiSourceFile sourceFile)
    {
        return new EffectPsiFileProperties(true);
    }
}

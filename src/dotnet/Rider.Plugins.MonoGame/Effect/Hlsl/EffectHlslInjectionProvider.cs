using JetBrains.ReSharper.Feature.Services.Cpp.Injections;
using JetBrains.ReSharper.Psi;
using JetBrains.ReSharper.Psi.Cpp.Caches;
using JetBrains.ReSharper.Psi.ExtensionsAPI;
using JetBrains.ReSharper.Psi.Impl.Shared.InjectedPsi;
using JetBrains.ReSharper.Psi.Tree;

namespace Rider.Plugins.MonoGame.Effect.Hlsl;

public class EffectHlslInjectionProvider : CppInjectionProviderBase
{
    public override bool IsApplicable(PsiLanguageType originalLanguage)
    {
        throw new System.NotImplementedException();
    }

    public override bool IsApplicableToNode(ITreeNode node, IInjectedFileContext context)
    {
        throw new System.NotImplementedException();
    }

    public override IInjectedNodeContext Regenerate(IndependentInjectedNodeContext nodeContext)
    {
        throw new System.NotImplementedException();
    }

    protected override bool CanBeOriginalNode(ITreeNode node)
    {
        throw new System.NotImplementedException();
    }

    protected override CppFileLocation GetFileLocation(IPsiSourceFile sourceFile, ITreeNode originalNode)
    {
        throw new System.NotImplementedException();
    }
}
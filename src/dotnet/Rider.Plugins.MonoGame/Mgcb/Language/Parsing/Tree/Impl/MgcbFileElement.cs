using JetBrains.ReSharper.Psi;
using JetBrains.ReSharper.Psi.ExtensionsAPI.Tree;

namespace Rider.Plugins.MonoGame.Mgcb.Language.Parsing.Tree.Impl;

public abstract class MgcbFileElement : FileElementBase, IMgcbTreeNode
{
    public override PsiLanguageType Language => MgcbLanguage.Instance;
    public abstract void Accept(TreeNodeVisitor visitor);
    public abstract void Accept<TContext>(TreeNodeVisitor<TContext> visitor, TContext context);
    public abstract TReturn Accept<TContext, TReturn>(TreeNodeVisitor<TContext, TReturn> visitor, TContext context);
}
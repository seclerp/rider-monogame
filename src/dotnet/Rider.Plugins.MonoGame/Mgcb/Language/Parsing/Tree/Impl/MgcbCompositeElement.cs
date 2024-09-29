using JetBrains.ReSharper.Psi;
using JetBrains.ReSharper.Psi.ExtensionsAPI.Tree;

namespace Rider.Plugins.MonoGame.Mgcb.Language.Parsing.Tree.Impl;

public abstract class MgcbCompositeElement : CompositeElement, IMgcbTreeNode
{
    public override PsiLanguageType Language => MgcbLanguage.Instance;

    public virtual void Accept(TreeNodeVisitor visitor)
    {
        visitor.VisitNode(this);
    }

    public virtual void Accept<TContext>(TreeNodeVisitor<TContext> visitor, TContext context)
    {
        visitor.VisitNode(this, context);
    }

    public virtual TReturn Accept<TContext, TReturn>(TreeNodeVisitor<TContext, TReturn> visitor, TContext context)
    {
        return visitor.VisitNode(this, context);
    }
}
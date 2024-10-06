using System.Diagnostics;
using System.Threading;
using JetBrains.ReSharper.Daemon.CodeFolding;
using JetBrains.ReSharper.Psi.Tree;
using Rider.Plugins.MonoGame.Mgcb.Language.Parsing.Tree;

namespace Rider.Plugins.MonoGame.Mgcb.Language.Feature.Daemon.CodeFolding;

public class MgcbCodeFoldingProcessor : TreeNodeVisitor<FoldingHighlightingConsumer>, ICodeFoldingProcessor
{
    public bool InteriorShouldBeProcessed(ITreeNode element, FoldingHighlightingConsumer consumer) => true;
    public bool IsProcessingFinished(FoldingHighlightingConsumer consumer) => false;

    public void ProcessBeforeInterior(ITreeNode element, FoldingHighlightingConsumer consumer)
    {
        var treeNode = element as IMgcbTreeNode;
        treeNode?.Accept(this, consumer);
    }

    public void ProcessAfterInterior(ITreeNode element, FoldingHighlightingConsumer consumer)
    {
    }

    public override void VisitIf_instructionNode(IIf_instruction if_instructionParam, FoldingHighlightingConsumer consumer)
    {
        var placeholder = if_instructionParam.Value is not null
            ? $"{if_instructionParam.IfKeyword.GetText()} {if_instructionParam.Identifier.GetText()}={if_instructionParam.Value.GetText()}"
            : $"{if_instructionParam.IfKeyword.GetText()} {if_instructionParam.Identifier.GetText()}";

        consumer.AddFoldingForBracedConstruct(if_instructionParam.IfKeyword, if_instructionParam.EndIfKeyword, if_instructionParam, placeholder: placeholder);
    }
}
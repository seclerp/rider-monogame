using JetBrains.Application.Parts;
using JetBrains.Application.Settings;
using JetBrains.DocumentModel;
using JetBrains.ReSharper.Daemon.UsageChecking;
using JetBrains.ReSharper.Feature.Services.Daemon;
using JetBrains.ReSharper.Psi.Tree;
using Rider.Plugins.MonoGame.Mgcb.Language.Feature.Daemon.Errors;
using Rider.Plugins.MonoGame.Mgcb.Language.Parsing;
using Rider.Plugins.MonoGame.Mgcb.Language.Parsing.Tree;

namespace Rider.Plugins.MonoGame.Mgcb.Language.Feature.Daemon.Stages;

[DaemonStage(Instantiation.DemandAnyThreadUnsafe, StagesBefore = [typeof(CollectUsagesStage), typeof(GlobalFileStructureCollectorStage)],
    StagesAfter = [typeof(LanguageSpecificDaemonStage)])]
public class MgcbErrorStage(ElementProblemAnalyzerRegistrar elementProblemAnalyzerRegistrar) : MgcbStageBase
{
    protected override IDaemonStageProcess CreateProcess(IDaemonProcess process, IContextBoundSettingsStore settings,
        DaemonProcessKind processKind, IMgcbFile file)
    {
        return new MgcbErrorStageProcess(process, processKind, elementProblemAnalyzerRegistrar, settings, file);
    }

    private class MgcbErrorStageProcess : MgcbDaemonStageProcessBase
    {
        public MgcbErrorStageProcess(IDaemonProcess process, DaemonProcessKind processKind, ElementProblemAnalyzerRegistrar elementProblemAnalyzerRegistrar, IContextBoundSettingsStore settings, IMgcbFile file)
            : base(process, file)
        {
        }

        public override void VisitNode(ITreeNode element, IHighlightingConsumer consumer)
        {
            if (element is IErrorElement errorElement)
            {
                var range = GetErrorHighlightRange(errorElement);
                if (!range.IsValid())
                    range = errorElement.Parent.GetDocumentRange();
                if (range.TextRange.IsEmpty)
                {
                    if (range.TextRange.EndOffset < range.Document.GetTextLength())
                        range = range.ExtendRight(1);
                    else if (range.TextRange.StartOffset > 0)
                        range = range.ExtendLeft(1);
                }

                consumer.AddHighlighting(new MgcbSyntaxError(errorElement.ErrorDescription, range));
            }
        }

        private DocumentRange GetErrorHighlightRange(IErrorElement errorElement)
        {
            var firstMeaningful = errorElement.FirstChild;
            // skip until first meaningful
            for (; firstMeaningful != null && firstMeaningful.IsFiltered(); firstMeaningful = firstMeaningful.NextSibling) { }
            if (firstMeaningful == null)
                return errorElement.GetDocumentRange();

            var lastNode = firstMeaningful;
            for (var nextSibling = firstMeaningful.NextSibling; nextSibling != null; nextSibling = nextSibling.NextSibling)
            {
                var nodeType = nextSibling.NodeType;
                if (nodeType.Equals(MgcbTokenTypes.NEW_LINE))
                    break;
                if (!nextSibling.IsFiltered())
                    lastNode = nextSibling;
            }

            return new DocumentRange(firstMeaningful.GetDocumentStartOffset(), lastNode.GetDocumentEndOffset());
        }
    }
}
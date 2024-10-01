using JetBrains.Application.Parts;
using JetBrains.Application.Settings;
using JetBrains.ReSharper.Daemon.UsageChecking;
using JetBrains.ReSharper.Feature.Services.Daemon;
using JetBrains.ReSharper.Psi.Tree;
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
        private readonly IElementAnalyzerDispatcher myElementAnalyzerDispatcher;

        public MgcbErrorStageProcess(IDaemonProcess process, DaemonProcessKind processKind, ElementProblemAnalyzerRegistrar elementProblemAnalyzerRegistrar, IContextBoundSettingsStore settings, IMgcbFile file)
            : base(process, file)
        {
            var elementProblemAnalyzerData = new ElementProblemAnalyzerData(file, settings, ElementProblemAnalyzerRunKind.FullDaemon);
            elementProblemAnalyzerData.SetDaemonProcess(process, processKind);
            myElementAnalyzerDispatcher = elementProblemAnalyzerRegistrar.CreateDispatcher(elementProblemAnalyzerData);
        }

        public override void VisitNode(ITreeNode element, IHighlightingConsumer consumer)
        {
            myElementAnalyzerDispatcher.Run(element, consumer);
        }
    }
}
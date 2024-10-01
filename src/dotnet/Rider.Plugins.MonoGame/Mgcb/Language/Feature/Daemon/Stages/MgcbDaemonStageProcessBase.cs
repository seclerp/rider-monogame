using System;
using JetBrains.ReSharper.Feature.Services.Daemon;
using JetBrains.ReSharper.Psi;
using JetBrains.ReSharper.Psi.Tree;
using Rider.Plugins.MonoGame.Mgcb.Language.Parsing.Tree;

namespace Rider.Plugins.MonoGame.Mgcb.Language.Feature.Daemon.Stages;

public abstract class MgcbDaemonStageProcessBase : TreeNodeVisitor<IHighlightingConsumer>,
    IRecursiveElementProcessor<IHighlightingConsumer>, IDaemonStageProcess
{
    private readonly IMgcbFile myFile;

    public IDaemonProcess DaemonProcess { get; }

    protected MgcbDaemonStageProcessBase(IDaemonProcess process, IMgcbFile file)
    {
        DaemonProcess = process;
        myFile = file;
    }

    public bool IsProcessingFinished(IHighlightingConsumer context)
    {
        if (DaemonProcess.InterruptFlag)
            throw new OperationCanceledException();
        return false;
    }

    public virtual bool InteriorShouldBeProcessed(ITreeNode element, IHighlightingConsumer consumer)
    {
        return !IsProcessingFinished(consumer);
    }

    public virtual void ProcessBeforeInterior(ITreeNode element, IHighlightingConsumer consumer)
    {
    }

    public virtual void ProcessAfterInterior(ITreeNode element, IHighlightingConsumer consumer)
    {
        if (element is IMgcbTreeNode mgcbElement && !mgcbElement.IsWhitespaceToken())
            mgcbElement.Accept(this, consumer);
        else
            VisitNode(element, consumer);
    }

    public virtual void Execute(Action<DaemonStageResult> committer)
    {
        HighlightInFile((file, consumer) => file.ProcessDescendants(this, consumer), committer);
    }

    private void HighlightInFile(Action<IMgcbFile, IHighlightingConsumer> fileHighlighter,
        Action<DaemonStageResult> commiter)
    {
        var consumer = new FilteringHighlightingConsumer(DaemonProcess.SourceFile, myFile, DaemonProcess.ContextBoundSettingsStore);
        fileHighlighter(myFile, consumer);
        commiter(new DaemonStageResult(consumer.CollectHighlightings()));
    }
}
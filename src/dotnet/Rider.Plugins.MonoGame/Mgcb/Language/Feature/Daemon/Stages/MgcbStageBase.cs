using System.Collections.Generic;
using JetBrains.Application.Settings;
using JetBrains.ReSharper.Feature.Services.Daemon;
using JetBrains.ReSharper.Psi;
using JetBrains.ReSharper.Psi.Files;
using JetBrains.Util;
using Rider.Plugins.MonoGame.Mgcb.Language.Parsing.Tree;

namespace Rider.Plugins.MonoGame.Mgcb.Language.Feature.Daemon.Stages;

public abstract class MgcbStageBase : IDaemonStage
{
    public IEnumerable<IDaemonStageProcess> CreateProcess(IDaemonProcess process, IContextBoundSettingsStore settings, DaemonProcessKind processKind)
    {
        if (!IsSupported(process.SourceFile))
            return EmptyList<IDaemonStageProcess>.Instance;

        process.SourceFile.GetPsiServices().Files.AssertAllDocumentAreCommitted();

        return process.SourceFile.GetPsiFiles<MgcbLanguage>()
            .SelectNotNull(file => CreateProcess(process, settings, processKind, (IMgcbFile) file));
    }

    protected abstract IDaemonStageProcess CreateProcess(IDaemonProcess process, IContextBoundSettingsStore settings, DaemonProcessKind processKind, IMgcbFile file);

    protected virtual bool IsSupported(IPsiSourceFile sourceFile)
    {
        if (sourceFile == null || !sourceFile.IsValid())
            return false;

        var properties = sourceFile.Properties;
        if (properties.IsNonUserFile || !properties.ProvidesCodeModel)
            return false;

        return sourceFile.IsLanguageSupported<MgcbLanguage>();
    }
}
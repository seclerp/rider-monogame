using JetBrains.ReSharper.Daemon.CodeFolding;
using JetBrains.ReSharper.Psi;

namespace Rider.Plugins.MonoGame.Mgcb.Language.Feature.Daemon.CodeFolding;

[Language(typeof(MgcbLanguage))]
public class MgcbCodeFoldingProcessorFactory : ICodeFoldingProcessorFactory
{
    public ICodeFoldingProcessor CreateProcessor() => new MgcbCodeFoldingProcessor();
}
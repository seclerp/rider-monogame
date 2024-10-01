using JetBrains.ReSharper.Feature.Services.Daemon;
using JetBrains.ReSharper.Psi;

namespace Rider.Plugins.MonoGame.Mgcb.Language.Feature.Daemon.Stages;

[Language(typeof(MgcbLanguage))]
public class MgcbLanguageSpecificDaemonBehaviour : ILanguageSpecificDaemonBehavior
{
    public ErrorStripeRequestWithDescription InitialErrorStripe(IPsiSourceFile sourceFile)
    {
        if (sourceFile.PrimaryPsiLanguage.Is<MgcbLanguage>())
        {
            var properties = sourceFile.Properties;
            if (!properties.ShouldBuildPsi) return ErrorStripeRequestWithDescription.CreateNoneNoPsi(properties);
            if (!properties.ProvidesCodeModel) return ErrorStripeRequestWithDescription.CreateNoneNoCodeModel(properties);
            return ErrorStripeRequestWithDescription.StripeAndErrors;
        }

        return ErrorStripeRequestWithDescription.None("File's primary language is not ShaderLab");
    }

    public bool CanShowErrorBox => true;
    public bool RunInSolutionAnalysis => false;
    public bool RunInFindCodeIssues => true;
}
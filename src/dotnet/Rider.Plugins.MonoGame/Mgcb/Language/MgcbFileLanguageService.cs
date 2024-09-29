using JetBrains.ProjectModel;
using JetBrains.ReSharper.Feature.Services.Resources;
using JetBrains.ReSharper.Psi;
using JetBrains.ReSharper.Psi.Parsing;
using JetBrains.Text;
using JetBrains.UI.Icons;

namespace Rider.Plugins.MonoGame.Mgcb.Language;

[ProjectFileType(typeof(MgcbFileType))]
public class MgcbFileLanguageService() : ProjectFileLanguageService(MgcbFileType.Instance)
{
    public override ILexerFactory GetMixedLexerFactory(ISolution solution, IBuffer buffer, IPsiSourceFile sourceFile = null) => 
        MgcbLanguage.Instance.LanguageService()?.GetPrimaryLexerFactory();

    // ReSharper disable once AssignNullToNotNullAttribute
    protected override PsiLanguageType PsiLanguageType =>
        (PsiLanguageType) MgcbLanguage.Instance ?? UnknownLanguage.Instance;

    public override IconId Icon => ServicesNavigationThemedIcons.UsageOther.Id;
}
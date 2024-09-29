using System.Collections.Generic;
using JetBrains.Annotations;
using JetBrains.ReSharper.Psi;
using JetBrains.ReSharper.Psi.ExtensionsAPI.Caches2;
using JetBrains.ReSharper.Psi.Modules;
using JetBrains.ReSharper.Psi.Parsing;
using JetBrains.ReSharper.Psi.Tree;
using Rider.Plugins.MonoGame.Mgcb.Language.Parsing;

namespace Rider.Plugins.MonoGame.Mgcb.Language;

[Language(typeof(MgcbLanguage))]
public class MgcbLanguageService : LanguageService
{
    public MgcbLanguageService([NotNull] PsiLanguageType psiLanguageType, [NotNull] IConstantValueService constantValueService) 
        : base(psiLanguageType, constantValueService)
    {
    }

    public override ILexerFactory GetPrimaryLexerFactory()
    {
        return new MgcbLexerFactory();
    }

    public override ILexer CreateFilteringLexer(ILexer lexer)
    {
        return new MgcbFilteringLexer(lexer);
    }

    public override IParser CreateParser(ILexer lexer, IPsiModule module, IPsiSourceFile sourceFile)
    {
        return new MgcbParser();
    }

    public override IEnumerable<ITypeDeclaration> FindTypeDeclarations(IFile file)
    {
        return [];
    }

    public override ILanguageCacheProvider CacheProvider { get; }
    public override bool IsCaseSensitive => true;
    public override bool SupportTypeMemberCache { get; }
    public override ITypePresenter TypePresenter { get; }
}
using System.Collections.Generic;
using JetBrains.Annotations;
using JetBrains.ReSharper.Psi;
using JetBrains.ReSharper.Psi.ExtensionsAPI.Caches2;
using JetBrains.ReSharper.Psi.Impl;
using JetBrains.ReSharper.Psi.Modules;
using JetBrains.ReSharper.Psi.Parsing;
using JetBrains.ReSharper.Psi.Tree;
using Rider.Plugins.MonoGame.Mgcb.Language.Parsing;

namespace Rider.Plugins.MonoGame.Mgcb.Language;

[Language(typeof(MgcbLanguage))]
public class MgcbLanguageService : LanguageService
{
    public MgcbLanguageService([NotNull] MgcbLanguage psiLanguageType, [NotNull] IConstantValueService constantValueService) 
        : base(psiLanguageType, constantValueService)
    {
    }

    public override ILexerFactory GetPrimaryLexerFactory() => new MgcbLexerFactory();

    public override ILexer CreateFilteringLexer(ILexer lexer) => new MgcbFilteringLexer(lexer);

    public override IParser CreateParser(ILexer lexer, IPsiModule module, IPsiSourceFile sourceFile) => new MgcbParser(lexer);

    public override IEnumerable<ITypeDeclaration> FindTypeDeclarations(IFile file) => [];

    public override ILanguageCacheProvider CacheProvider { get; }
    public override bool IsCaseSensitive => true;

    public override bool SupportTypeMemberCache => false;
    
    public override ITypePresenter TypePresenter => DefaultTypePresenter.Instance;
}
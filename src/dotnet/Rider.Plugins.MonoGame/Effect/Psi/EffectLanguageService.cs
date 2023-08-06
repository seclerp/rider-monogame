using System.Collections.Generic;
using JetBrains.Annotations;
using JetBrains.ReSharper.Psi;
using JetBrains.ReSharper.Psi.Cpp.Parsing;
using JetBrains.ReSharper.Psi.ExtensionsAPI.Caches2;
using JetBrains.ReSharper.Psi.Modules;
using JetBrains.ReSharper.Psi.Parsing;
using JetBrains.ReSharper.Psi.Tree;
using JetBrains.Text;
using Rider.Plugins.MonoGame.Effect.Psi.Lexing;

namespace Rider.Plugins.MonoGame.Effect.Psi;

[Language(typeof(EffectLanguage))]
public class EffectLanguageServer : LanguageService
{
    public EffectLanguageServer([NotNull] PsiLanguageType psiLanguageType, [NotNull] IConstantValueService constantValueService) : base(psiLanguageType, constantValueService)
    {
    }

    public override ILexerFactory GetPrimaryLexerFactory() => new EffectLexerFactory();

    public override ILexer CreateFilteringLexer(ILexer lexer)
    {
        throw new System.NotImplementedException();
    }

    public override IParser CreateParser(ILexer lexer, IPsiModule module, IPsiSourceFile sourceFile)
    {
        throw new System.NotImplementedException();
    }

    public override IEnumerable<ITypeDeclaration> FindTypeDeclarations(IFile file)
    {
        throw new System.NotImplementedException();
    }

    public override ILanguageCacheProvider CacheProvider { get; }
    public override bool IsCaseSensitive => false;
    public override bool SupportTypeMemberCache { get; }
    public override ITypePresenter TypePresenter { get; }

    private class EffectLexerFactory : ILexerFactory
    {
        public ILexer CreateLexer(IBuffer buffer)
        {
            return new EffectLexerGenerated(buffer, CppLexer.Create);
        }
    }
}
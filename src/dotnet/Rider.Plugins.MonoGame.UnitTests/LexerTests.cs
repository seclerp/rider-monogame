using JetBrains.ReSharper.Psi.Parsing;
using JetBrains.Text;
using JetBrains.Util;
using Rider.Plugins.MonoGame.Mgcb.Language.Parsing;

namespace Rider.Plugins.MonoGame.UnitTests;

public class LexerTests
{
    [Fact]
    public void Test1()
    {
        var lexerFactory = new MgcbLexerFactory();
        var buffer = new StringBuffer("""
            # Directories
            /outputDir:bin/foo
            /intermediateDir:obj/foo
            
            /rebuild
            
            # Build a texture
            /importer:TextureImporter
            /processor:TextureProcessor
            /processorParam:ColorKeyEnabled=false
            /build:Textures\wood.png
            /build:Textures\metal.png
            /build:Textures\plastic.png
            
            $if BuildEffects=Yes
                /importer:EffectImporter
                /processor:EffectProcessor
                /build:Effects\custom.fx
                # all other effects here....
            $endif
        """);

        var lexer = lexerFactory.CreateLexer(buffer);
        var tokens = new List<(string, TokenNodeType)>();
        foreach (var pos in new EnumerableLexer<TokenNodeType>(lexer))
        {
            tokens.Add((buffer.GetText(new TextRange(pos.YyBufferStart, pos.YyBufferEnd)), pos.CurrentTokenType));
        }
    }
}
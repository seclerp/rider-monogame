using JetBrains.Text;
using Rider.Plugins.MonoGame.Mgcb.Language.Parsing;

namespace Rider.Plugins.MonoGame.UnitTests;

public class ParserTests
{
    [Fact]
    public void TestParser()
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
        var parser = new MgcbParser(lexer);
        var file = parser.ParseFile();
    }
}
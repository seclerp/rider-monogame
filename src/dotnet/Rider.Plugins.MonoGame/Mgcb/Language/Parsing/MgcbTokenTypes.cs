using JetBrains.ReSharper.Psi.Parsing;

namespace Rider.Plugins.MonoGame.Mgcb.Language.Parsing;

public static class MgcbTokenTypes
{
    private const int Offset = 0;

    public static readonly TokenNodeType OPTION = new MgcbTokenType("OPTION", Offset + 1);
    public static readonly TokenNodeType OPTION_KEY = new MgcbTokenType("OPTION_KEY", Offset + 2);
    public static readonly TokenNodeType OPTION_SEPARATOR = new MgcbTokenType("OPTION_SEPARATOR", Offset + 3);
    public static readonly TokenNodeType OPTION_VALUE = new MgcbTokenType("OPTION_VALUE", Offset + 4);
    
    public static readonly TokenNodeType IF_INSTRUCTION = new MgcbTokenType("IF_INSTRUCTION", Offset + 5);
    public static readonly TokenNodeType SET_INSTRUCTION = new MgcbTokenType("SET_INSTRUCTION", Offset + 6);
 
    public static readonly TokenNodeType IF_KEYWORD = new MgcbTokenType("IF_KEYWORD", Offset + 7);
    public static readonly TokenNodeType ENDIF_KEYWORD = new MgcbTokenType("ENDIF_KEYWORD", Offset + 8);
    public static readonly TokenNodeType SET_KEYWORD = new MgcbTokenType("SET_KEYWORD", Offset + 9);

    public static readonly TokenNodeType COMMENT = new MgcbTokenType("COMMENT", Offset + 10);
    public static readonly TokenNodeType EQ = new MgcbTokenType("EQ", Offset + 11);

    public static readonly TokenNodeType PREPROCESSOR_IDENTIFIER = new MgcbTokenType("PREPROCESSOR_IDENTIFIER", Offset + 12);
    public static readonly TokenNodeType PREPROCESSOR_VALUE = new MgcbTokenType("PREPROCESSOR_VALUE", Offset + 13);

    public static readonly TokenNodeType WHITE_SPACE = new MgcbTokenType("WHITE_SPACE", Offset + 14);
    public static readonly TokenNodeType NEW_LINE = new MgcbTokenType("NEW_LINE", Offset + 15);
}
using JetBrains.ReSharper.Psi.Parsing;

namespace Rider.Plugins.MonoGame.Mgcb.Language.Parsing;

public static class MgcbTokenTypes
{
    public class NewLineType(string representation, int index) : MgcbTokenType(representation, index)
    {
        public override bool IsWhitespace => true;
    }

    public class WhitespaceType(string representation, int index) : MgcbTokenType(representation, index)
    {
        public override bool IsWhitespace => true;
    }

    public class KeywordType(string representation, int index) : MgcbTokenType(representation, index)
    {
        public override bool IsKeyword => true;
    }
    
    public class IdentifierType(string representation, int index) : MgcbTokenType(representation, index)
    {
        public override bool IsIdentifier => true;
    }
    
    public class CommentType(string representation, int index) : MgcbTokenType(representation, index)
    {
        public override bool IsComment => true;
    }

    public class OperatorType(string representation, int index) : MgcbTokenType(representation, index);
    
    public class StringValueType(string representation, int index) : MgcbTokenType(representation, index)
    {
        public override bool IsStringLiteral => true;
    }
    
    private const int Offset = 0;

    public static readonly TokenNodeType OPTION = new MgcbTokenType("OPTION", Offset + 1);
    public static readonly TokenNodeType OPTION_KEY = new IdentifierType("OPTION_KEY", Offset + 2);
    public static readonly TokenNodeType OPTION_SEPARATOR = new OperatorType("OPTION_SEPARATOR", Offset + 3);
    public static readonly TokenNodeType OPTION_VALUE = new StringValueType("OPTION_VALUE", Offset + 4);
    
    public static readonly TokenNodeType IF_INSTRUCTION = new MgcbTokenType("IF_INSTRUCTION", Offset + 5);
    public static readonly TokenNodeType SET_INSTRUCTION = new MgcbTokenType("SET_INSTRUCTION", Offset + 6);
 
    public static readonly TokenNodeType IF_KEYWORD = new KeywordType("IF_KEYWORD", Offset + 7);
    public static readonly TokenNodeType ENDIF_KEYWORD = new KeywordType("ENDIF_KEYWORD", Offset + 8);
    public static readonly TokenNodeType SET_KEYWORD = new KeywordType("SET_KEYWORD", Offset + 9);

    public static readonly TokenNodeType COMMENT = new CommentType("COMMENT", Offset + 10);
    public static readonly TokenNodeType EQ = new OperatorType("EQ", Offset + 11);

    public static readonly TokenNodeType PREPROCESSOR_IDENTIFIER = new IdentifierType("PREPROCESSOR_IDENTIFIER", Offset + 12);
    public static readonly TokenNodeType PREPROCESSOR_VALUE = new StringValueType("PREPROCESSOR_VALUE", Offset + 13);

    public static readonly TokenNodeType WHITE_SPACE = new WhitespaceType("WHITE_SPACE", Offset + 14);
    public static readonly TokenNodeType NEW_LINE = new NewLineType("NEW_LINE", Offset + 15);
}
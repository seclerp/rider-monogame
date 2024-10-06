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

    public static readonly TokenNodeType OPTION = new MgcbTokenType("OPTION", OPTION_NODE_TYPE_INDEX);
    public const int OPTION_NODE_TYPE_INDEX = Offset + 1;

    public static readonly TokenNodeType OPTION_KEY = new IdentifierType("OPTION_KEY", OPTION_KEY_NODE_TYPE_INDEX);
    public const int OPTION_KEY_NODE_TYPE_INDEX = Offset + 2;

    public static readonly TokenNodeType OPTION_SEPARATOR = new OperatorType("OPTION_SEPARATOR", OPTION_SEPARATOR_NODE_TYPE_INDEX);
    public const int OPTION_SEPARATOR_NODE_TYPE_INDEX = Offset + 3;

    public static readonly TokenNodeType OPTION_VALUE = new StringValueType("OPTION_VALUE", OPTION_VALUE_NODE_TYPE_INDEX);
    public const int OPTION_VALUE_NODE_TYPE_INDEX = Offset + 4;

    public static readonly TokenNodeType IF_INSTRUCTION = new MgcbTokenType("IF_INSTRUCTION", IF_INSTRUCTION_NODE_TYPE_INDEX);
    public const int IF_INSTRUCTION_NODE_TYPE_INDEX = Offset + 5;

    public static readonly TokenNodeType SET_INSTRUCTION = new MgcbTokenType("SET_INSTRUCTION", SET_INSTRUCTION_NODE_TYPE_INDEX);
    public const int SET_INSTRUCTION_NODE_TYPE_INDEX = Offset + 6;

    public static readonly TokenNodeType IF_KEYWORD = new KeywordType("IF_KEYWORD", IF_KEYWORD_NODE_TYPE_INDEX);
    public const int IF_KEYWORD_NODE_TYPE_INDEX = Offset + 7;

    public static readonly TokenNodeType ENDIF_KEYWORD = new KeywordType("ENDIF_KEYWORD", ENDIF_KEYWORD_NODE_TYPE_INDEX);
    public const int ENDIF_KEYWORD_NODE_TYPE_INDEX = Offset + 8;

    public static readonly TokenNodeType SET_KEYWORD = new KeywordType("SET_KEYWORD", SET_KEYWORD_NODE_TYPE_INDEX);
    public const int SET_KEYWORD_NODE_TYPE_INDEX = Offset + 9;

    public static readonly TokenNodeType COMMENT = new CommentType("COMMENT", COMMENT_NODE_TYPE_INDEX);
    public const int COMMENT_NODE_TYPE_INDEX = Offset + 10;

    public static readonly TokenNodeType EQ = new OperatorType("EQ", EQ_NODE_TYPE_INDEX);
    public const int EQ_NODE_TYPE_INDEX = Offset + 11;

    public static readonly TokenNodeType PREPROCESSOR_IDENTIFIER = new IdentifierType("PREPROCESSOR_IDENTIFIER", PREPROCESSOR_IDENTIFIER_NODE_TYPE_INDEX);
    public const int PREPROCESSOR_IDENTIFIER_NODE_TYPE_INDEX = Offset + 12;

    public static readonly TokenNodeType PREPROCESSOR_VALUE = new StringValueType("PREPROCESSOR_VALUE", PREPROCESSOR_VALUE_NODE_TYPE_INDEX);
    public const int PREPROCESSOR_VALUE_NODE_TYPE_INDEX = Offset + 13;

    public static readonly TokenNodeType WHITE_SPACE = new WhitespaceType("WHITE_SPACE", WHITE_SPACE_NODE_TYPE_INDEX);
    public const int WHITE_SPACE_NODE_TYPE_INDEX = Offset + 14;

    public static readonly TokenNodeType NEW_LINE = new NewLineType("NEW_LINE", NEW_LINE_NODE_TYPE_INDEX);
    public const int NEW_LINE_NODE_TYPE_INDEX = Offset + 15;
}
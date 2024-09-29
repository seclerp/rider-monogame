using JetBrains.ReSharper.Psi.Parsing;
using JetBrains.Text;

namespace Rider.Plugins.MonoGame.Mgcb.Language.Parsing;

public partial class MgcbLexer : ILexer
{
    public object CurrentPosition
    {
        get =>
            new TokenPosition<TokenNodeType>(
                CurrentTokenType: myCurrentTokenType,
                YyBufferIndex: yy_buffer_index,
                YyBufferStart: yy_buffer_start,
                YyBufferEnd: yy_buffer_end,
                YyLexicalState: yy_lexical_state
            );
        set
        {
            var tokenPosition = (TokenPosition<MgcbTokenType>)value;
            myCurrentTokenType = tokenPosition.CurrentTokenType;
            yy_buffer_index = tokenPosition.YyBufferIndex;
            yy_buffer_start = tokenPosition.YyBufferStart;
            yy_buffer_end = tokenPosition.YyBufferEnd;
            yy_lexical_state = tokenPosition.YyLexicalState;
        }
    }

    public TokenNodeType TokenType
    {
        get
        {
            LocateToken();
            return myCurrentTokenType;
        }
    }

    public int TokenStart
    {
        get
        {
            LocateToken();
            return yy_buffer_start;
        }
    }

    public int TokenEnd
    {
        get
        {
            LocateToken();
            return yy_buffer_end;
        }
    }

    public IBuffer Buffer => yy_buffer;

    private TokenNodeType myCurrentTokenType = null;

    public void Start()
    {
        Start(0, yy_buffer.Length, YYINITIAL);
    }

    public void Start(int startOffset, int endOffset, uint state)
    {
        yy_buffer_index = startOffset;
        yy_buffer_start = startOffset;
        yy_buffer_end = startOffset;
        yy_eof_pos = endOffset;
        yy_lexical_state = (int)state;
        myCurrentTokenType = null;
    }

    public void Advance()
    {
        myCurrentTokenType = null;
        LocateToken();
    }
    
    private void LocateToken()
    {
        if (myCurrentTokenType == null)
        {
            myCurrentTokenType = locateToken();
        }
    }
}
using Antlr4.Runtime;
using JetBrains.ReSharper.Psi.Parsing;
using JetBrains.Text;

namespace Rider.Plugins.MonoGame.Psi.Antlr;

public class AntlrLexer : ILexer
{
    private IToken? _currentToken = null;
    private readonly Lexer _lexer;
    private readonly ITokenSource _tokenSource;

    public AntlrLexer(Lexer lexer)
    {
        _lexer = lexer;
        _tokenSource = _lexer;
    }

    public void Start()
    {
        _lexer.Reset();
    }

    public void Advance()
    {
        _currentToken = _lexer.NextToken();
    }

    public object CurrentPosition { get; set; }
    public TokenNodeType TokenType { get; }
    public int TokenStart => _currentToken?.StartIndex ?? -1;
    public int TokenEnd => _currentToken?.StopIndex ?? -1;
    public IBuffer Buffer => new AntlrBuffer(_tokenSource.InputStream);
}
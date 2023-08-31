using System.Text;
using Antlr4.Runtime;
using Antlr4.Runtime.Misc;
using JetBrains.Text;
using JetBrains.Util;

namespace Rider.Plugins.MonoGame.Psi.Antlr;

public class AntlrBuffer : IBuffer
{
    private readonly ICharStream _stream;

    public AntlrBuffer(ICharStream stream)
    {
        _stream = stream;
    }

    public string GetText()
    {
        // -1 to ensure EOF symbol is not calculated.
        return _stream.GetText(Interval.Of(0, _stream.Size - 1));
    }

    public string GetText(TextRange range)
    {
        return _stream.GetText(Interval.Of(range.StartOffset, range.EndOffset));
    }

    public void AppendTextTo(StringBuilder builder, TextRange range)
    {
        builder.Append(GetText(range));
    }

    public void CopyTo(int sourceIndex, char[] destinationArray, int destinationIndex, int length)
    {
        GetText(TextRange.FromLength(sourceIndex, length)).CopyTo(sourceIndex, destinationArray, destinationIndex, length);
    }

    public char this[int index] => GetText(TextRange.FromLength(index, 1))[0];

    public int Length => _stream.Size;
}
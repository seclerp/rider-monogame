using JetBrains.Util;

namespace Rider.Plugins.MonoGame.Mgcb.Language.Parsing;

public static class ParserMessages
{
    public const string IDS_ITEM = "item";
    public const string IDS_OPTION= "option";
    public const string IDS_IF_INSTRUCTION= "'$if' instruction";
    public const string IDS_SET_INSTRUCTION= "'$set' instruction";
    
    public static string GetString(string id) => id;

    public static string GetUnexpectedTokenMessage() => "Unexpected token";

    public static string GetExpectedMessage(string tokenRepr)
    {
        return string.Format(GetString("{0} expected"), tokenRepr).Capitalize(); // why the GetString?
    }

    public static string GetExpectedMessage(string firstExpectedSymbol, string secondExpectedSymbol)
    {
        return string.Format(GetString("{0} or {1} expected"), firstExpectedSymbol, secondExpectedSymbol).Capitalize();
    }
}
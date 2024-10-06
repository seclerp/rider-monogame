namespace Rider.Plugins.MonoGame.Mgcb.Language.Parsing.Tree.Impl;

public static class ChildRole
{
    public const short NONE = 0;

    public const short MGCB_IDENTIFIER = 1;
    public const short MGCB_VALUE = 2;
    public const short MGCB_IF_KEYWORD = 3;
    public const short MGCB_ENDIF_KEYWORD = 4;

    public const short LAST = 100;
}
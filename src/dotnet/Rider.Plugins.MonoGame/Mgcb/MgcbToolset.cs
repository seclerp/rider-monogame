using JetBrains.DataFlow;

namespace Rider.Plugins.MonoGame.Mgcb;

public class MgcbToolset<TTool> where TTool : class
{
    public IProperty<TTool> MgcbEditor { get; init;  }
    public IProperty<TTool> MgcbEditorWindows { get; init; }
    public IProperty<TTool> MgcbEditorLinux { get; init; }
    public IProperty<TTool> MgcbEditorMac { get; init; }

    public void Unset()
    {
        MgcbEditor.SetValue(null);
        MgcbEditorWindows.SetValue(null);
        MgcbEditorLinux.SetValue(null);
        MgcbEditorMac.SetValue(null);
    }
}
using JetBrains.DataFlow;

namespace Rider.Plugins.MonoGame.Mgcb;

public class MgcbToolset<TTool> where TTool : class
{
    public IProperty<TTool> Editor { get; init;  }

    public void Unset()
    {
        Editor.SetValue(null);
    }
}
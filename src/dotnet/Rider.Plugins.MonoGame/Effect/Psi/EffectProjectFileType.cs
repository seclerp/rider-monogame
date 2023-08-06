using JetBrains.Annotations;
using JetBrains.ProjectModel;

namespace Rider.Plugins.MonoGame.Effect.Psi;

[ProjectFileTypeDefinition(Name)]
public class EffectProjectFileType : KnownProjectFileType
{
    public new const string Name = "MGFX";
    public const string MGFX_EXTENSION = ".fx";

    public bool ShouldBeIndexedInExternalModule => true;

    [CanBeNull, UsedImplicitly]
    public new static EffectProjectFileType Instance { get; private set; }

    public EffectProjectFileType()
        : base(Name, "MGFX", new[] {MGFX_EXTENSION})
    {
    }
}
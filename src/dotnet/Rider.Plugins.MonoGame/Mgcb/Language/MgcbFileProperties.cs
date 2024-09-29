using JetBrains.Annotations;
using JetBrains.ProjectModel;
using JetBrains.ReSharper.Psi;
using JetBrains.ReSharper.Psi.Impl;

namespace Rider.Plugins.MonoGame.Mgcb.Language;

public class MgcbFileProperties : DefaultPsiProjectFileProperties
{
    public MgcbFileProperties([NotNull] IProjectFile projectFile, [NotNull] IPsiSourceFile sourceFile) : base(projectFile, sourceFile)
    {
    }
}
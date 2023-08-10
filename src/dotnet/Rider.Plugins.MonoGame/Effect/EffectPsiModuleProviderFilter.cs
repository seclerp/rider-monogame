using System;
using JetBrains.Lifetimes;
using JetBrains.ProjectModel;
using JetBrains.ReSharper.Psi.Modules;
using Rider.Plugins.MonoGame.Extensions;

namespace Rider.Plugins.MonoGame.Effect;

[SolutionComponent]
public class EffectPsiModuleProviderFilter : IProjectPsiModuleProviderFilter
{
    public Tuple<IProjectPsiModuleHandler, IPsiModuleDecorator> OverrideHandler(Lifetime lifetime, IProject project,
        IProjectPsiModuleHandler handler)
    {
        if (handler.PrimaryModule != null && project.IsMonoGameProject())
        {
            var module = new EffectPsiModule(project.GetSolution(), project.Name, handler.PrimaryModule.TargetFrameworkId);
            var newHandlerAndDecorator = new EffectModuleHandlerAndPsiDecorator(module, handler);
            return new Tuple<IProjectPsiModuleHandler, IPsiModuleDecorator>(newHandlerAndDecorator,
                newHandlerAndDecorator);
        }

        return null;
    }
}

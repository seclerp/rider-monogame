using JetBrains.Collections.Viewable;
using JetBrains.Lifetimes;
using JetBrains.ProjectModel;
using JetBrains.ProjectModel.NuGet.DotNetTools;
using JetBrains.RdBackend.Common.Features;
using JetBrains.Util;
using Rider.Plugins.MonoGame.Mgcb;

namespace Rider.Plugins.MonoGame;

[SolutionComponent]
public class MonoGameRdModelHost
{
    public MonoGameRdModelHost(
        Lifetime lifetime,
        ISolution solution,
        MgcbToolsTracker toolsTracker,
        ILogger logger)
    {
        var model = solution.GetProtocolSolution().GetMonoGameRiderModel();
        toolsTracker.MgcbEditorGlobalTool.FlowInto(lifetime, model.MgcbEditorToolGlobal, MapGlobalTool);
        toolsTracker.MgcbEditorSolutionTool.FlowInto(lifetime, model.MgcbEditorToolSolution, MapLocalTool);
        toolsTracker.MgcbEditorProjectTools.View(lifetime, (projectLifetime, project, toolProperty) =>
        {
            projectLifetime.Bracket(
                () =>
                {
                    var toolDefRx = new ToolDefinitionRx();
                    toolProperty.FlowInto(projectLifetime, toolDefRx.Value, MapLocalTool);
                    model.MgcbEditorToolProjects.Add(project.Guid, toolDefRx);
                },
                () =>
                {
                    model.MgcbEditorToolProjects[project.Guid].Value.Value = new ToolDefinition(string.Empty, ToolKind.None);
                    model.MgcbEditorToolProjects.Remove(project.Guid);
                });
        });
    }

    private static ToolDefinition MapLocalTool(LocalTool tool) => tool switch
    {
        null => new ToolDefinition(string.Empty, ToolKind.None),
        var other => new ToolDefinition(other.Version, ToolKind.Local)
    };

    private static ToolDefinition MapGlobalTool(GlobalToolCacheEntry tool) => tool switch
    {
        null => new ToolDefinition(string.Empty, ToolKind.None),
        var other => new ToolDefinition(other.Version.ToString(), ToolKind.Global)
    };
}
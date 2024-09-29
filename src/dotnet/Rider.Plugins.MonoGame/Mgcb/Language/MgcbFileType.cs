using System.Collections.Generic;
using JetBrains.ProjectModel;

namespace Rider.Plugins.MonoGame.Mgcb.Language;

[ProjectFileTypeDefinition(Name)]
public class MgcbFileType : KnownProjectFileType
{
    public const string Name = "MgcbFileReSharper";
    public const string PresentableName = "MGCB File (ReSharper)";

    public static MgcbFileType Instance { get; private set; }
    
    public MgcbFileType() : base(Name, PresentableName, [".mgcb2"])
    {
    }

    protected MgcbFileType(string name, string presentableName, IEnumerable<string> strings)
        : base(name, presentableName, strings)
    {
    }
}
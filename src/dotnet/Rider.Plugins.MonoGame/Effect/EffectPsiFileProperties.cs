#nullable enable
using System.Collections.Generic;
using JetBrains.ReSharper.Psi;
using JetBrains.Util;

namespace Rider.Plugins.MonoGame.Effect
{
    public class EffectPsiFileProperties : IPsiSourceFileProperties
    {
        public EffectPsiFileProperties(bool isICacheParticipant)
        {
            IsICacheParticipant = isICacheParticipant;
        }
        public bool ShouldBuildPsi => true;

        public bool IsGeneratedFile => false;

        public bool IsICacheParticipant { get; }

        public bool ProvidesCodeModel => true;

        public bool IsNonUserFile { get; init; }

        public IEnumerable<string> GetPreImportedNamespaces()
        {
            return EmptyList<string>.Instance;
        }

        public string GetDefaultNamespace() => "";

        public ICollection<PreProcessingDirective> GetDefines()
        {
            return EmptyList<PreProcessingDirective>.Instance;
        }
    }
}

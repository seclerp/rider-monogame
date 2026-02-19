<img align="left" width="128" height="128" src="/img/logo-paddings.png">
<h3>
  MonoGame plugin for JetBrains Rider
  &nbsp;&nbsp;
  <a href="https://github.com/seclerp/rider-monogame/actions/workflows/build.yml"><img src="https://github.com/seclerp/rider-monogame/actions/workflows/build.yml/badge.svg" alt="Build" align="absmiddle"></a>
</h3>

This plugin improves MonoGame usage experience inside JetBrains Rider.

<br/>

### Features

- **MGCB file autocomplete and syntax highlighting**: All supported MGCB options are properly highlighted in editor and could be autocompleted.
- **Build entries previewer**: See all your assets in a realtime tree view according to their declarations.
- **Table preview for a build entry properties**: Review build entry properties and processor parameters in a table representation.
- **"Open in external MGCB editor" action**: Jump to external MGCB editor GUI in one click.
- **Asset file templates**: Create MGCB, Spritefont and Effect files directly from Rider.
- **Spritefont editor support**: Syntax highlighting for `.spritefont` files together with XML-schema based autocompletion. 
- **New Project wizard integration**: Install & manage MonoGame templates directly from New Project wizard dialog.
---

### How to install

#### Using marketplace:

1. Go to `Settings` / `Plugins` / `Marketplace`
1. Search for "MonoGame"
1. Click `Install`, then `Save`
1. After saving restart Rider

#### Using `.zip` file
1. Go to [**Releases**](https://github.com/seclerp/rider-monogame/releases)
2. Download the latest release of plugin for your edition of JetBrains Rider (Stable or EAP)
3. Proceed to `Settings` / `Plugins` / `⚙` / `Install plugin from disk`
4. Click `Save`
5. After saving restart Rider

### How to use

Just open .mgcb file for editing. Previewer will be on the right side of the editor.

Additional file templates are located under <kbd>Add</kbd> section of a folder or project context menu.

### Requirements

- JetBrains Rider **2025.3.2+**

- Project with MonoGame installed (**3.8+ is recommended**)

### Development

> **Note**: You should have JDK 21 and .NET SDK 10.0+ installed and configured.

#### Preparing

`./gradlew rdgen` - generates RD protocol data for plugin internal communication

#### Building plugin parts

`./gradlew buildPlugin`

It will build both frontend and backend parts.

#### Running

Next command will start instance of JetBrains Rider with plugin attached to it:

`./gradlew runIde`

### Contributing

Contributions are welcome! 🎉

It's better to create an issue with description of your bug/feature before creating pull requests.

#### About branching

This project uses customized git strategy.

Each `release/*` branch plays main development branch role for specific release.

For example, `release/251` means that branch is related to `251.*` release cycle for `2025.1` Rider version.

### See also

- [**Marketplace page**](https://plugins.jetbrains.com/plugin/18415-monogame)
- [**Changelog**](CHANGELOG.md)

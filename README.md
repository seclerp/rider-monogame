<img align="left" width="128" height="128" src="/img/logo-paddings.png">
<h3>
  MonoGame plugin for JetBrains Rider
  &nbsp;
  <a href="https://github.com/seclerp/rider-monogame/actions/workflows/build.yml"><img src="https://github.com/seclerp/rider-monogame/actions/workflows/build.yml/badge.svg" alt="Build" align="absmiddle"></a>
  <a href="https://plugins.jetbrains.com/plugin/18415-monogame"><img src="https://img.shields.io/jetbrains/plugin/v/18415.svg?label=Plugin&logoColor=black&colorB=0A7BBB&logo=data:image/svg+xml;base64,PD94bWwgdmVyc2lvbj0iMS4wIiBlbmNvZGluZz0idXRmLTgiPz4KPHN2ZyB2aWV3Qm94PSIwIDAgMjQgMjQiIHdpZHRoPSIyNCIgaGVpZ2h0PSIyNCIgeG1sbnM9Imh0dHA6Ly93d3cudzMub3JnLzIwMDAvc3ZnIj4KICA8cmVjdCB3aWR0aD0iMjMuOTUxIiBoZWlnaHQ9IjIzLjk1MiIgc3R5bGU9InN0cm9rZS13aWR0aDogMHB4OyBzdHJva2U6IHJnYigyNTUsIDI1NSwgMjU1KTsgcGFpbnQtb3JkZXI6IGZpbGw7IGZpbGw6IHJnYigyNTUsIDI1NSwgMjU1KTsiLz4KICA8cGF0aCBkPSJNMCAwdjI0aDI0VjB6bTcuMDMxIDMuMTEzQTQuMDYzIDQuMDYzIDAgMCAxIDkuNzIgNC4xNGEzLjIzIDMuMjMgMCAwIDEgLjg0IDIuMjhBMy4xNiAzLjE2IDAgMCAxIDguNCA5LjU0bDIuNDYgMy42SDguMjhMNi4xMiA5LjlINC4zOHYzLjI0SDIuMTZWMy4xMmMxLjYxLS4wMDQgMy4yODEuMDA5IDQuODcxLS4wMDd6bTUuNTA5LjAwN2gzLjk2YzMuMTggMCA1LjM0IDIuMTYgNS4zNCA1LjA0IDAgMi44Mi0yLjE2IDUuMDQtNS4zNCA1LjA0aC0zLjk2em00LjA2OSAxLjk3NmMtLjYwNy4wMS0xLjIzNS4wMDQtMS44NDkuMDA0djYuMDZoMS43NGEyLjg4MiAyLjg4MiAwIDAgMCAzLjA2LTMgMi44OTcgMi44OTcgMCAwIDAtMi45NTEtMy4wNjR6TTQuMzE5IDUuMXYyLjg4SDYuNmMxLjA4IDAgMS42OC0uNiAxLjY4LTEuNDQgMC0uOTYtLjY2LTEuNDQtMS43NC0xLjQ0ek0yLjE2IDE5LjVoOVYyMWgtOVoiLz4KPC9zdmc+" alt="Version"  align="absmiddle"></a>
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

<div align="center">
  <h1>MonoGame plugin for JetBrains Rider</h1>
  <img src="img/rider-monogame-cover.png" alt="Logo">
  <br /><br />
  This plugin improves MonoGame usage experience inside JetBrains Rider.
  <br /><br />
  <a href="https://github.com/seclerp/rider-monogame/actions/workflows/build-stable.yml"><img src="https://github.com/seclerp/rider-monogame/actions/workflows/build-stable.yml/badge.svg" alt="Build (Stable)"></a>
  <!-- <a href="https://github.com/seclerp/rider-monogame/actions/workflows/build-eap.yml"><img src="https://github.com/seclerp/rider-monogame/actions/workflows/build-eap.yml/badge.svg" alt="Build (EAP)"></a> -->
</div>

---

### Features

Main features are related to MonoGame Content Pipeline files (`.mgcb`):
- Syntax highlighting
- Build options autocomplete
- Previewer with build items tree view and properties table panel

### How to install

#### Using marketplace:
<!-- > **For EAP users**: you should add `https://plugins.jetbrains.com/plugins/eap/list` to your plugin repositories list before installing -->

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

TODO

### Requirements

- JetBrains Rider **2021.3.***
<!-- or JetBrains Rider **2021.3 EAP10** -->

- Project with MonoGame installed (**3.8+ is recommended**)  

> **Note**: Projects with older versions of MonoGame might work, but with issues

### Development

> **Note**: You should have JDK 11 and .NET SDK 5.0+ installed and configured.

#### Preparing

`./gradlew rdgen` - generates RD protocol data for plugin internal communication

#### Building plugin parts

- for stable version of Rider:

  `./gradlew buildPlugin`


- for EAP version of Rider:

  `./gradlew buildPlugin -PRiderSdkVersion=2021.3.0-eap10 -PProductVersion=2021.3-EAP10-SNAPSHOT`

It will build both frontend and backend parts.

#### Running

Next command will start instance of JetBrains Rider with plugin attached to it:

`./gradlew runIde`

### Contributing

Contributions are welcome! 🎉

It's better to create an issue with description of your bug/feature before creating pull requests.

#### About branching

This project uses modified version of **trunk-based git strategy**.

- `develop` branch play main development branch role. When creating any feature or bugfix, please make your branch from `develop`.

- `master` branch stands for stable releases. `develop` branch will be merged into it before every release.
- `eap` branch stands for EAP release. `develop` branch will be also merged into it before every release (if EAP build of Rider is available to use)

### See also

- [**Marketplace page**](https://plugins.jetbrains.com/plugin/TODO)
- [**Changelog**](CHANGELOG.md)

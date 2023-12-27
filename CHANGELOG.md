# Changelog

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [Unreleased]

## [233.1.0] - 2023-12-27
### Added
- General: Support for Rider 2023.3

## [232.0.0-rc1] - 2023-07-27
### Added
- General: Support for Rider 2023.2

## [231.1.0] - 2023-07-11
### Added
- General: Support for Rider 2023.1 (#16)
- MGCB: Support for new MGCB Editor tools layout of MonoGame 3.8.1 (#13)
- MGCB Previewer: Minor style adjustments for parameters and preprocessor values tables
### Changed
- MGCB: Refactor of `mgcb-editor` tools detection logic (#14)
### Fixed
- MGCB: False-positive trigger of `Install` toolbar decorator even if tools are installed (#15)
### Removed
- MGCB: Temporary removed `Install` action due to broken behavior (#15)

## [223.0.0] - 2022-09-30
### Added
- Enable support for Rider 2022.3 EAP

## [222.0.0] - 2022-08-15
### Added
- Add support for Rider 2022.2
### Changed
- Change version numbering to reflect Rider version in it

## [1.0.1] - 2022-04-27
### Changed
- Upgrade Rider SDK to 2022.1
### Fixed
- Fix: Exception Thrown when switching solutions

## [1.0.0] - 2022-01-15
### Added
- Autocomplete and syntax highlighting
- Build entries provider
- Table view for a build entry properties
- Open in external MGCB editor action
- Additional file templates

[Unreleased]: https://github.com/seclerp/rider-monogame/compare/v233.1.0...HEAD
[233.1.0]: https://github.com/seclerp/rider-monogame/compare/v232.0.0-rc1...v233.1.0
[232.0.0-rc1]: https://github.com/seclerp/rider-monogame/compare/v231.1.0...v232.0.0-rc1
[231.1.0]: https://github.com/seclerp/rider-monogame/compare/v223.0.0...v231.1.0
[223.0.0]: https://github.com/seclerp/rider-monogame/compare/v1.0.1...v223.0.0
[222.0.0]: https://github.com/seclerp/rider-monogame/compare/v1.0.1...v222.0.0
[1.0.1]: https://github.com/seclerp/rider-monogame/compare/v1.0.0...v1.0.1
[1.0.0]: https://github.com/seclerp/rider-monogame/releases/tag/v1.0.0

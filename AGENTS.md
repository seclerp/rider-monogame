# Agent Contribution Guide

This file provides guidance for AI coding agents (Junie, Codex, Claude, and others) working in this
repository. It is the single source of truth for agent-facing conventions; tool-specific files
(e.g. `CLAUDE.md`) simply point back to this document.

## Project overview

This is a JetBrains Rider plugin that improves the MonoGame development experience. It has three
main parts:

- `src/rider` — Kotlin frontend, running inside JetBrains Rider (IntelliJ Platform plugin code:
  actions, editors, previewers, settings, file type support for `.mgcb`/`.spritefont`/`.fx`).
- `src/dotnet` — .NET backend (`Rider.Plugins.MonoGame`), running in the ReSharper Host Process,
  plus its test project `Rider.Plugins.MonoGame.Tests`.
- `protocol` — RD (Reactive Distributed) protocol definitions (Kotlin DSL) used to generate the
  communication layer between the Kotlin frontend and the .NET backend. Run `./gradlew rdgen`
  after changing anything under `protocol/` to regenerate the generated sources.

Build tooling is Gradle Kotlin DSL (`build.gradle.kts`, `gradle.properties`), using the
`org.jetbrains.intellij.platform` and `org.jetbrains.changelog` Gradle plugins.

## Verification

To verify the plugin is working after a new feature or a fix lands, `:compileKotlin` (or just
compiling the .NET project) is **not enough** — it does not catch plugin packaging, resource, or
cross-language RD protocol issues.

Always verify with:

```
./gradlew :buildPlugin
```

Use `./gradlew runIde` only when you need to manually exercise the plugin inside a running Rider
instance; it is not a substitute for `buildPlugin` verification.

## Commits

- Commits should **not** include any co-authors unless explicitly asked to.
- Keep commit messages focused on a single logical change.
- Commit messages should be concise and not too verbose.
- If a commit is related to an issue, include a separate line in the commit message:

  ```
  Fix #ISSUE_ID
  ```

## Changelog entries

Each `CHANGELOG.md` entry should follow the pattern:

```
Subsystem: description
```

If there is an issue ID:

```
Subsystem: description (#ISSUE_ID)
```

If there is an external contributor:

```
Subsystem: description (#ISSUE_ID by @USER_ID)
```

## Pull requests

- Create pull requests using the `gh` CLI (e.g. `gh pr create`).
- Title and body should be concise.
- If the PR fixes an existing issue, the body must include a separate line:

  ```
  Fix #ISSUE_ID
  ```

## Release preparation process

If asked by the user to prepare, cut, bump, or ship a release, use the `prepare-release` skill
(`.agents/skills/prepare-release/SKILL.md`), which covers fetching the target branch, computing
the next version from the changelog, bumping `gradle.properties`, verifying the build, updating
`CHANGELOG.md`, and committing — in that order.

## Architecture notes

- **Kotlin frontend** (`src/rider/main/kotlin/me/seclerp/rider/plugins/monogame`): organized by
  feature area — `mgcb` (MGCB file support: PSI, lexer/parser, previewer, settings, toolset
  detection, actions), `spritefont` (`.spritefont` XML/XSD support), `effect` (`.fx` shader file
  support), `templates` (New Project Wizard integration for MonoGame templates), `settings`
  (plugin-wide settings), `rd` (generated/handwritten RD protocol glue on the Kotlin side).
- **.NET backend** (`src/dotnet/Rider.Plugins.MonoGame`): mirrors the frontend's needs — `Mgcb`,
  `Options`, `Extensions`, `Rd` (generated RD protocol glue on the .NET side). Tests live in the
  sibling `Rider.Plugins.MonoGame.Tests` project.
- **RD protocol** (`protocol/`): the contract between frontend and backend is defined here in
  Kotlin and code-generated into both `src/rider` and `src/dotnet` via `./gradlew rdgen`. Any
  change to cross-language communication must start here.
- Generated lexer/parser sources live under `src/rider/gen` and should not be edited by hand.

## Dependencies & versions

Key versions are centralized in `gradle.properties`:

- `pluginVersion` — the plugin's own version (bumped during release prep, see above).
- `productVersion` — the target Rider platform version this plugin is built against.
- `rdVersion` / `rdKotlinVersion` — RD protocol library versions.
- `javaVersion` — JDK version used to build (kept in sync with `./gradlew jvmWrapper`-managed JDKs).
- `intellijPlatformGradleVersion` — version of the `org.jetbrains.intellij.platform` Gradle plugin.

Development requires JDK 21+ and .NET SDK 10.0+ installed and configured (see `README.md`).

## Branching

This project uses a customized git strategy: each `release/*` branch is the main development
branch for a specific Rider release cycle (e.g. `release/251` tracks the `251.*`/`2025.1` cycle).
Keep this in mind when proposing branches or targeting pull requests.

Branch naming rules:

- `feature/name` — for new features.
- `bugfix/name` — for bug fixes.
- `release/number` — major release branches targeting a specific Rider release cycle. The
  number encodes the Rider version: `262` targets Rider 2026.2, `251` targets Rider 2025.1, etc.

Names should be concise, lowercased, and use `-` as the word separator (e.g. `feature/mgcb-editor-detached-launch`).

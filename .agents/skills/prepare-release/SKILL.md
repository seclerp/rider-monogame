---
name: prepare-release
description: Prepares a new release of the rider-monogame plugin - syncs the target release branch, computes the next plugin version from the CHANGELOG's Unreleased section, bumps gradle.properties, verifies the build, updates CHANGELOG.md, and commits. Use when the user asks to prepare, cut, bump, or ship a new release/version of the plugin.
---

# Prepare Release

Step-by-step workflow for preparing a new version of the plugin. Follow the steps **in order**
and do not skip the confirmation steps — this workflow modifies version numbers and commits to
a release branch, so correctness matters more than speed.

## Versioning scheme (read this first)

`pluginVersion` (in `gradle.properties`) has the form `<cycle>.<minor>.<patch>`, e.g. `262.1.1`:

- `<cycle>` encodes the target Rider release (e.g. `262` = Rider 2026.2, `251` = Rider 2025.1).
  It matches the `release/<cycle>` branch name and the `productVersion` property. It only
  changes when the plugin starts targeting a new Rider version — this is a separate,
  intentional decision, not something this skill should ever bump on its own. If `Unreleased`
  contains a "Support for Rider X.Y" entry that implies a new cycle, ask the user to confirm
  before doing anything version-related instead of guessing.
- `<minor>.<patch>` follows [Semantic Versioning](https://semver.org/spec/v2.0.0.html) *within*
  the current cycle, mapped from [Keep a Changelog](https://keepachangelog.com/en/1.0.0/)
  categories in the `[Unreleased]` section:
  - `Added` (or any backward-incompatible change called out as such) → bump `<minor>`, reset
    `<patch>` to `0`.
  - `Changed` / `Deprecated` / `Removed` → usually `<minor>`, unless the entry itself is purely
    a fix; use judgment and confirm with the user if ambiguous.
  - `Fixed` / `Security` only → bump `<patch>`.
  - The first stable release of a new cycle conventionally starts at `<cycle>.1.0` (see e.g.
    `262.1.0`, `261.1.0` in `CHANGELOG.md`); EAP/RC pre-releases use a `-eapN` / `-rcN` suffix
    on `<cycle>.0.0` instead of the `.1.0` pattern.

## Steps

1. **Fetch recent changes.**
   - Determine the target branch: the release branch the user explicitly named, or the
     currently checked out branch otherwise.
   - Run `git status` first. If there are uncommitted changes unrelated to this workflow, stop
     and ask the user how to proceed (do not silently stash or discard anything).
   - If the target branch isn't checked out, check it out.
   - Run `git fetch origin <branch>`, then fast-forward the local branch to match
     (`git merge --ff-only origin/<branch>`, or equivalently `git pull --ff-only`).
   - If the fast-forward fails because the branches diverged, stop and ask the user how to
     reconcile — never force-push or rewrite history to work around it.

2. **Determine the next version.**
   - Read the `[Unreleased]` section of `CHANGELOG.md`.
   - If it is empty, do not guess a version. Stop and ask the user how to proceed (e.g. cancel,
     supply the changes to add first, or explicitly release with no changes).
   - Otherwise, apply the versioning scheme above to compute the candidate next
     `pluginVersion`.
   - Present the candidate version and the changes it would include to the user and get
     explicit confirmation before writing anything. If the user disagrees, apply their
     correction and confirm again.

3. **Bump the version.** Update the `pluginVersion` field in `gradle.properties` to the
   confirmed version.

4. **Verify the build.** Run `./gradlew :buildPlugin` and make sure it succeeds. `:compileKotlin`
   or compiling only the .NET project is not sufficient — it misses plugin packaging, resource,
   and cross-language RD protocol issues. If the build fails, fix the underlying issue (or ask
   the user how to proceed) before continuing; do not bump the changelog on top of a broken
   build.

5. **Update the changelog.** In `CHANGELOG.md`:
   - Turn `[Unreleased]` into a new released section titled `## [<version>] - <YYYY-MM-DD>`
     (today's date), directly followed by a fresh, empty `[Unreleased]` section above it.
   - Update the compare links at the bottom of the file:
     - `[Unreleased]` must point to `.../compare/v<version>...HEAD`.
     - Add a new `[<version>]` link comparing it against the previous released version
       (`.../compare/v<previous>...v<version>`), except when there is no previous release at
       all, in which case link straight to the tag (`.../releases/tag/v<version>`, see the
       `[1.0.0]` entry for the pattern).

6. **Commit the changes** using this strict format:

   ```
   Prepare `<version>`

   - <concise bullet describing the version bump in step 3>
   - <concise bullet describing the changelog update in step 5>
   ```

   - Stage only the files this workflow touched (`gradle.properties`, `CHANGELOG.md`, and any
     file edited to fix a build failure from step 4) — do not sweep in unrelated pending
     changes.
   - Do not add co-authors unless explicitly asked to.

## Out of scope

This skill stops after the local commit. It does **not**:

- create a git tag,
- push the commit or tag to `origin`,
- create a GitHub release or publish the plugin.

If the user wants any of those, treat it as an explicit follow-up request rather than doing it
automatically.

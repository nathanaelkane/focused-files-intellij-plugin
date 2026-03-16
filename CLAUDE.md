# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Commands

```bash
# Build the plugin
./gradlew buildPlugin

# Run tests
./gradlew check

# Run a single test class
./gradlew test --tests "com.github.nathanaelkane.focusedfiles.MyPluginTest"

# Run the IDE with the plugin loaded for manual testing
./gradlew runIde

# Verify plugin compatibility
./gradlew verifyPlugin

# Run UI tests in a separate IDE instance
./gradlew runIdeForUiTests
```

## Architecture

This is an IntelliJ Platform Plugin built with Kotlin/Gradle using the [IntelliJ Platform Plugin Template](https://github.com/JetBrains/intellij-platform-plugin-template). It currently contains scaffold/template code that needs to be replaced with the actual focused-files functionality.

**Key configuration files:**
- `gradle.properties` — plugin metadata (`pluginVersion`, `platformVersion`, `pluginSinceBuild`), platform dependencies
- `src/main/resources/META-INF/plugin.xml` — plugin registration (extensions, actions, services)
- `src/main/resources/messages/MyBundle.properties` — all user-visible strings (accessed via `MyBundle.message()`)

**Entry points registered in `plugin.xml`:**
- `MyToolWindowFactory` — creates the tool window UI panel
- `MyProjectActivity` — runs on project startup (via `postStartupActivity`)

**Service pattern:** Project-scoped services are annotated `@Service(Service.Level.PROJECT)` and retrieved via `project.service<ServiceClass>()`.

**Testing:** Tests extend `BasePlatformTestCase` (light fixture). Test data files live in `src/test/testData/` and the path is set via `@TestDataPath` + `getTestDataPath()`.

**Build/release pipeline:**
- `buildPlugin` produces a distributable ZIP in `build/distributions/`
- Plugin description is extracted from the `<!-- Plugin description -->` block in `README.md`
- Release notes are pulled from `CHANGELOG.md` (Keep a Changelog format, no section groups)
- Version channels are derived from the SemVer pre-release label (e.g. `1.0.0-beta.1` → `beta` channel)
- Signing uses env vars: `CERTIFICATE_CHAIN`, `PRIVATE_KEY`, `PRIVATE_KEY_PASSWORD`
- Publishing uses env var: `PUBLISH_TOKEN`

<img src="src/main/resources/META-INF/pluginIcon.svg" width="80" height="80" alt="icon" align="left"/>

# Focused Files

![Build](https://github.com/nathanaelkane/focused-files-intellij-plugin/workflows/Build/badge.svg)
[![Version](https://img.shields.io/jetbrains/plugin/v/30723.svg)](https://plugins.jetbrains.com/plugin/30723)
[![Downloads](https://img.shields.io/jetbrains/plugin/d/30723.svg)](https://plugins.jetbrains.com/plugin/30723)

<!-- Plugin description -->
An IntelliJ plugin to help you quickly navigate to files you've been working on in your git-versioned projects. Whether you're jumping back into a coding session or reviewing your progress before a commit, Focused Files scopes the _Navigate → File…_ popup to only the files that matter right now.

**Features:**

- **Show git modified files**: navigate to files changed since the last commit (staged and unstaged)
- **Show git branch files**: navigate to files changed on the current branch
- **Show git branch + modified files**: navigate to files from either of the above combined
- **Show open tabs**: navigate to files currently open in editor tabs

Keyboard shortcuts are unbound by default and can be assigned in _Settings → Keymap_.
<!-- Plugin description end -->

## Installation

- Using the IDE built-in plugin system:

  <kbd>Settings/Preferences</kbd> > <kbd>Plugins</kbd> > <kbd>Marketplace</kbd> > <kbd>Search for "Focused Files"</kbd> >
  <kbd>Install</kbd>

- Using JetBrains Marketplace:

  Go to [JetBrains Marketplace](https://plugins.jetbrains.com/plugin/30723) and install it by clicking the <kbd>Install to ...</kbd> button in case your IDE is running.

  You can also download the [latest release](https://plugins.jetbrains.com/plugin/30723/versions) from JetBrains Marketplace and install it manually using
  <kbd>Settings/Preferences</kbd> > <kbd>Plugins</kbd> > <kbd>⚙️</kbd> > <kbd>Install plugin from disk...</kbd>

- Manually:

  Download the [latest release](https://github.com/nathanaelkane/focused-files-intellij-plugin/releases/latest) and install it manually using
  <kbd>Settings/Preferences</kbd> > <kbd>Plugins</kbd> > <kbd>⚙️</kbd> > <kbd>Install plugin from disk...</kbd>


---
Plugin based on the [IntelliJ Platform Plugin Template][template].

[template]: https://github.com/JetBrains/intellij-platform-plugin-template
[docs:plugin-description]: https://plugins.jetbrains.com/docs/intellij/plugin-user-experience.html#plugin-description-and-presentation

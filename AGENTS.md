```markdown
# AGENTS.md - Umamusume Simulator (umasim)

This document provides an overview of the Umamusume Simulator project for AI agents and developers.

## Project Overview

The Umamusume Simulator (umasim) is a Kotlin Multiplatform project designed to simulate character training and races from the game "Umamusume: Pretty Derby". It provides tools for users to optimize training strategies and understand game mechanics better. The project includes desktop, web, and command-line interfaces.

**Author:** mee1080
**License:** AGPL v3
**Repository:** [https://github.com/mee1080/umasim](https://github.com/mee1080/umasim)

## Modules

The project is organized into several Gradle modules:

*   **`core`**:
    *   **Description:** Contains the fundamental logic for the Umamusume simulation, including training, events, and character progression. It's a Kotlin Multiplatform module targeting JVM (for desktop/CLI) and JS (for web).
    *   **Key Dependencies:** `utility`, `kotlinx-serialization-json`, `kotlinx-coroutines-core`.
*   **`utility`**:
    *   **Description:** A Kotlin Multiplatform module providing common utilities, data structures, and helper functions used across the entire project (e.g., settings management, JSON parsing wrappers). Targets JVM, JS, and Wasm.
    *   **Key Dependencies:** `multiplatform-settings`, `kotlinx-serialization-json`, `kotlinx-coroutines-core`.
*   **`jvm`**:
    *   **Description:** Contains JVM-specific code, primarily for desktop and CLI applications. This includes functionalities like file system access, database interaction (SQLite), and JVM-specific networking.
    *   **Key Dependencies:** `core`, `kotlinx-coroutines-core`, `ktor-client-cio`, `sqlite-jdbc`.
*   **`desktop`**:
    *   **Description:** Implements the Jetpack Compose for Desktop application. Provides a graphical user interface for running simulations, configuring parameters, and viewing results.
    *   **Key Dependencies:** `core`, `jvm`, `compose.desktop.currentOs`, `kotlinx-serialization-json`.
*   **`web`**:
    *   **Description:** Implements the web interface using Jetpack Compose for Web (targeting JavaScript). Allows users to interact with the simulator through a web browser.
    *   **Key Dependencies:** `utility`, `core`, `compose.html.core`, `@material/web`, `ktor-client-js`.
*   **`cli`**:
    *   **Description:** Provides a command-line interface for running simulations. Useful for scripting and automated tasks.
    *   **Key Dependencies:** `utility`, `core`, `jvm`, `clikt`.
*   **`race`**:
    *   **Description:** A Kotlin Multiplatform module dedicated to race simulation logic. Parts of this module were ported from `uma-clock-emu`. Targets JVM, JS, and Wasm.
    *   **Key Dependencies:** `utility`, `kotlinx-coroutines-core`, `kotlinx-serialization-json`.
*   **`compose`**:
    *   **Description:** A modern UI module built with Jetpack Compose, targeting JVM (desktop) and WasmJS. It appears to be a central UI component for race emulation and potentially other features, possibly intended to supersede or enhance older UI modules. Includes features for MCP server interaction.
    *   **Key Dependencies:** `utility`, `race`, Compose libraries (runtime, material3, materialIconsExtended, resources), `koalaplot-core`, `mcp` (for JVM target).
*   **`mcp`**:
    *   **Description:** Likely relates to the ModelContextProtocol (`io.modelcontextprotocol:kotlin-sdk`). This JVM module might be used for specific server interactions or interoperability, particularly with the `compose` module's MCP server version.
    *   **Key Dependencies:** `utility`, `race`, `kotlinx-coroutines-core`, `mcp` library.
*   **`chat`**:
    *   **Description:** A Kotlin Multiplatform module (targeting desktop JVM) that integrates chat functionalities, potentially using an AI agent (Gemini API key placeholder found). It seems to leverage the `:compose` module for its UI.
    *   **Key Dependencies:** `ai.koog:koog-agents`, Compose libraries, `multiplatform-settings`, `:compose`.

## Main Technologies

*   **Primary Language:** Kotlin
*   **Build System:** Gradle with Kotlin DSL (`.gradle.kts`)
*   **Architecture:** Kotlin Multiplatform (targeting JVM, JavaScript, and WebAssembly)
*   **UI Frameworks:**
    *   Jetpack Compose for Desktop
    *   Jetpack Compose for Web (HTML & WasmJS)
    *   Material Components for Web (via npm)
*   **Core Libraries:**
    *   `kotlinx-serialization`: For JSON processing.
    *   `kotlinx-coroutines`: For asynchronous programming.
    *   `Ktor Client`: For HTTP communication.
    *   `Clikt`: For command-line argument parsing.
    *   `Multiplatform Settings`: For persistent settings.
    *   `Koalaplot`: For plotting/charting in Compose UIs.
    *   `SQLite-JDBC`: For database storage in JVM environments.
    *   `MCP (ModelContextProtocol) SDK`: For specific server communication.
    *   `Koog Agents`: For AI agent integration in the `chat` module.

## Build System

The project uses Gradle with the Kotlin DSL.
*   Main build configuration: `build.gradle.kts` (root)
*   Project modules: Defined in `settings.gradle.kts`
*   Dependency versions: Managed in `gradle/libs.versions.toml`
*   Kotlin Multiplatform plugin is central to the build, configuring targets for JVM, JS (IR), and WasmJS.

## Notes for Agents

*   **Data Fetching:** The desktop application fetches game data (character info, support cards, training data) from the GitHub repository (`https://github.com/mee1080/umasim`) upon startup.
*   **Configuration:** Application settings are typically stored in `setting.json` (for the older desktop app) or using `multiplatform-settings`. The `compose` module uses `settings.conf`.
*   **Race Logic Origin:** The `race` module's logic is partially ported from `uma-clock-emu`. This might be relevant for understanding its implementation details.
*   **Modular UI:** The project has evolved its UI approach. Initially separate `desktop` and `web` modules, with the `compose` module representing a more unified and modern Compose-based UI for multiple platforms (Desktop/Wasm).
*   **Wasm Target:** The project utilizes Kotlin/Wasm for web deployment, particularly in the `compose` and `race` modules. Pay attention to Wasm-specific configurations and limitations.
*   **Building the Project:** Use the Gradle wrapper (`./gradlew` or `gradlew.bat`). Common tasks:
    *   `./gradlew build`: Builds all modules.
    *   `./gradlew :desktop:run`: Runs the desktop application.
    *   `./gradlew :compose:run`: Runs the newer Compose-based desktop application.
    *   `./gradlew :web:jsBrowserDevelopmentRun`: Runs the web application in development mode.
    *   `./gradlew :compose:wasmJsBrowserDevelopmentRun`: Runs the WasmJS compose application in development mode.
*   **Testing:** Look for test sources in `commonTest`, `jvmTest`, `desktopTest`, etc., directories within each module. Run tests using `./gradlew check` or specific test tasks like `./gradlew :core:allTests`.
*   **Dependencies:** Refer to `gradle/libs.versions.toml` for library versions and aliases used in `build.gradle.kts` files.
*   **READMEs:** Some modules have their own `README.md` or `readme.txt` files (e.g., `desktop/readme.txt`, `compose/readme.txt`) which contain specific instructions or information. The `Library/` directory also contains auto-generated READMEs about library usage for some older module structures.

This document should be updated as the project evolves.
```

# Project Information: ウマ娘育成シミュレータ (umasim)

## Project Overview

This project, "ウマ娘育成シミュレータ" (umasim), is a simulator for the game "Uma Musume Pretty Derby". It allows users to simulate training scenarios, races, and potentially optimize strategies. The project includes desktop, web, and command-line interfaces, as well as race simulation capabilities.

A significant portion of the race mechanics (`/race` directory) is ported from `uma-clock-emu` by Romulus Urakagi Tsai (@urakagi).

## Technologies Used

*   **Primary Programming Language**: Kotlin (Multiplatform, targeting JVM, JS, and potentially Native with Wasm)
*   **UI Frameworks**:
    *   Jetpack Compose for Desktop (for the desktop application)
    *   Jetpack Compose for Web (for the web application and race emulator)
    *   Material Web components are also used in the web version.
*   **Build Tool**: Gradle, using the Gradle Wrapper (`gradlew`).
*   **Key Kotlin Libraries**:
    *   Kotlinx Serialization (for JSON processing)
    *   Kotlinx Coroutines (for asynchronous programming)
    *   Ktor client (for HTTP requests, likely for fetching updated game data)

## Repository Structure

The repository is a multi-module Gradle project. Key modules include:

*   `core`: Contains the central simulation logic, data models, and core functionalities. Written in common Kotlin, making it platform-independent.
*   `compose`: Implements the race simulation UI and logic, targeting both Desktop (via `umasim.exe`) and Web (Wasm). This module can also run in an "MCP server mode".
*   `desktop`: Provides the main desktop application interface for the training simulator, built with Jetpack Compose for Desktop.
*   `web`: Contains the web application version of the simulator, built with Kotlin/JS and Jetpack Compose for Web.
*   `cli`: A command-line interface for the simulator.
*   `race`: Contains code related to race mechanics, ported from `uma-clock-emu`.
*   `jvm`: Holds JVM-specific code and utilities.
*   `utility`: Common utility functions and helpers shared across modules.
*   `mcp`: Likely related to a specific server protocol or mode for the `compose` module, enabling it to act as a server for race simulations.
*   `data/`: Contains various text files (`.txt`, `.md`, `.csv`) storing game data such as character information, skills, race details, training outcomes, scenario-specific notes, and simulation results.
*   `optimize/`: Contains Python scripts (`.py`) and batch files (`.bat`) for running optimization algorithms on the simulation data, often tailored to specific game scenarios.
*   `docs/`: Contains the built web application files, served as GitHub Pages. Includes the main web app and the Wasm-based race emulator.
*   `Library/`: Contains auto-generated markdown files listing dependencies for different modules.

## Data Sources

*   **`data/` directory**: This is the primary source for game-related data used by the simulator.
    *   Includes raw data for skills (`all_skill_info.txt`, `skill_data.txt`), characters (`chara.txt`), support cards (`support_card.txt`), races (`race.txt`), training (`training.txt`), and events (`event_track.txt`).
    *   Contains markdown files (`*_memo.md`) with notes and parameters for various in-game training scenarios (e.g., Aoharu, Grand Live, L'Arc).
    *   Stores simulation outputs in CSV format under `data/simulation/` (e.g., `guts_20241210.csv`).
*   **`optimize/` directory**: Contains Python scripts and batch files.
    *   These scripts perform optimization tasks for the simulator, likely to find the best strategies for different scenarios or character builds.
    *   Examples include `optimize_legend_red.py`, `optimize_mecha.py`, and scenario-specific optimizers like `optimize_larc.py`.
    *   Batch files are used to execute these Python scripts.
*   **External Data**: The desktop application mentions fetching updated game data (character, support card, training effects) from the GitHub repository (`https://github.com/mee1080/umasim`) upon startup.

## Build and Execution

Gradle Wrapper (`gradlew` or `gradlew.bat`) is the primary tool for building the project.

*   **Web Version (Main Application - JavaScript)**:
    *   **Build**: `gradlew web:build` (as seen in `build_web.bat`)
    *   **Run**: Open `docs/index.html` in a web browser after the build (files are copied to `docs/`).
*   **Web Version (Race Emulator - WebAssembly)**:
    *   **Build**: `gradlew compose:wasmJsBrowserDistribution` (as seen in `build_wasm.bat`)
    *   **Run**: Open `docs/race/index.html` in a web browser after the build (files are copied to `docs/race/`).
*   **Desktop Version (`umasim.exe`)**:
    *   **Build (Package Executable)**: `gradlew compose:packageExe` (or similar like `compose:packageDistribution`)
    *   **Run (Packaged)**: Execute `umasim.exe` from the build output directory (e.g., `compose/build/compose/binaries/main/app/umasim.exe`).
    *   **Run (Development)**: `gradlew compose:run`
*   **CLI Version**:
    *   **Build (Distributable)**: `gradlew cli:assembleDist` or `gradlew cli:installDist`
    *   **Run (Development)**: `gradlew cli:run --args="<arguments>"`
*   **MCP Server Version**:
    *   **Build**: Build `umasim.exe` using the desktop version instructions (e.g., `gradlew compose:packageExe`).
    *   **Run**: Execute `path/to/umasim.exe mcp`.

## License

This project is licensed under the **AGPL v3**.

## Author & Contact

*   **Author**: mee1080
*   **Contact**: X (Twitter) @mee10801 (for feedback, feature requests, bug reports)

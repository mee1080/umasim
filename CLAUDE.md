# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

モジュール一覧・実装ルール・シナリオ実装手順は AGENTS.md にまとめてあるので、そちらを必ず参照すること。

@AGENTS.md

## よく使うコマンド

開発環境は Windows（`gradlew.bat` / Bash からは `./gradlew`）。JVM ターゲットは 17（`gradle/libs.versions.toml` の `jvmTarget`）。

```bash
./gradlew :core:jvmTest                                   # core のテスト（JVM）
./gradlew :core:jvmTest --tests "io.github.mee1080.umasim.scenario.ramen.RamenCalculatorTest"   # 単一テストクラス
./gradlew :compose:run                                    # レースエミュレータ（Compose Desktop）
./gradlew :desktop:run                                    # 育成シミュレータ（旧デスクトップ）
./gradlew :compose:createDistributable                    # 配布用デスクトップアプリ（GitHub Actions のリリースで使用）
```

テストは AGENTS.md のとおり指示がない限り作成・実行不要。

## 公開用ビルド（docs/）

`docs/` は GitHub Pages 用のビルド成果物で、手編集しない。以下のバッチで生成・上書きされる。

* `build_web.bat`: `web:build` の成果物で `docs/` 全体を置き換える
* `build_wasm.bat`: `compose:wasmJsBrowserDistribution` の成果物を `docs/race` と `web/src/jsMain/resources/race` にコピー（Web版の中にレースエミュレータを同梱）

## アーキテクチャ概要

### マスターデータの読み込み

* 生データはリポジトリ直下の `data/*.txt`（タブ区切り）。`chara.txt` / `support_card.txt` / `team_member.txt` / `goal_race.txt` / `race.txt` を `core` の `Store.load(...)` に文字列で渡し、各 `XxxLoader` が遅延パースする。
* JVM: `jvm` モジュールの `StoreLoader` が `../data/` を読み、無ければ GitHub の raw URL から取得する。テストも `../data/` を直接読む（`core/src/jvmTest/.../TestStoreLoader.desktop.kt`）。
* Web(JS): `web/src/jsMain/.../data/Source.kt` にデータ文字列が埋め込まれている。`data/` を更新しても Web 版には自動反映されない。
* `data/*_memo.md` はシナリオ調査メモ（新シナリオは `scenario_memo_template.md` から作成）。

### 育成シミュレーション（core / simulation2）

* `Simulator` / `Runner` が 1 育成を回すエントリポイント。各ターンで選択可能な `Action` を列挙し、`ActionSelector`（`core/.../ai/` に各シナリオ用実装）が行動を選択、`SimulationStateUpdater` が `SimulationState` に反映する。
* 上昇量計算は `Calculator`、行動結果の予測は `ActionPredictor`、評価は `Evaluator`。
* シナリオ差分は `Scenario` enum に集約されている。各エントリが `trainingData`・`scenarioEvents`・`calculator`（`ScenarioCalculator` 実装）などを持ち、共通処理はこれらのフックを呼ぶ形でシナリオ固有処理に委譲する。シナリオ固有の状態は `SimulationState.scenarioStatus`（`ScenarioStatus` 実装）。
* `cli` モジュールは育成シミュレーションをバッチ実行するためのもの（jar の Main は `RamenCliKt`）。`optimize/` の Python（Optuna）からパラメータ探索に使われる。

### レースシミュレーション（race / compose / mcp）

* `race` モジュールはレース計算ロジック（`calc2/RaceCalculator`, `RaceState`, `SkillChecker`）とスキル等のデータ。uma-clock-emu からの移植を含む。
* `compose` は Desktop と WasmJS 共通のレースエミュレータ UI。`mcp` は `race` を MCP サーバー（stdio / SSE）として公開する。

package io.github.mee1080.umasim.scenario

import io.github.mee1080.umasim.data.*
import io.github.mee1080.umasim.simulation2.*
import io.github.mee1080.umasim.simulation2.Calculator.CalcInfo

/**
 * シナリオ固有の計算や動作を制御するためのインターフェース。
 *
 * 各シナリオ（URA, アオハル, L'Arcなど）はこのインターフェースを実装し、
 * トレーニング上昇値の計算、固有アクションの追加、ターン更新時の処理などを行います。
 */
interface ScenarioCalculator {

    object Default : ScenarioCalculator

    /**
     * トレーニングによるシナリオ固有のステータス上昇量（UIの上段に表示される値）を計算します。
     * [io.github.mee1080.umasim.simulation2.Calculator.calcTrainingSuccessStatusSeparated] から呼び出されます。
     *
     * @param info トレーニングの計算に必要な情報（キャラ、サポカ配置、シナリオステータスなど）
     * @param base 基礎ステータス上昇量（UIの下段に表示される値）
     * @param raw 基礎ステータス上昇量の小数値
     * @param friendTraining 友情トレーニングが発生しているかどうか
     * @return シナリオ固有のステータス上昇量。デフォルトは 0
     */
    fun calcScenarioStatus(
        info: CalcInfo,
        base: Status,
        raw: ExpectedStatus,
        friendTraining: Boolean,
    ): Status = Status()

    /**
     * シナリオ固有のレース上昇値を計算します。
     * [io.github.mee1080.umasim.simulation2.ActionPredictor.predictRace] から呼び出されます。
     * 主に目標レース（goal = true）での獲得ステータスを制御するために使用されます。
     *
     * @param state 現在のシミュレーション状態
     * @param race 出走するレース情報
     * @param goal 目標レースかどうか
     * @return シナリオ固有の上昇値。nullを返すとデフォルトの計算が使用されます。
     */
    fun calcBaseRaceStatus(
        state: SimulationState,
        race: RaceEntry,
        goal: Boolean,
    ): Status? = null

    /**
     * シナリオ固有のレースボーナスを適用します。
     * [io.github.mee1080.umasim.simulation2.ActionPredictor.predictRace] 内で、
     * 計算されたレース上昇値に対して最後に適用されます。
     *
     * @param state 現在のシミュレーション状態
     * @param base 適用前のステータス
     * @return ボーナス適用後のステータス
     */
    fun applyScenarioRaceBonus(
        state: SimulationState,
        base: Status,
    ): Status = base

    /**
     * レース実行時に追加されるシナリオ固有の効果（ゲージ上昇など）を定義します。
     * [io.github.mee1080.umasim.simulation2.ActionPredictor.predictRace] から呼び出されます。
     * [predictScenarioActionParams] でも代用可能ですが、レース固有のパラメータが必要な場合に使用します。
     *
     * @param state 現在のシミュレーション状態
     * @param race レース情報
     * @param goal 目標レースかどうか
     * @return 追加されるシナリオアクションパラメータ。ない場合は null
     */
    fun raceScenarioActionParam(
        state: SimulationState,
        race: RaceEntry,
        goal: Boolean,
    ): ScenarioActionParam? = null

    /**
     * シナリオ固有のお休み/お出かけ効果を定義します。
     * [io.github.mee1080.umasim.simulation2.ActionPredictor.predictSleep] から呼び出されます。
     * nullを返すとデフォルトのお休み（HP+30〜70、やる気アップなど）が提供されます。
     *
     * @param state 現在のシミュレーション状態
     * @return 提供するアクションの配列。nullの場合はデフォルト。
     */
    fun predictSleep(
        state: SimulationState,
    ): Array<Action>? = null

    /**
     * 通常のアクション（トレーニング、レース、お休みなど）にシナリオ固有のパラメータを付与します。
     * [io.github.mee1080.umasim.simulation2.ActionPredictor.predict] および [io.github.mee1080.umasim.simulation2.ActionPredictor.predictNormal] から呼び出されます。
     * 各アクションの実行結果に、ゲージ増加などのシナリオ固有の効果（[ScenarioActionParam]）を紐付けるために使用します。
     *
     * @param state 現在のシミュレーション状態
     * @param baseActions 元のアクションリスト
     * @return パラメータが付与されたアクションリスト
     */
    fun predictScenarioActionParams(
        state: SimulationState,
        baseActions: List<Action>,
    ): List<Action> = baseActions

    /**
     * シナリオ固有の独立したアクションを追加します。
     * [io.github.mee1080.umasim.simulation2.ActionPredictor.predictGoal] および [io.github.mee1080.umasim.simulation2.ActionPredictor.predictNormal] から呼び出されます。
     * 海外適性の習得、料理の作成、女神の叡智の発動など、通常メニューとは別のアクションを提供するために使用します。
     *
     * @param state 現在のシミュレーション状態
     * @param goal 目標レースがあるターンかどうか
     * @return 追加するアクションの配列
     */
    fun predictScenarioAction(
        state: SimulationState,
        goal: Boolean,
    ): Array<Action> = emptyArray()

    /**
     * 通常レースへの出走を禁止するかどうかを返します。
     * [io.github.mee1080.umasim.simulation2.ActionPredictor.predictNormal] から呼び出されます。
     * 海外遠征中など、通常のレース選択肢を表示したくない場合に true を返します。
     *
     * @param state 現在のシミュレーション状態
     * @return 出走禁止なら true
     */
    fun normalRaceBlocked(
        state: SimulationState,
    ): Boolean = false

    /**
     * ターン更新時のシナリオ固有処理を行います。
     * [io.github.mee1080.umasim.simulation2.SimulationStateUpdater.onTurnChange] から呼び出されます。
     * 毎ターンの開始時に、ゲージの減少、ランダムな状態変化、シナリオ変数の更新などを行うために使用します。
     *
     * @param state 更新前のシミュレーション状態
     * @return 更新後のシミュレーション状態
     */
    fun updateScenarioTurn(
        state: SimulationState,
    ): SimulationState = state

    /**
     * ステータスが上昇した際のシナリオ固有処理を行います。
     * [io.github.mee1080.umasim.simulation2.SimulationStateUpdater.addStatus] から呼び出されます。
     * 特定のステータス上昇をきっかけにシナリオ固有のポイントに変換したり（Twinkle Legendsなど）、
     * やる気が最大を超えた際の処理などを行うために使用します。
     *
     * @param state 更新前のシミュレーション状態
     * @param status 加算されるステータス
     * @return 更新後のシミュレーション状態
     */
    fun updateOnAddStatus(
        state: SimulationState,
        status: Status,
    ): SimulationState = state

    /**
     * トレーニングの基本データをシナリオ固有のものに差し替えます。
     * [io.github.mee1080.umasim.simulation2.ActionPredictor.calcTrainingResult] から呼び出されます。
     * U.A.F.のように、シナリオの進行状況に応じてトレーニングの種類やLvが変化する場合に使用します。
     *
     * @param state 現在のシミュレーション状態
     * @param trainingType 対象のトレーニングタイプ
     * @return 差し替え後のトレーニングデータ。差し替えない場合は null
     */
    fun getTraining(
        state: SimulationState,
        trainingType: StatusType,
    ): TrainingBase? = null

    /**
     * トレーニングの基礎ステータス（UIの下段）の計算に反映するボーナスを返します。
     * [io.github.mee1080.umasim.simulation2.ActionPredictor.calcTrainingResult] 内で [io.github.mee1080.umasim.simulation2.Calculator.calcTrainingSuccessStatusAndFriendEnabled] に渡されます。
     * モチベーションボーナスやトレーニング効果、友情ボーナスの乗算係数などを調整できます。
     *
     * @param baseInfo トレーニングの計算情報
     * @return 反映するボーナス。ない場合は null
     */
    fun getScenarioCalcBonus(
        baseInfo: CalcInfo,
    ): Calculator.ScenarioCalcBonus? = null

    /**
     * トレーニングの基礎ステータス（UIの下段）の結果を最終的に修正します。
     * [io.github.mee1080.umasim.simulation2.Calculator.calcTrainingSuccessStatusSeparated] から呼び出されます。
     * 特定のステータス上昇量に上限を設けたり、特定の条件下で一律の倍率をかけるなどの最終調整に使用します。
     *
     * @param info トレーニングの計算情報
     * @param base 計算された基礎ステータス
     * @param raw 計算された基礎ステータスの小数値
     * @param friendTraining 友情トレーニングが発生しているかどうか
     * @return 修正後の基礎ステータス
     */
    fun modifyBaseStatus(
        info: CalcInfo,
        base: Status,
        raw: ExpectedStatus,
        friendTraining: Boolean,
    ): Status = base

    /**
     * 得意率アップのシナリオ補正値を返します。
     * [io.github.mee1080.umasim.simulation2.SimulationState.specialityRateUp] を通じて、サポカの配置決定時に使用されます。
     * サポカの種類（スピード、スタミナなど）ごとに、得意トレーニングへの配置確率を底上げできます。
     *
     * @param state 現在のシミュレーション状態
     * @param cardType 対象のサポカタイプ
     * @return 得意率の加算値
     */
    fun getSpecialityRateUp(
        state: SimulationState,
        cardType: StatusType,
    ): Int = 0

    /**
     * 配置率アップのシナリオ補正値を返します。
     * [io.github.mee1080.umasim.simulation2.SimulationState.positionRateUp] を通じて、[io.github.mee1080.umasim.simulation2.Calculator.calcCardPositionSelection] で使用されます。
     * サポカが「どこにも配置されない（NONE）」確率を減少させ、いずれかのトレーニングに出現しやすくします。
     *
     * @param state 現在のシミュレーション状態
     * @return 配置率の向上値
     */
    fun getPositionRateUp(
        state: SimulationState,
    ): Int = 0

    /**
     * ヒント発生率アップのシナリオ補正値を返します。
     * [io.github.mee1080.umasim.simulation2.SimulationState.hintFrequencyUp] を通じて、ターン更新時のヒントアイコン表示判定に使用されます。
     *
     * @param state 現在のシミュレーション状態
     * @param position 配置されているトレーニング
     * @return ヒント発生率の加算値
     */
    fun getHintFrequencyUp(
        state: SimulationState,
        position: StatusType,
    ): Int = 0

    /**
     * 全てのサポートカードでヒントが発生する状態かどうかを返します。
     * [io.github.mee1080.umasim.simulation2.SimulationState.allSupportHint] を通じて、[io.github.mee1080.umasim.simulation2.SimulationStateUpdater.selectTrainingHint] で使用されます。
     * trueを返すと、そのトレーニングに配置されている全てのサポカからヒント（スキル等）を獲得できます。
     *
     * @param state 現在のシミュレーション状態
     * @param position 対象のトレーニング
     * @return 全員ヒント発生なら true
     */
    fun isAllSupportHint(
        state: SimulationState,
        position: StatusType
    ): Boolean = false

    /**
     * 失敗率低下のシナリオ補正値を返します。
     * [io.github.mee1080.umasim.simulation2.ActionPredictor.calcTrainingFailureRate] で使用されます。
     *
     * @param state 現在のシミュレーション状態
     * @return 失敗率を何パーセント軽減するか（例: 20 を返すと失敗率が 0.8 倍になる）
     */
    fun getFailureRateDown(
        state: SimulationState,
    ): Int = 0

    /**
     * 体力消費軽減のシナリオ補正値を返します。
     * [io.github.mee1080.umasim.simulation2.Calculator.calcTrainingHp] で使用されます。
     *
     * @param scenarioStatus 現在のシナリオステータス
     * @return 消費体力を何パーセント軽減するか
     */
    fun getHpCostDown(
        scenarioStatus: ScenarioStatus,
    ): Int = 0

    /**
     * 追加で配置されるメンバー数を返します。
     * [io.github.mee1080.umasim.simulation2.SimulationState.additionalMemberCount] を通じて、[io.github.mee1080.umasim.simulation2.SimulationStateUpdater.shuffleMember] で使用されます。
     * 同一サポカを複数のトレーニングに同時に出現させたい場合などに使用します。
     *
     * @param state 現在のシミュレーション状態
     * @return 追加配置する人数
     */
    fun getAdditionalMemberCount(
        state: SimulationState,
    ): Int = 0

    /**
     * トレーニングによる絆上昇量のボーナス値を返します。
     * [io.github.mee1080.umasim.simulation2.SimulationState.trainingRelationBonus] を通じて、[io.github.mee1080.umasim.simulation2.SimulationStateUpdater.applyStatusAction] で使用されます。
     *
     * @param state 現在のシミュレーション状態
     * @return 絆上昇量の加算値
     */
    fun getTrainingRelationBonus(
        state: SimulationState,
    ): Int = 0

    /**
     * ヒントアイコンが表示される最低人数（保証値）を返します。
     * [io.github.mee1080.umasim.simulation2.SimulationState.forceHintCount] を通じて、[io.github.mee1080.umasim.simulation2.SimulationStateUpdater.shuffleMember] で使用されます。
     *
     * @param state 現在のシミュレーション状態
     * @return ヒント発生を保証するサポカ数
     */
    fun getForceHintCount(
        state: SimulationState,
    ): Int = 0

    /**
     * シャッフルされたメンバーの配置を最終的に調整します。
     * [io.github.mee1080.umasim.simulation2.SimulationStateUpdater.shuffleMember] の最後（付近）で呼び出されます。
     * シナリオ固有のキャラクターを強制的に特定のトレーニングに配置したり、条件に基づいて配置を上書きするために使用します。
     *
     * @param state 現在のシミュレーション状態
     * @param member シャッフル後のメンバーリスト
     * @return 調整後のメンバーリスト
     */
    fun modifyShuffledMember(
        state: SimulationState,
        member: List<MemberState>,
    ): List<MemberState> = member
}

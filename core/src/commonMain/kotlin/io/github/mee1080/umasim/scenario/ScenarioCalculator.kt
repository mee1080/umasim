package io.github.mee1080.umasim.scenario

import io.github.mee1080.umasim.data.*
import io.github.mee1080.umasim.simulation2.Action
import io.github.mee1080.umasim.simulation2.Calculator
import io.github.mee1080.umasim.simulation2.Calculator.CalcInfo
import io.github.mee1080.umasim.simulation2.ScenarioActionParam
import io.github.mee1080.umasim.simulation2.SimulationState

interface ScenarioCalculator {

    object Default : ScenarioCalculator

    /**
     * トレーニングの上段値
     */
    fun calcScenarioStatus(
        info: CalcInfo,
        base: Status,
        raw: ExpectedStatus,
        friendTraining: Boolean,
    ): Status = Status()

    /**
     * シナリオ固有のレース上昇値
     */
    fun calcBaseRaceStatus(
        state: SimulationState,
        race: RaceEntry,
        goal: Boolean,
    ): Status? = null

    /**
     * シナリオ固有のレースボーナス
     */
    fun applyScenarioRaceBonus(
        state: SimulationState,
        base: Status,
    ): Status = base

    /**
     * シナリオでレースに追加する効果（predictScenarioActionParamsで代用可能）
     */
    fun raceScenarioActionParam(
        state: SimulationState,
        race: RaceEntry,
        goal: Boolean,
    ): ScenarioActionParam? = null

    /**
     * シナリオ固有のお休み効果
     */
    fun predictSleep(
        state: SimulationState,
    ): Array<Action>? = null

    /**
     * シナリオで通常アクションに追加する効果
     */
    fun predictScenarioActionParams(
        state: SimulationState,
        baseActions: List<Action>,
    ): List<Action> = baseActions

    /**
     * シナリオ固有アクション
     */
    fun predictScenarioAction(
        state: SimulationState,
        goal: Boolean,
    ): Array<Action> = emptyArray()

    /**
     * 通常レース出走禁止（海外遠征など）
     */
    fun normalRaceBlocked(
        state: SimulationState,
    ): Boolean = false

    /**
     * ターン更新時の処理
     */
    fun updateScenarioTurn(
        state: SimulationState,
    ): SimulationState = state

    /**
     * ステータス上昇時の処理（やる気上昇時の超絶好調など）
     */
    fun updateOnAddStatus(
        state: SimulationState,
        status: Status,
    ): SimulationState = state

    /**
     * スピ～賢さのシナリオ特有のトレーニング（UAFの各トレーニング、島合宿など）
     */
    fun getTraining(
        state: SimulationState,
        trainingType: StatusType,
    ): TrainingBase? = null

    /**
     * トレーニングの下段の計算に反映するボーナス
     */
    fun getScenarioCalcBonus(
        state: SimulationState,
        baseInfo: CalcInfo,
    ): Calculator.ScenarioCalcBonus? = null

    /**
     * 得意率アップ
     */
    fun getSpecialityRateUp(
        state: SimulationState,
        cardType: StatusType,
    ): Int = 0

    /**
     * 配置率アップ
     */
    fun getPositionRateUp(
        state: SimulationState,
    ): Int = 0

    /**
     * ヒント率アップ
     */
    fun getHintFrequencyUp(
        state: SimulationState,
        position: StatusType,
    ): Int = 0

    /**
     * 全てのヒントイベント発生
     */
    fun isAllSupportHint(
        state: SimulationState,
        position: StatusType
    ): Boolean = false
}

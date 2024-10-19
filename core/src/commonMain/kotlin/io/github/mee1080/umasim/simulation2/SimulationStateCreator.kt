package io.github.mee1080.umasim.simulation2

import io.github.mee1080.umasim.data.*

fun SupportCard.toMemberState(
    scenario: Scenario,
    index: Int,
    relation: Int? = null,
    passionTurn: Int = 0,
    friendCount: Int = 0,
) = MemberState(
    index = index,
    card = this,
    position = StatusType.NONE,
    supportState = SupportState(
        relation = relation ?: initialRelation,
        hintIcon = false,
        passionTurn = passionTurn,
        friendCount = friendCount,
        outingStep = 0,
    ),
    scenarioState = scenario.memberState(this),
)

fun List<SupportCard>.toMemberState(scenario: Scenario) =
    mapIndexed { index, target -> target.toMemberState(scenario, index) }

fun TeamMemberData.toMemberState(scenario: Scenario, index: Int) = MemberState(
    index = index,
    card = Store.getSupport(supportCardId, 0),
    position = StatusType.NONE,
    supportState = null,
    scenarioState = scenario.memberState(this),
)

fun createTeamMemberState(count: Int, scenario: Scenario) = Store.Aoharu.getShuffledGuest().subList(0, count)
    .mapIndexed { index, member -> member.toMemberState(scenario, index) }
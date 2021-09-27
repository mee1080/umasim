package io.github.mee1080.umasim.simulation2

import io.github.mee1080.umasim.data.Status

class SimulationEvents(
    val beforeSimulation: ((state: SimulationState) -> SimulationState) = { it },
    val initialStatus: ((status: Status) -> Status) = { it },
    val beforeAction: ((state: SimulationState) -> SimulationState) = { it },
    val afterAction: ((state: SimulationState) -> SimulationState) = { it }
)
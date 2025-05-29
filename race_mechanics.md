# General Race Flow

This document outlines the mechanics of a race, primarily managed by the `RaceCalculator` class.

## Race Initialization

The race begins with an initialization step. This is handled by the `RaceCalculator.initializeState` method.
This method sets up the initial conditions for the race, such as:
- Positioning of racers.
- Initial speeds and other racer-specific attributes.
- Resetting any state from previous races.

## Race Progression

The race progresses on a frame-by-frame basis. Each frame represents a small time step in the race.

1.  **`RaceCalculator.progressRace`**: This method is the main engine for advancing the race. It's likely called repeatedly (e.g., in a game loop) to move the simulation forward. It orchestrates the updates for a single frame.
2.  **`RaceCalculator.updateFrame`**: Called by `progressRace`, this method handles the detailed logic for a single frame update. This could involve:
    *   Updating racer positions based on their current speed and acceleration.
    *   Handling inputs or AI for racer actions.
    *   Detecting collisions or other interactions.
    *   Applying physics or game rules.

This cycle of `progressRace` calling `updateFrame` continues, advancing the race state with each iteration.

## Finishing the Race

The race concludes when certain conditions are met, which are checked by the `RaceCalculator.goal` method.
This method likely evaluates if one or more racers have:
- Crossed a designated finish line.
- Completed a required number of laps.
- Met any other objective defined by the race rules.

Once `RaceCalculator.goal` indicates that the finishing conditions are satisfied for a racer, that racer has completed the race. The race might continue for other racers until they also meet the goal or a timeout occurs.

# Character Stats (UmaStatus)

The `UmaStatus` data class, defined in `RaceState.kt`, holds the core attributes of a character participating in a race. These stats significantly influence the character's performance during the simulation.

-   **`charaName` (String)**: The name of the character. This is primarily for identification and display, and does not directly influence race calculations.
-   **`speed` (Int)**: Represents the character's base speed. A higher speed stat generally leads to a higher maximum speed and target speeds during different phases of the race. It's a fundamental factor in `modifiedSpeed` calculations, which affects target speed and maximum spurt speed.
-   **`stamina` (Int)**: Dictates the character's endurance. Stamina is crucial for maintaining speed over long distances and for the total amount of "SP" (Stamina Points) a character has (`spMax`). Insufficient SP can lead to significant slowdowns. `modifiedStamina` is used to calculate `spMax`.
-   **`power` (Int)**: Influences acceleration, ability to navigate slopes, and lane changing. Higher power (`modifiedPower`) results in better acceleration (`acceleration` calculation in `RaceState`), reduced speed loss on uphills, and contributes to lane changing speed (`baseLaneChangeTargetSpeed`). It also plays a role in "position competition" speed.
-   **`guts` (Int)**: Affects performance in the later stages of the race, particularly during "last spurt" scenarios and "compete fight" situations. `modifiedGuts` influences `spurtSpCoef` (SP consumption during spurt), `leadCompetitionSpeed`, `competeFightSpeed`, `competeFightAcceleration`, and `secureLeadSpeed`. It also contributes to the minimum base speed (`vMinBase`).
-   **`wisdom` (Int)**: Impacts skill activation rates, pace control, and resistance to negative status effects like "temptation." `modifiedWisdom` influences `skillActivateRate`, the randomness factor in target speeds (`initSectionTargetSpeedRandoms`), `temptationRate`, and effectiveness of position keeping (`positionKeepSpeedUpOvertakeRate`, `positionKeepPaceUpRate`).
-   **`condition` (Condition)**: An enum representing the character's current form (e.g., BEST, GOOD, BAD). This acts as a multiplier (`condCoef`) to the main stats (speed, stamina, power, guts, wisdom) when calculating their "modified" values for the race. Better conditions provide a boost to effective stats.
-   **`style` (Style)**: The character's preferred racing style (e.g., NIGE - Leader, SEN - Front, SASI - Insert, OI - Chaser). This determines base speed coefficients (`styleSpeedCoef`), acceleration coefficients (`styleAccelerateCoef`), and SP coefficients (`styleSpCoef`) for different phases of the race. It also influences position keeping logic and other style-specific mechanics like "Oonige" (Great Leader).
-   **`distanceFit` (FitRank)**: Represents how well the character is suited for the race's distance (e.g., S, A, B). This rank provides coefficients (`distanceFitSpeedCoef`, `distanceFitAccelerateCoef`) that modify speed and acceleration. A better fit means better performance at that distance.
-   **`surfaceFit` (FitRank)**: Indicates suitability for the track surface (e.g., Turf, Dirt). This rank provides coefficients (`surfaceFitAccelerateCoef`) that modify acceleration. It also affects speed (`surfaceSpeedModify`) and power (`surfacePowerModify`) based on track surface and condition.
-   **`styleFit` (FitRank)**: Shows compatibility with their chosen racing style. This rank provides a coefficient (`styleFitCoef`) that modifies wisdom, potentially affecting skill activation and other wisdom-related calculations.
-   **`popularity` (Int)**: The character's popularity. While present in `UmaStatus`, its direct impact on core race mechanics like speed, stamina, or skill activation isn't immediately obvious from `RaceCalculator.kt` or `RaceState.kt` snippets, suggesting it might be used for other systems (e.g., event triggers, rewards) or its influence is more subtle.
-   **`gateNumber` (Int)**: The starting gate assigned to the character. This determines the initial lane (`initialLane` in `RaceCalculator.initializeState`) and post position (`postNumber`). A value of 0 means a random gate, -1 and -2 seem to imply preferential random assignment to inner/outer gates respectively.
-   **`hasSkills` (List<SkillData>)**: A list of skills the character possesses. Skills are a major factor, providing various bonuses (passive stat boosts, temporary speed/acceleration increases, healing, debuffs, etc.). `RaceCalculator.invokeSkills()` processes these, and their effects are applied throughout the race based on their conditions and logic defined in `SkillData` and checked in `RaceState`.
-   **`uniqueLevel` (Int)**: The level of the character's unique skill. This typically enhances the effects or improves the activation conditions of their unique skill, as applied in `RaceCalculator.invokeSkills()` where unique skills are leveled up.

# Track Information

Race tracks are defined by a combination of data structures primarily found in `RaceState.kt` and `trackData.kt`. The specific details for each course are loaded from `rawCourseData` (a JSON string within `rawData.kt`) into a map called `trackData`.

## `Track` Class (defined in `RaceState.kt`)

This class represents the specific track instance selected for a race.

-   **`location` (Int)**: An ID representing the general racecourse location (e.g., Tokyo, Kyoto, Nakayama). This ID is a key in the `trackData` map.
-   **`course` (Int)**: An ID representing the specific course configuration within that location (e.g., Tokyo芝1600m). This ID is a key within the `courses` map of a `RaceTrack` entry in `trackData`.
-   **`condition` (CourseCondition)**: An enum representing the track's surface condition (e.g., GOOD, SLIGHTLY_HEAVY, HEAVY, BAD for turf/dirt). This affects `surfaceCondition` (an Int value) which then influences SP consumption and potentially speed/power modifiers.
-   **`gateCount` (Int)**: The number of starting gates available for the race. This influences the `initialLaneAdjuster`.
-   **`surfaceCondition` (Int)**: The integer value corresponding to the `condition` enum. Used in calculations like `spConsumptionCoef` and `surfaceSpeedModify`/`surfacePowerModify`.
-   **`initialLaneAdjuster` (Double)**: A value that adjusts the initial lane position of racers. It can vary based on the `location` and `gateCount`. For example, Ooi (location 10101) has a 0.0 adjuster, while tracks with 14+ gates or specific locations with 10+ gates have non-zero adjusters.

## `TrackDetail` Class (defined in `trackData.kt`)

This class holds the detailed geometric and characteristic data for a specific course configuration. Instances of this are what `RaceSetting.trackDetail` refers to, populated from `trackData`.

-   **`raceTrackId` (Int)**: Corresponds to the `location` ID.
-   **`name` (String)**: The human-readable name of the course (e.g., "芝1200m").
-   **`distance` (Int)**: The total length of the race in meters. This is fundamental for many calculations, including phase transitions, SP max (`spMax`), and base speed.
-   **`distanceType` (Int)**: An integer representing the distance category (1: Short, 2: Mile, 3: Middle, 4: Long). This is used to determine `distanceCategory` (an enum) which then affects things like `conservePowerTimeCoef`.
-   **`surface` (Int)**: An integer representing the track surface type (e.g., 1 for Turf, 2 for Dirt). This influences `spConsumptionCoef`, `surfaceSpeedModify`, `surfacePowerModify`, and `surfaceFitAccelerateCoef`.
-   **`turn` (Int)**: Indicates the direction of turns on the track (e.g., 1 for right turn, 2 for left turn, 4 for straight only like Niigata 1000m). While present, its direct use in core speed/stamina calculations in `RaceCalculator.kt` is not immediately obvious but could be relevant for skill conditions or more detailed physics not shown.
-   **`courseSetStatus` (List<Int>)**: A list of integers that can influence `modifiedSpeed` calculations. Specific statuses (1-5, likely corresponding to Speed, Stamina, Power, Guts, Wisdom) can give a bonus to the effective stat if the character's stat value is high enough.
-   **`laneMax` (Int)**: Represents the maximum width of the usable track in some unit (likely related to lane positioning calculations, e.g. `maxLaneDistance`).
-   **`finishTimeMin` (Double)** and **`finishTimeMax` (Double)**: Reference times for the course, potentially used for calculating race time deltas or performance evaluations. `finishTimeMax` is used in `RaceCalculator.goal()` to calculate `raceTimeDelta`.
-   **`corners` (List<Corner>)**: A list defining the corner segments of the track. Each `Corner` has a `start` (position in meters where the corner begins) and `length` (length of the corner in meters). Used to determine if a racer is in a corner, which can affect movement due to `cornerLoss` and is relevant for many skill conditions.
-   **`straights` (List<Straight>)**: A list defining the straight segments of the track. Each `Straight` has a `start` and `end` position in meters. Used to determine if a racer is in a straight, relevant for skill conditions (e.g., `isInFinalStraight`).
-   **`slopes` (List<Slope>)**: A list defining the sloped segments of the track. Each `Slope` has a `start` (position), `length`, and `slope` (gradient, positive for uphill, negative for downhill). The `getSlope(position)` method uses this to determine the current slope, which impacts target speed (uphills reduce it, downhills can increase it if in `downSlopeMode`) and acceleration.

## `trackData` and `rawCourseData`

-   **`rawCourseData` (String in `rawData.kt`)**: This is a large, multi-line JSON string embedded directly in the code. It contains an object where keys are location IDs (e.g., "10001" for Sapporo). Each location value is an object containing its `name` and a `courses` object. The `courses` object maps course IDs (e.g., "10101" for Sapporo芝1200m) to their detailed `TrackDetail` data.
-   **`trackData` (Map<Int, RaceTrack> in `trackData.kt`)**: This variable is lazily initialized by parsing `rawCourseData` using Kotlinx Serialization. It transforms the raw JSON into a structured map where `RaceTrack` (defined in `trackData.kt`) holds the location `name` and a map of its `TrackDetail` courses.
    The `RaceSetting` then uses `trackData` to look up the `TrackDetail` for the selected `track.location` and `track.course`.

This system allows for a comprehensive definition of various race tracks, each with unique geometry and characteristics that directly influence the race simulation. The `recentEventTrackList` in `trackData.kt` further shows how specific tracks can be curated, for example, for special events, by fetching a list from an external URL.

# Race Parameters and Calculations

The core logic for how a race unfolds is managed in `RaceState.kt` and `RaceCalculator.kt`. These files define how character stats, track features, and various random factors interact frame by frame.

## Speed Calculations

-   **Base Speed (`RaceSettingWithPassive.baseSpeed`)**: This is a fundamental speed value for the race, calculated as `20.0 - (courseLength - 2000) / 1000.0`. It's adjusted based on the track's total distance.
-   **Target Speed (`RaceState.targetSpeed`)**: This is the speed a character aims to reach in the current frame. It's a complex calculation:
    *   If SP is zero, target speed becomes `vMin` (a minimum speed).
    *   If current speed is below `v0` (0.85 * baseSpeed), target speed is `v0`.
    *   **Spurt Phase vs. Normal Phase**:
        *   If in the last spurt (final part of the race and `spurtParameters` are set), the target speed is `spurtParameters.speed`.
        *   Otherwise, it's based on the current race phase:
            *   Phases 0 and 1: `baseSpeed * styleSpeedCoef[currentPhase]`
            *   Phases 2 and 3 (later phases): `baseSpeed * styleSpeedCoef[currentPhase] + sqrt(modifiedSpeed / 500.0) * distanceFitSpeedCoef + (modifiedGuts * 450.0).pow(0.597) * 0.0001` (includes bonuses from speed stat, distance fit, and guts).
    *   A random factor based on wisdom and current track section (`sectionTargetSpeedRandoms`) is added.
    *   **Modifiers**:
        *   `PositionKeepState`: Can multiply target speed (e.g., 1.04 for SPEED_UP, 0.915 or 0.945 for PACE_DOWN).
        *   Operating skills: Sum of `totalSpeed` from active skills.
        *   `forceInSpeed`: Bonus if moving to an inner lane in phase 0.
        *   Slopes: Reduced on uphills (`abs(currentSlope) * 200.0 / modifiedPower`), increased in `downSlopeMode` on downhills.
        *   Lane changing with skills: Small bonus based on `modifiedPower`.
        *   Special modes: `leadCompetitionSpeed`, `competeFightSpeed`, `positionCompetitionSpeed`, `secureLeadSpeed`, `staminaLimitBreakSpeed` are added if active.
-   **Current Speed (`RaceSimulationState.currentSpeed`)**: This is the character's actual speed in the current frame.
    *   It's updated each frame in `RaceState.updateSelfSpeed` based on the `targetSpeed`, `acceleration`, and `deceleration`.
    *   `newSpeed = min(currentSpeed + elapsedTime * acceleration, targetSpeed)` if accelerating.
    *   `newSpeed = max(currentSpeed + elapsedTime * deceleration, targetSpeed)` if decelerating.
    *   During start dash, current speed is capped at `v0`.
    *   It's always kept between `vMin` (minimum speed, `startSpeed` during dash or `setting.vMinBase` otherwise) and `maxSpeed` (a general maximum, not explicitly detailed but implied).
    *   `speedDebuff` from skills is subtracted, then `currentSpeed` modifications from skills are added back.

## Acceleration and Deceleration

-   **Acceleration (`RaceState.acceleration`)**:
    *   Base coefficient `c` is 0.0006 (or 0.0004 on uphills).
    *   Formula: `c * sqrt(500.0 * modifiedPower) * styleAccelerateCoef[currentPhase] * surfaceFitAccelerateCoef * distanceFitAccelerateCoef`. It depends on modified power, style, phase, surface fit, and distance fit.
    *   During start dash, a flat `24.0` is added.
    *   Bonuses from operating skills and `competeFightAcceleration` (if active) are added.
    *   If `isInConservePower`, `conservePowerAcceleration` is added.
    *   Cannot be negative.
-   **Deceleration (`RaceState.deceleration`)**:
    *   If SP is zero: `-1.2`.
    *   If in `PositionKeepState.PACE_DOWN`: `-0.5`.
    *   Otherwise, depends on phase: Phase 0: `-1.2`, Phase 1: `-0.8`, Phases 2 & 3: `-1.0`.

## Stamina (SP)

-   **Max SP (`RaceSettingWithPassive.spMax`)**: Calculated as `trackDetail.distance + 0.8 * modifiedStamina * runningStyle.styleSpCoef`. Depends on race distance, modified stamina, and style's SP coefficient.
-   **SP Consumption (`RaceState.calcConsumePerSecond`)**:
    *   Base consumption: `20.0 * (currentSpeed - baseSpeed + 12.0).pow(2) / 144.0 * groundCoef`. Increases quadratically with speed difference from (baseSpeed - 12.0). `groundCoef` depends on track surface and condition.
    *   `spurtSpCoef` (1 + 200 / sqrt(600.0 * modifiedGuts)): Multiplies consumption if in spurt phase (phase 2+).
    *   **Modifiers (if `applyStatusModifier` is true):**
        *   `downSlopeMode`: Consumption multiplied by `0.4`.
        *   `leadCompetition`: Consumption multiplied (higher if also `isInTemptation`, and even higher for "Oonige").
        *   `isInTemptation` (not in lead competition): Consumption multiplied by `1.6`, and `0.6` of base consumption is added to `temptationWaste`.
        *   `PositionKeepState.PACE_DOWN`: Consumption multiplied by `0.6`.
-   **Required SP (`RaceState.calcRequiredSp`)**: Estimates SP needed for a given distance (`length`) at a certain speed (`v`), using `calcConsumePerSecond` (with `applyStatusModifier = false`).
-   **Running out of SP**: If `simulation.sp <= 0`, target speed drops to `vMin`, and deceleration becomes a harsh `-1.2`. This effectively makes the character slow down significantly.

## Race Phases (`RaceState.getPhase`)

The race is divided into 4 phases (0, 1, 2, 3) based on the character's position relative to the total course length.
-   Phase 0: Start to `courseLength / 6.0` (`phase1Start`)
-   Phase 1: `phase1Start` to `(courseLength * 2.0) / 3.0` (`phase2Start`)
-   Phase 2: `phase2Start` to `(courseLength * 5.0) / 6.0` (`phase3Start`)
-   Phase 3: `phase3Start` to `courseLength`
Phase transitions affect:
-   Target speed calculation (different formulas and coefficients like `styleSpeedCoef`).
-   Acceleration calculation (`styleAccelerateCoef`).
-   Deceleration values.
-   Skill activation conditions (many skills are phase-dependent).
-   Spurt parameter calculation (triggered when entering phase 2).
-   Conserve Power activation (triggered when entering phase 2).

## Special States and Modes

These are temporary conditions or strategic choices that affect a character's behavior, primarily managed in `RaceCalculator.updateFrame` and `RaceState`.

-   **Temptation (掛かり - `simulation.isInTemptation`)**:
    *   **Trigger**: Random chance at race start based on `temptationRate` (lower with higher wisdom). If triggered, a `temptationSection` (1-8) is chosen. Mode starts (`temptationModeStart`) when the character enters this section.
    *   **Effect**: Increases SP consumption significantly (multiplied by 1.6, or higher if also in `leadCompetition`). Part of the extra SP is wasted (`temptationWaste`). May affect `conservePowerAcceleration` negatively.
    *   **End**: Random chance to end every 3 seconds of temptation duration (0.55 probability). Guaranteed to end after 12 seconds.
-   **Position Keep (ポジションキープ - `simulation.positionKeepState`)**: Logic to maintain a desired position relative to others or a general pace.
    *   Active only in sections 0-10. Disabled in section 11.
    *   **`APPROXIMATE` mode**: If in a `paceDownModeSetting` section (style-dependent), enters `PACE_DOWN` for a set distance.
    *   **`VIRTUAL` mode** (uses a `paceMaker`): More complex logic based on distance to pacemaker, their style, and own style. Can enter `PACE_UP_EX` (chase much faster pacemaker), `SPEED_UP` (Nige slightly behind), `OVERTAKE` (Nige further behind), `PACE_UP` (Sasi/Oi far behind), `PACE_DOWN` (Sasi/Oi too close). These states apply multipliers to target speed (e.g., PACE_DOWN uses 0.915 or 0.945, SPEED_UP/PACE_UP use 1.04, OVERTAKE uses 1.05). Triggered/exited based on distance thresholds and random chances (wisdom-dependent rates like `positionKeepSpeedUpOvertakeRate`).
    *   **`SPEED_UP` mode** (simpler): Random chance to enter `SPEED_UP` state for a set distance.
-   **Lead Competition (位置取り争い - `state.inLeadCompetition`)**:
    *   **Trigger**: For Nige style, if position is beyond `system.leadCompetitionPosition` (e.g., 200m) and `leadCompetitionStart` is not set.
    *   **Effect**: Adds `setting.leadCompetitionSpeed` (guts-based) to target speed. Increases SP consumption. Lasts for `setting.leadCompetitionFrame` (guts-based time).
-   **Compete Fight (追い比べ - `simulation.competeFight`)**:
    *   **Trigger**: In final straight, if SP is >= 15% of max SP, random chance each second (`system.competeFightRate`).
    *   **Effect**: Adds `setting.competeFightSpeed` (guts-based) to target speed and `setting.competeFightAcceleration` (guts-based) to acceleration.
    *   **End**: If SP drops below 5% of max SP.
-   **Conserve Power (脚色十分 - `state.isInConservePower`)**:
    *   **Trigger**: When entering Phase 2 (`currentPhase == 1 && getPhase(simulation.position) == 2`), `applyConservePower` is called.
    *   **Effect**: If `setting.conservePowerAccelerationBase` (from Power stat over 1200 and style/distance category) is > 0, adds this (adjusted by activity like temptation/lead competition) to acceleration for `setting.conservePowerFrame`.
-   **Position Competition (位置取り調整 - `simulation.positionCompetition`)**:
    *   **Trigger**: In sections 11-15, if not in `staminaKeep`, random chance (`system.positionCompetitionRate`) if `positionCompetitionNextFrame` is reached.
    *   **Effect**: Adds `setting.positionCompetitionSpeed` (power/guts/style-based) to target speed. Consumes `setting.positionCompetitionStamina`. Lasts 2 seconds. Then a 1-second cooldown.
    *   `positionCompetitionCount` tracks activations.
-   **Stamina Keep (持久力温存 - `simulation.staminaKeep`)**:
    *   **Trigger**: In sections 11-15, as an alternative to `Position Competition`. If estimated SP required for phase 2 (`calcRequiredSpInPhase2`) is high relative to current SP, random chance (`system.staminaKeepRate`).
    *   **Effect**: Prevents `Position Competition` from activating. Active until section 16 or if `Position Competition` triggers. `staminaKeepDistance` tracks distance covered in this mode.
-   **Secure Lead (リード確保 - `simulation.secureLead`)**:
    *   **Trigger**: In sections 11-15 (not for Oi style), if `secureLeadNextFrame` is reached, random chance (`system.secureLeadRate`).
    *   **Effect**: Adds `setting.secureLeadSpeed` (guts/style-based) to target speed. Consumes `setting.secureLeadStamina`. Lasts 2 seconds. Then a 2-second cooldown (if not triggered again).
-   **Stamina Limit Break (スタミナ勝負 - `simulation.staminaLimitBreak`)**:
    *   **Trigger**: For long races (`courseLength > 2100`), if current speed reaches `setting.maxSpurtSpeed`.
    *   **Effect**: Adds `setting.staminaLimitBreakSpeed` (from Stamina stat over 1200 and distance) to target speed.
-   **Down Slope Mode (下り坂モード - `simulation.isInDownSlopeMode`)**:
    *   **Trigger**: On a downhill slope, if not `fixRandom`, random chance each second (`setting.modifiedWisdom * 0.0004`) to start.
    *   **Effect**: Target speed increases by `abs(currentSlope) / 10.0 + 0.3`. SP consumption is multiplied by `0.4`.
    *   **End**: On a downhill slope, random chance each second (`0.2`) to end. Ends if not on a downhill slope.

## Lane Movement (`RaceState.applyMoveLane`)

Logic for how characters change lanes:
-   **Target Lane (`simulation.targetLane`)**:
    *   If skill fixes lane (e.g. `fixLane` in `OperatingSkill`): `9.5 * horseLane` (likely far outside).
    *   If "overtake" special state active: `maxOf(currentTargetLane, horseLane, extraMoveLane)`.
    *   If SP is zero: current lane.
    *   If `PositionKeepState.PACE_DOWN`: `0.18` (very inner).
    *   If `extraMoveLane` (calculated for final corner/straight, based on current lane and track width) is further out than current lane: `extraMoveLane`.
    *   In phase 0 or 1 and not side-blocked: `max(0.0, currentLane - 0.05)` (try to move slightly inwards).
    *   Otherwise: current lane.
-   **Lane Change Speed (`simulation.laneChangeSpeed`)**:
    *   Increases by `laneChangeAccelerationPerFrame` up to a `targetSpeed`. This `targetSpeed` is `setting.baseLaneChangeTargetSpeed` (power-based), potentially modified by current lane position relative to max lane distance if before `moveLanePoint`.
    *   If side-blocked and trying to move into block, or if target lane is current lane: speed is 0.
    *   Actual speed is `min(laneChangeSpeed + sumOfSkillLaneChangeSpeed, 0.6)`.
-   **Current Lane Update (`simulation.currentLane`)**: Updated by `actualSpeed`. If moving inwards, speed is effectively increased by `(1.0 + currentLane)` factor, suggesting inner lanes are "faster" to move into.

## Start Dash (`RaceState.updateStartDash`, `RaceCalculator.initializeState`)

-   `simulation.isStartDash` is true initially.
-   `simulation.delayTime` is set based on `startDelay` (random, modified by start skills). Actual movement and SP consumption only begin after `delayTime` is over.
-   During start dash, acceleration gets a `+24.0` bonus.
-   Target speed is effectively `setting.v0` (0.85 * baseSpeed). Current speed is capped at `v0`.
-   Start dash ends (`isStartDash = false`) once current speed reaches `setting.v0`.

## Spurt Mechanics (`RaceState.calcSpurtParameter`, `RaceState.calcSpurtDistance`)

-   **Spurt Parameter Calculation (`calcSpurtParameter`)**:
    *   Triggered when entering phase 2. Calculates `simulation.spurtParameters` (distance, speed, spDiff, time).
    *   `maxDistance` is remaining race distance.
    *   `spurtDistance` is calculated by `calcSpurtDistance` using `setting.maxSpurtSpeed`.
    *   If `spurtDistance >= maxDistance` (can spurt at max speed for the rest of the race):
        *   Sets `spurtParameters` with `maxDistance` and `setting.maxSpurtSpeed`.
        *   If this is the first spurt calculation and near start of phase 2, `maxSpurt` flag may be set.
    *   If SP is insufficient for `setting.v3` (a slower target speed for later phase): `spurtParameters` set with 0 distance and `setting.v3`.
    *   Otherwise (SP is between enough for `v3` and enough for `maxSpurtSpeed`):
        *   It iterates through speeds from `v3` to `maxSpurtSpeed`. For each speed `v`, it calculates `distanceV` (distance possible at this speed `v` given SP).
        *   It creates `SpurtParameters` candidates, calculating total time as `distanceV / v + (maxDistance - distanceV) / setting.v3` (spurt at `v` then continue at `v3`).
        *   Candidates are sorted by time. The best one is chosen, with a random chance (wisdom-based) to pick a non-optimal one.
-   **Spurt Distance Calculation (`calcSpurtDistance(v)`)**: Calculates how far a character can run at speed `v` given current SP, considering SP cost at speed `v` versus cost at `setting.v3`. Includes a fixed 60m distance.
-   The `spurtParameters.speed` is then used as the target speed in `RaceState.targetSpeed` if `simulation.position + spurtParameters.distance > setting.courseLength` (i.e., if the calculated spurt will finish the race).

This covers the main aspects of race parameters and calculations as found in the provided file snippets. Many of these interact in complex ways, with stats, skills, track features, and random elements all playing a role.

# Skill System

Skills play a crucial role in race outcomes, providing various buffs, debuffs, or altering character behavior. The definitions for skills are primarily found in `SkillData.kt` (loaded from `data/skill_data.txt`), while their invocation and effects are handled in `RaceCalculator.kt` and `RaceState.kt`.

## Skill Definition (`SkillData.kt`)

-   **`SkillData` Class**: Contains all information about a skill, including:
    *   `id`, `name`, `rarity`, `group`, `type` (e.g., BUFF, DEBUFF, HEAL), `sp` (cost if any, though most race skills don't have an SP cost to activate).
    *   `activateLot`: Base chance for the skill to even be considered for activation (0 means always considered if conditions met, 1 means subject to `skillActivateRate`).
    *   `invokes`: A list of `Invoke` objects, each representing a potential way the skill can manifest with its own conditions and effects.
-   **`Invoke` Class**: Defines a specific invocation of a skill:
    *   `conditions` and `preConditions`: Lists of `SkillCondition` lists. All conditions within an inner list must be met (AND), and at least one inner list among the outer list must be met (OR). `preConditions` might be checked earlier or less frequently.
    *   `effects`: A list of `SkillEffect` objects detailing what the skill does.
    *   `cd` (cooldown), `duration`.
-   **`SkillCondition` Class**: Defines a single condition (e.g., `type = "phase"`, `operator = "=="`, `value = 0` for "is in phase 0").
-   **`SkillEffect` Class**: Defines a single effect (e.g., `type = "targetSpeed"`, `value = 3500` (0.35 m/s), `duration`). Values can be modified by `level` (for unique skills) and `special` flags (e.g., scaling with stats or other factors).

## Skill Invocation and Pre-computation

-   **`RaceSetting.invokeSkills()`**: Called during `RaceCalculator.initializeState`.
    *   Filters `UmaStatus.hasSkills` based on `activateLot` and `skillActivateRate` (wisdom-based, unless `skillActivateAdjustment` is set to ignore this).
    *   Unique skills (`rarity == "unique"`) have their effects scaled by `umaStatus.uniqueLevel` via `Invoke.applyLevel()`.
    *   Creates a list of `InvokedSkill` objects for the race.
-   **`InvokedSkill` Class (in `RaceState.kt`)**:
    *   Holds the `SkillData`, the specific `Invoke` instance, and two functions: `preCheck` and `check`. These functions are generated from the `preConditions` and `conditions` of the `Invoke` object, respectively, and are used to evaluate if the skill can activate in the current `RaceState`.

## Passive Skills

-   **`RaceSetting.applyPassive()`**: Called during `RaceCalculator.initializeState` after `invokeSkills`.
    *   Iterates through the `InvokedSkill` list.
    *   If an `InvokedSkill.invoke.isPassive` is true and its `check` function returns true for the current state, its effects are applied to create a `PassiveBonus`.
-   **`PassiveBonus` Class (in `RaceState.kt`)**:
    *   Accumulates permanent stat bonuses (speed, stamina, power, guts, wisdom) and `temptationRate` modifications from all activated passive skills.
    *   These bonuses are then used to calculate the `modifiedSpeed`, `modifiedStamina`, etc., in `RaceSettingWithPassive`.
    *   The number of triggered passives is stored in `simulation.passiveTriggered`.

## Start Skills

-   **`RaceState.triggerStartSkills()`**: Called at the very end of `RaceCalculator.initializeState`.
    *   Some passive skills (`!skill.invoke.isStart`) are triggered here (if their conditions are met by the initial state) and their effects (like healing) are applied immediately.
    *   Skills where `InvokedSkill.invoke.isStart` is true are processed:
        *   They can modify `simulation.startDelay` (e.g., `startMultiply`, `startAdd` effects).
        *   Their other effects (e.g., initial speed boost) are applied.
    *   These triggered skills are recorded in the first `RaceFrame`.

## Active Skills During the Race

-   **Triggering (`RaceState.checkSkillTrigger()`)**: Called every frame in `RaceCalculator.updateFrame`.
    *   Iterates through all `InvokedSkill`s that are not passive and not start skills.
    *   Checks `preConditions` first (if not already `preChecked`).
    *   If `preConditions` pass and the skill is not on cooldown (`coolDownMap`):
        *   Checks main `conditions`.
        *   If conditions are met and a random check against `skillActivateRate` (modified by wisdom, unless `fixRandom`) passes:
            *   The skill is triggered.
            *   `TriggeredSkill` object is created.
            *   If it's a healing skill (`invoke.isHeal`), SP is increased by `invoke.heal(this)`, and `healTriggerCount` is incremented.
            *   If it has a duration and provides speed/acceleration:
                *   An `OperatingSkill` instance is created and added to `simulation.operatingSkills`.
            *   The skill is put on cooldown in `simulation.coolDownMap` using `invoke.coolDownId` for `invoke.cd` duration (in frames).
            *   `skillTriggerCount` is incremented for the current phase.
-   **Effects (`OperatingSkill` in `RaceState.kt`)**:
    *   Active skills are stored in `simulation.operatingSkills`.
    *   `OperatingSkill` holds:
        *   `data` (the `InvokedSkill`), `startFrame`.
        *   Calculated effects: `targetSpeed` (from `invoke.targetSpeed(state)`), `speedWithDecel` (from `invoke.speedWithDecel(state)`), `currentSpeed` (immediate speed change, from `invoke.currentSpeed(state)`), `acceleration` (from `invoke.acceleration(state)`).
        *   `duration` (from `invoke.calcDuration(state)`).
        *   `fixLane` (bool, from `invoke.isFixLane`), `laneChangeSpeed` (from `invoke.laneChangeSpeed(state)`).
    *   These values are then used in `RaceState.targetSpeed` and `RaceState.acceleration` calculations.
        *   `operatingSkills.sumOf { it.totalSpeed }` (where `totalSpeed` is `targetSpeed + speedWithDecel`) is added to the target speed.
        *   `operatingSkills.sumOf { it.acceleration }` is added to acceleration.
        *   `operatingSkills.sumOf { it.currentSpeed }` is added to current speed after other calculations, and also contributes to `speedDebuff` for the next frame if negative.
-   **Duration**:
    *   `OperatingSkill.duration` is calculated via `Invoke.calcDuration(state)`, which can be fixed or dynamic (e.g., based on current SP, like unique skill of Super Creek).
    *   Skills are removed from `simulation.operatingSkills` in `RaceCalculator.updateFrame` when `(simulation.frameElapsed - operatingSkill.startFrame) * secondPerFrame > operatingSkill.duration`.

## Skill Cooldown (`RaceSimulationState.coolDownMap`)

-   When a skill is triggered, its `invoke.coolDownId` (either the skill's own ID or a specific ID for that invoke instance) is added to `simulation.coolDownMap` with a value representing the frame number until which it's on cooldown.
-   `RaceState.checkSkillTrigger` checks this map and will not attempt to activate a skill if its `coolDownId` is present and the current `frameElapsed` is less than the stored cooldown frame.
-   The base cooldown duration comes from `Invoke.cd` (default 500 frames, can be different per invoke).

The skill system is intricate, with many layers of conditions, effects, and interactions that significantly impact the race dynamics. Many specific skill conditions (like order, distance to others, etc.) are approximated or simplified as noted in `SkillData.kt`'s `ignoreConditions` and `approximateConditions` for simulation purposes.

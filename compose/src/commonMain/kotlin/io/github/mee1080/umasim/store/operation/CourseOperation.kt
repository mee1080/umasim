package io.github.mee1080.umasim.store.operation

import io.github.mee1080.umasim.race.calc2.Track
import io.github.mee1080.umasim.race.data.CourseCondition
import io.github.mee1080.umasim.race.data.trackData
import io.github.mee1080.umasim.store.AppState
import io.github.mee1080.umasim.store.framework.DirectOperation

fun setLocation(location: Int) = DirectOperation<AppState> { state ->
    state.updateSetting { setting ->
        if (location == setting.track.location) {
            return@updateSetting setting
        }
        val raceTrack = trackData[location] ?: return@updateSetting setting
        val course = raceTrack.courses.keys.min()
        val track = setting.track.copy(location = location, course = course)
        setting.copy(track = track)
    }
}

fun setCourse(course: Int) = DirectOperation<AppState> { state ->
    state.updateSetting { setting ->
        val track = setting.track.copy(course = course)
        setting.copy(track = track)
    }
}

fun setCourseCondition(condition: CourseCondition) = DirectOperation<AppState> { state ->
    state.updateSetting { setting ->
        val track = setting.track.copy(condition = condition)
        setting.copy(track = track)
    }
}

fun setTrack(track: Track) = DirectOperation<AppState> { state ->
    state.updateSetting { setting ->
        setting.copy(track = track)
    }
}

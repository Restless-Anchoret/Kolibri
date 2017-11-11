package com.ran.kolibri.component.scheduling

import com.ran.kolibri.app.Config.SCHEDULING_GIT_COMMIT_TASK_PERIOD
import com.ran.kolibri.extension.logInfo
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import java.time.Instant

@Component
class GitCommitSchedulingTask {

    @Scheduled(fixedRateString = "${SCHEDULING_GIT_COMMIT_TASK_PERIOD}000")
    fun scheduledGitCommit() {
        logInfo { "GitCommitSchedulingTask started at: ${Instant.now()}" }
        // todo
        logInfo { "GitCommitSchedulingTask finished at: ${Instant.now()}" }
    }

}

package com.ran.kolibri.service

import com.ran.kolibri.dto.export.project.ProjectExportDto
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class ImportService {

    @Transactional
    fun importProject(imported: ProjectExportDto) {
        // todo
    }

}

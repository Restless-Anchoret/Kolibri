package com.ran.kolibri.service.export

import com.fasterxml.jackson.databind.ObjectMapper
import com.ran.kolibri.dto.export.project.ProjectExportDto
import com.ran.kolibri.extension.map
import com.ran.kolibri.service.project.ProjectService
import ma.glasnost.orika.MapperFacade
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class ExportService {

    @Autowired
    lateinit var projectService: ProjectService
    @Autowired
    lateinit var orikaMapperFacade: MapperFacade
    @Autowired
    lateinit var objectMapper: ObjectMapper

    @Transactional
    fun exportProject(projectId: Long): ProjectExportDto {
        val project = projectService.getProjectById(projectId)
        return orikaMapperFacade.map(project)
    }

    @Transactional
    fun exportProjectAsString(projectId: Long): String {
        val projectExportDto = exportProject(projectId)
        return objectMapper.writeValueAsString(projectExportDto)
    }

}

package com.ran.kolibri.service.export

import com.fasterxml.jackson.databind.ObjectMapper
import com.ran.kolibri.dto.export.project.ExportDto
import com.ran.kolibri.dto.export.project.ProjectExportDto
import com.ran.kolibri.extension.map
import com.ran.kolibri.service.project.ProjectService
import ma.glasnost.orika.MapperFacade
import org.apache.commons.codec.digest.DigestUtils
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
    fun exportProject(projectId: Long): ExportDto {
        val project = projectService.getProjectById(projectId)
        val projectExportDto = orikaMapperFacade.map<ProjectExportDto>(project)
        var projectExportDtoAsString = objectMapper.writeValueAsString(projectExportDto)
        var checksum = DigestUtils.sha512Hex(projectExportDtoAsString)
        var exportDto = ExportDto()
        exportDto.checksum = checksum
        exportDto.project = projectExportDto
        return exportDto
    }

    @Transactional
    fun exportProjectAsString(projectId: Long): String {
        val projectExportDto = exportProject(projectId)
        return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(projectExportDto)
    }

}

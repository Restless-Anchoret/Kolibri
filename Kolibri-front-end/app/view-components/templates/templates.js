'use strict';

angular.module('kolibri')
    .component('templatesComponent', {
        templateUrl: 'view-components/templates/templates.html',
        controller: function($scope, $log, projectsService, utilsService) {
            $scope.tableParams = utilsService.createTableParams(
                projectsService.getAllTemplateProjects, {}
            );

            $scope.projectActions = [
                { act: editProjectTemplate, name: 'Edit' },
                { act: exportProjectTemplate, name: 'Export' },
                { act: deleteProjectTemplate, name: 'Delete' }
            ];

            function editProjectTemplate(projectId) {
                $log.debug('Editing project template. Id = ' + projectId);
            }

            function exportProjectTemplate(projectId) {
                $log.debug('Exporting project template. Id = ' + projectId);
            }

            function deleteProjectTemplate(projectId) {
                $log.debug('Deleting project template. Id = ' + projectId);
            }
        }
    });

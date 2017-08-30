'use strict';

angular.module('kolibri')
    .component('templatesComponent', {
        templateUrl: 'view-components/templates/templates.html',
        controller: function($scope, $log, projectsService, utilsService) {
            var requestParams = {
                isTemplate: true,
                sort: 'name,asc'
            };

            $scope.tableParams = utilsService.createTableParams(
                projectsService.getProjects, requestParams
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

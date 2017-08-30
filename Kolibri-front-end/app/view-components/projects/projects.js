'use strict';

angular.module('kolibri')
    .component('projectsComponent', {
        templateUrl: 'view-components/projects/projects.html',
        controller: function($scope, $log, projectsService, utilsService) {
            var requestParams = {
                isTemplate: false,
                sort: 'name,asc'
            };

            $scope.tableParams = utilsService.createTableParams(
                projectsService.getProjects, requestParams
            );

            $scope.projectActions = [
                { act: editProject, name: 'Edit' },
                { act: exportProject, name: 'Export' },
                { act: deleteProject, name: 'Delete' }
            ];

            function editProject(projectId) {
                $log.debug('Editing project. Id = ' + projectId);
            }

            function exportProject(projectId) {
                $log.debug('Exporting project. Id = ' + projectId);
            }

            function deleteProject(projectId) {
                $log.debug('Deleting project. Id = ' + projectId);
            }
        }
    });

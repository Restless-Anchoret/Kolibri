'use strict';

angular.module('kolibri')
    .component('projectsComponent', {
        templateUrl: 'view-components/projects/projects.html',
        controller: function($scope, $log, projectsService, NgTableParams) {
            $scope.tableParams = new NgTableParams({}, {
                getData: function(params) {
                    return projectsService.getAllActiveProjects(params.url()).then(function(data) {
                        $log.debug('Projects were successfully retrieved. Response:');
                        $log.debug(data);
                        params.total(data.totalElements);
                        return data.content;
                    });
                }
            });

            $scope.projectOperations = [
                { action: editProject, name: 'Edit' },
                { action: exportProject, name: 'Export' },
                { action: deleteProject, name: 'Delete' }
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

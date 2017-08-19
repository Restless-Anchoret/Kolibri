'use strict';

angular.module('kolibri')
    .component('templatesComponent', {
        templateUrl: 'view-components/templates/templates.html',
        controller: function($scope, $log, projectsService, NgTableParams, RequestInfo) {
            $scope.tableParams = new NgTableParams({}, {
                getData: function(params) {
                    return projectsService.getAllTemplateProjects(new RequestInfo({ pageable: params.url() }))
                        .then(function(data) {
                            $log.debug('Template projects were successfully retrieved. Response:');
                            $log.debug(data);
                            params.total(data.totalElements);
                            return data.content;
                        });
                }
            });

            $scope.projectOperations = [
                { action: editProjectTemplate, name: 'Edit' },
                { action: exportProjectTemplate, name: 'Export' },
                { action: deleteProjectTemplate, name: 'Delete' }
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

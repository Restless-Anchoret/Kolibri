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
        }
    });

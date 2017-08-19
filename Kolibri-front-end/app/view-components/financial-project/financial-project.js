'use strict';

angular.module('kolibri')
    .component('financialProjectComponent', {
        templateUrl: 'view-components/financial-project/financial-project.html',
        controller: function($scope, $log, $stateParams, financialProjectsService, RequestInfo) {
            var params = {
                projectId: $stateParams.projectId
            };
            financialProjectsService.getFinancialProjectById(new RequestInfo(params))
                .then(function(data) {
                    $log.debug('Financial project was retrieved. Response: ');
                    $log.debug(data);
                    $scope.project = data;
                });
        }
    });

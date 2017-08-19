'use strict';

angular.module('kolibri')
    .component('financialProjectComponent', {
        templateUrl: 'view-components/financial-project/financial-project.html',
        controller: function($scope, $log, $stateParams, financialProjectsService) {
            financialProjectsService.getFinancialProjectById($stateParams.projectId)
                .then(function(data) {
                    $log.debug('Financial project was retrieved. Response: ');
                    $log.debug(data);
                    $scope.project = data;
                });
        }
    });

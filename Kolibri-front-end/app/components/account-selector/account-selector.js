'use strict';

angular.module('kolibri')
    .component('accountSelectorComponent', {
        templateUrl: 'components/account-selector/account-selector.html',
        bindings: {
            model: '=',
            projectId: '<',
            selectClass: '@'
        },
        controller: function($scope, $log, financialProjectsService) {
            var ctrl = this;

            ctrl.$onInit = function() {
                _.extend($scope, {
                    loadingFunction: financialProjectsService.getFinancialProjectAccounts,
                    criteria: {
                        projectId: ctrl.projectId
                    }
                });
            };

        }
    });

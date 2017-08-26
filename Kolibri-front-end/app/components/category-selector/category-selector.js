'use strict';

angular.module('kolibri')
    .component('categorySelectorComponent', {
        templateUrl: 'components/category-selector/category-selector.html',
        bindings: {
            model: '=',
            projectId: '<',
            selectClass: '@'
        },
        controller: function($scope, $log, financialProjectsService) {
            var ctrl = this;

            ctrl.$onInit = function() {
                _.extend($scope, {
                    loadingFunction: financialProjectsService.getFinancialProjectOperationCategories,
                    criteria: {
                        projectId: ctrl.projectId
                    }
                });
            };

        }
    });

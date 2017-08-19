'use strict';

angular.module('kolibri')
    .component('financialProjectHeaderComponent', {
        templateUrl: 'view-components/financial-project/header/header.html',
        controller: function($scope) {
            $scope.financialProjectTabs = [
                { state: 'financial-project.operations', name: 'Operations' },
                { state: 'financial-project.accounts', name: 'Accounts' },
                { state: 'financial-project.categories', name: 'Categories' },
                { state: 'financial-project.tools', name: 'Tools' }
            ];
        }
    });

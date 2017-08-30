'use strict';

angular.module('kolibri')
    .component('financialProjectAccountsComponent', {
        templateUrl: 'view-components/financial-project/accounts/accounts.html',
        controller: function($scope, $log, $stateParams, financialProjectsService, utilsService) {
            var requestParams = {
                projectId: $stateParams.projectId,
                sort: 'name,asc'
            };

            $scope.tableParams = utilsService.createTableParams(
                financialProjectsService.getFinancialProjectAccounts, requestParams
            );

            $scope.accountsActions = [
                { act: editAccount, name: 'Edit' },
                { act: removeAccount, name: 'Remove' }
            ];

            function editAccount(accountId) {
                $log.debug('Editing account. Id = ' + accountId);
            }

            function removeAccount(accountId) {
                $log.debug('Deleting account. Id = ' + accountId);
            }
        }
    });

'use strict';

angular.module('kolibri')
    .component('financialProjectOperationsComponent', {
        templateUrl: 'view-components/financial-project/operations/operations.html',
        controller: function($scope, $log, $stateParams, financialProjectsService, utilsService) {
            var requestParams = {
                projectId: $stateParams.projectId
            };

            $scope.tableParams = utilsService.createTableParams(
                financialProjectsService.getFinancialProjectOperations, requestParams
            );

            $scope.operationsActions = [
                { act: editOperation, name: 'Edit' },
                { act: removeOperation, name: 'Remove' }
            ];

            $scope.isIncomeOperation = function(operation) {
                return (operation.operationType === "income");
            };
            $scope.isExpendOperation = function(operation) {
                return (operation.operationType === "expend");
            };
            $scope.isTransferOperation = function(operation) {
                return (operation.operationType === "transfer");
            };

            function editOperation(operationId) {
                $log.debug('Editing operation. Id = ' + operationId);
            }

            function removeOperation(operationId) {
                $log.debug('Deleting operation. Id = ' + operationId);
            }
        }
    });

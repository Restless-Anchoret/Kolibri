'use strict';

angular.module('kolibri')
    .component('financialProjectOperationsComponent', {
        templateUrl: 'view-components/financial-project/operations/operations.html',
        controller: function($scope, $log, $stateParams, financialProjectsService, utilsService, dialogsService) {
            var requestParams = {
                projectId: $stateParams.projectId
            };

            var tableParams = utilsService.createTableParams(
                financialProjectsService.getFinancialProjectOperations, requestParams
            );

            var operationsActions = [
                { act: editOperation, name: 'Edit' },
                { act: removeOperation, name: 'Remove' }
            ];

            _.extend($scope, {
                tableParams: tableParams,
                operationsActions: operationsActions,
                isIncomeOperation: isIncomeOperation,
                isExpendOperation: isExpendOperation,
                isTransferOperation: isTransferOperation,
                operationHasComment: operationHasComment,
                newOperation: newOperation
            });

            function isIncomeOperation(operation) {
                return (operation.operationType === "income");
            }

            function isExpendOperation(operation) {
                return (operation.operationType === "expend");
            }

            function isTransferOperation(operation) {
                return (operation.operationType === "transfer");
            }

            function operationHasComment(operation) {
                return (operation.comment && operation.comment.trim().length > 0);
            }

            function newOperation() {
                dialogsService.newOperationDialog($stateParams.projectId);
            }

            function editOperation(operationId) {
                $log.debug('Editing operation. Id = ' + operationId);
            }

            function removeOperation(operationId) {
                $log.debug('Deleting operation. Id = ' + operationId);
            }
        }
    });

'use strict';

angular.module('kolibri')
    .controller('NewOperationDialogController', function($scope) {

        var incomeOperationType = { name: 'Income Operation' };
        var expendOperationType = { name: 'Expend Operation' };
        var transferOperationType = { name: 'Transfer Operation' };

        var operationTypes = [
            incomeOperationType,
            expendOperationType,
            transferOperationType
        ];

        var footerButtons = [{
            name: 'Create',
            callback: createNewOperation
        }];

        _.extend($scope, {
            projectId: $scope.ngDialogData.projectId,
            incomeOperationType: incomeOperationType,
            operationType: incomeOperationType,
            expendOperationType: expendOperationType,
            transferOperationType: transferOperationType,
            operationTypes: operationTypes,
            footerButtons: footerButtons
        });

        function createNewOperation(closeDialog) {

        }

    });

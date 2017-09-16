'use strict';

angular.module('kolibri')
    .controller('NewOperationDialogController', function($scope, $log, financialProjectsService,
                                                         dialogsService, utilsService, RequestInfo) {

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
            isIncomeOperationChosen: isIncomeOperationChosen,
            isExpendOperationChosen: isExpendOperationChosen,
            isTransferOperationChosen: isTransferOperationChosen,
            operationTypes: operationTypes,
            footerButtons: footerButtons,
            data: {
                operationType: incomeOperationType,
                operationDate: utilsService.getDateWithoutTime()
            }
        });

        function isIncomeOperationChosen() {
            return $scope.data.operationType === incomeOperationType;
        }

        function isExpendOperationChosen() {
            return $scope.data.operationType === expendOperationType;
        }

        function isTransferOperationChosen() {
            return $scope.data.operationType === transferOperationType;
        }

        function createNewOperation(closeDialog) {
            if (isAnyFieldNotFilled()) {
                dialogsService.showToastError('Fill all the fields to continue');
                return;
            }

            var requestParams = { projectId: $scope.projectId };
            var addOperationDto = {
                operationCategoryId: $scope.data.operationCategory.id,
                moneyAmount: $scope.data.moneyAmount,
                comment: ($scope.data.comment ? $scope.data.comment : ''),
                operationDate: $scope.data.operationDate.getTime()
            };
            var addOperationFunction;

            if (isIncomeOperationChosen()) {
                addOperationDto.incomeAccountId = $scope.data.incomeAccount.id;
                addOperationFunction = financialProjectsService.addIncomeOperation;
            }

            if (isExpendOperationChosen()) {
                addOperationDto.expendAccountId = $scope.data.expendAccount.id;
                addOperationFunction = financialProjectsService.addExpendOperation;
            }

            if (isTransferOperationChosen()) {
                addOperationDto.toAccountId = $scope.data.toAccount.id;
                addOperationDto.fromAccountId = $scope.data.fromAccount.id;
                addOperationFunction = financialProjectsService.addTransferOperation;
            }
            addOperationFunction(new RequestInfo(requestParams, addOperationDto))
                .then(function() {
                    dialogsService.showToastSuccess('Operation was added');
                    closeDialog();
                });
        }

        function isAnyFieldNotFilled() {
            $log.debug($scope);
            return isIncomeOperationChosen() && !$scope.data.incomeAccount ||
                isExpendOperationChosen() && !$scope.data.expendAccount ||
                isTransferOperationChosen() && (!$scope.data.fromAccount || !$scope.data.toAccount) ||
                $scope.data.moneyAmount === undefined || !$scope.data.operationCategory;
        }

    });

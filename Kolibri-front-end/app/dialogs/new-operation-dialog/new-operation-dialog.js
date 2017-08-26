'use strict';

angular.module('kolibri')
    .controller('NewOperationDialogController', function($scope) {
        $scope.footerButtons = [{
            name: 'Create',
            callback: createNewOperation
        }];

        function createNewOperation(closeDialog) {

        }
    });

'use strict';

angular.module('kolibri')
    .controller('ConfirmDialogController', function($scope) {
        $scope.footerButtons = [{
            name: 'Yes',
            callback: $scope.ngDialogData.callback,
            afterCloseCallback: $scope.ngDialogData.afterCloseCallback
        }];
    });

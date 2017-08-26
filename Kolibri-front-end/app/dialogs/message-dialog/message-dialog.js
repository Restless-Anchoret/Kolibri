'use strict';

angular.module('kolibri')
    .controller('MessageDialogController', function($scope) {
        $scope.footerButtons = [{
            name: 'OK',
            callback: $scope.ngDialogData.callback,
            afterCloseCallback: $scope.ngDialogData.afterCloseCallback
        }];
    });

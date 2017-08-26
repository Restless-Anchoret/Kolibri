'use strict';

angular.module('kolibri')
    .controller('MessageDialogController', function($scope) {
        $scope.footerButtons = [{
            name: 'OK',
            callback: function(closeDialog) {
                closeDialog();
                $scope.ngDialogData.callback();
            }
        }];
    });

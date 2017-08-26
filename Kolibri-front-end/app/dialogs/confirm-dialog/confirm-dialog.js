'use strict';

angular.module('kolibri')
    .controller('ConfirmDialogController', function($scope) {
        $scope.footerButtons = [{
            name: 'Yes',
            callback: function(closeDialog) {
                closeDialog();
                $scope.ngDialogData.callback();
            }
        }];
    });

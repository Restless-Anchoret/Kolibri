'use strict';

angular.module('kolibri')
    .component('dialogTemplateComponent', {
        templateUrl: 'dialogs/dialog-template/dialog-template.html',
        transclude: true,
        bindings: {
            ngDialogId: '<',
            title: '<',
            footerButtons: '<',
            showCloseButton: '<',
            closeButtonText: '@'
        },
        controller: function($scope, ngDialog) {
            var ctrl = this;

            ctrl.$onInit = function() {
                ctrl.showCloseButton = (ctrl.showCloseButton === undefined ? true : ctrl.showCloseButton);
                ctrl.closeButtonText = (ctrl.closeButtonText ? ctrl.closeButtonText : 'Close');
                _.forEach(ctrl.footerButtons, function(footerButton) {
                    footerButton.resultCallback = function() {
                        if (footerButton.hasOwnProperty('callback')) {
                            footerButton.callback();
                        }
                        closeDialog();
                        if (footerButton.hasOwnProperty('afterCloseCallback')) {
                            footerButton.afterCloseCallback();
                        }
                    }
                });
                $scope.closeDialog = closeDialog;
            };

            function closeDialog() {
                ngDialog.close(ctrl.ngDialogId);
            }
        }
    });

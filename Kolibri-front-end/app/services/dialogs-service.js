'use strict';

angular.module('kolibri')
    .factory('dialogsService', function(ngDialog) {

        return {
            confirmDialog: confirmDialog,
            messageDialog: messageDialog,
            newOperationDialog: newOperationDialog,
            editOperationDialog: editOperationDialog
        };

        function confirmDialog(title, message, callback, afterCloseCallback) {
            return ngDialog.open({
                template: 'dialogs/confirm-dialog/confirm-dialog.html',
                controller: 'ConfirmDialogController',
                data: {
                    title: title,
                    message: message,
                    callback: callback,
                    afterCloseCallback: afterCloseCallback
                }
            });
        }

        function messageDialog(title, message, callback, afterCloseCallback) {
            return ngDialog.open({
                template: 'dialogs/message-dialog/message-dialog.html',
                controller: 'MessageDialogController',
                data: {
                    title: title,
                    message: message,
                    callback: callback,
                    afterCloseCallback: afterCloseCallback
                }
            });
        }

        function newOperationDialog() {

        }

        function editOperationDialog() {

        }

    });

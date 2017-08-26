'use strict';

angular.module('kolibri')
    .factory('dialogsService', function(ngDialog) {

        return {
            confirmDialog: confirmDialog,
            messageDialog: messageDialog,
            newOperationDialog: newOperationDialog,
            editOperationDialog: editOperationDialog
        };

        function confirmDialog(title, message, callback) {
            return ngDialog.open({
                template: 'dialogs/confirm-dialog/confirm-dialog.html',
                controller: 'ConfirmDialogController',
                data: {
                    title: title,
                    message: message,
                    callback: callback
                }
            });
        }

        function messageDialog(title, message, callback) {
            return ngDialog.open({
                template: 'dialogs/message-dialog/message-dialog.html',
                controller: 'MessageDialogController',
                data: {
                    title: title,
                    message: message,
                    callback: callback
                }
            });
        }

        function newOperationDialog(projectId) {
            return ngDialog.open({
                template: 'dialogs/new-operation-dialog/new-operation-dialog.html',
                controller: 'NewOperationDialogController',
                data: {
                    projectId: projectId
                }
            });
        }

        function editOperationDialog() {

        }

    });

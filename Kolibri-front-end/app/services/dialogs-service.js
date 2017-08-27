'use strict';

angular.module('kolibri')
    .factory('dialogsService', function(ngDialog, toaster) {

        return {
            showToastInfo: showToastInfo,
            showToastSuccess: showToastSuccess,
            showToastWarning: showToastWarning,
            showToastError: showToastError,
            confirmDialog: confirmDialog,
            messageDialog: messageDialog,
            newOperationDialog: newOperationDialog,
            editOperationDialog: editOperationDialog
        };

        function showToast(type, message, title) {
            toaster.pop({
                type: type,
                body: message,
                title: title
            });
        }

        function showToastInfo(message, title) {
            showToast('info', message, title);
        }

        function showToastSuccess(message, title) {
            showToast('success', message, title);
        }

        function showToastWarning(message, title) {
            showToast('warning', message, title);
        }

        function showToastError(message, title) {
            showToast('error', message, title);
        }

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

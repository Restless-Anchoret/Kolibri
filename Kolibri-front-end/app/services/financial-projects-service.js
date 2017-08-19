'use strict';

angular.module('kolibri')
    .factory('financialProjectsService', function($http, utilsService, backendSettings) {
        return {
            getFinancialProjectAccounts: function(projectId, errorCallback) {
                return $http.get(backendSettings.url + '/financial-projects/' + projectId + '/accounts')
                    .then(utilsService.extractData, errorCallback);
            },
            getFinancialProjectOperationCategories: function(projectId, errorCallback) {
                return $http.get(backendSettings.url + '/financial-projects/' + projectId + '/categories')
                    .then(utilsService.extractData, errorCallback);
            },
            getFinancialProjectOperations: function(projectId, errorCallback) {
                return $http.get(backendSettings.url + '/financial-projects/' + projectId + '/operations')
                    .then(utilsService.extractData, errorCallback);
            },
            addIncomeOperation: function(projectId, dto, errorCallback) {
                return $http.post(backendSettings.url + '/financial-projects/' + projectId + '/income', dto)
                    .then(utilsService.extractData, errorCallback);
            },
            addExpendOperation: function(projectId, dto, errorCallback) {
                return $http.post(backendSettings.url + '/financial-projects/' + projectId + '/expend', dto)
                    .then(utilsService.extractData, errorCallback);
            },
            addTransferOperation: function(projectId, dto, errorCallback) {
                return $http.post(backendSettings.url + '/financial-projects/' + projectId + '/transfer', dto)
                    .then(utilsService.extractData, errorCallback);
            }
        };
    });

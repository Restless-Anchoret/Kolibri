'use strict';

angular.module('kolibri')
    .factory('financialProjectsService', function($http, utilsService, backendSettings) {
        return {
            getFinancialProjectAccounts: function(projectId, pageable, errorCallback) {
                return $http.get(backendSettings.url + '/financial-projects/' + projectId +
                    '/accounts?' + utilsService.pageableToUrl(pageable))
                    .then(utilsService.extractData, errorCallback);
            },
            getFinancialProjectOperationCategories: function(projectId, pageable, errorCallback) {
                return $http.get(backendSettings.url + '/financial-projects/' + projectId +
                    '/categories?' + utilsService.pageableToUrl(pageable))
                    .then(utilsService.extractData, errorCallback);
            },
            getFinancialProjectOperations: function(projectId, pageable, errorCallback) {
                return $http.get(backendSettings.url + '/financial-projects/' + projectId +
                    '/operations?' + utilsService.pageableToUrl(pageable))
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

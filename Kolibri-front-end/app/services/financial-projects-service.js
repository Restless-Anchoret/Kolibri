'use strict';

angular.module('kolibri')
    .factory('financialProjectsService', function($http, httpFactoryService) {
        return {
            getFinancialProjectById: httpFactoryService.create(
                '/financial-projects/{projectId}', 'GET'),

            getFinancialProjectAccounts: httpFactoryService.create(
                '/financial-projects/{projectId}/accounts', 'GET'),

            getFinancialProjectOperationCategories: httpFactoryService.create(
                '/financial-projects/{projectId}/categories', 'GET'),

            getFinancialProjectOperations: httpFactoryService.create(
                '/financial-projects/{projectId}/operations', 'GET'),

            addIncomeOperation: httpFactoryService.create(
                '/financial-projects/{projectId}/income', 'POST'),

            addExpendOperation: httpFactoryService.create(
                '/financial-projects/{projectId}/expend', 'POST'),

            addTransferOperation: httpFactoryService.create(
                '/financial-projects/{projectId}/transfer', 'POST')
        };
    });

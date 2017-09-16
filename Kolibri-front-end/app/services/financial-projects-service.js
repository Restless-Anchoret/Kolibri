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
                '/financial-projects/{projectId}/operations/income', 'POST'),

            addExpendOperation: httpFactoryService.create(
                '/financial-projects/{projectId}/operations/expend', 'POST'),

            addTransferOperation: httpFactoryService.create(
                '/financial-projects/{projectId}/operations/transfer', 'POST')
        };
    });

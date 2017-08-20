'use strict';

angular.module('kolibri')
    .factory('financialProjectsService', function($http, httpFactoryService) {
        return {
            getFinancialProjectById: httpFactoryService.create(
                '/financial-projects/{projectId}', 'GET'),

            getFinancialProjectAccounts: httpFactoryService.create(
                '/financial-projects/{projectId}/accounts', 'GET'),

            getAllFinancialProjectAccounts: httpFactoryService.create(
                '/financial-projects/{projectId}/accounts/all', 'GET'),

            getFinancialProjectOperationCategories: httpFactoryService.create(
                '/financial-projects/{projectId}/categories', 'GET'),

            getAllFinancialProjectOperationCategories: httpFactoryService.create(
                '/financial-projects/{projectId}/categories/all', 'GET'),

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

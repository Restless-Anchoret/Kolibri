'use strict';

angular.module('kolibri')
    .component('financialProjectCategoriesComponent', {
        templateUrl: 'view-components/financial-project/categories/categories.html',
        controller: function($scope, $log, $stateParams, financialProjectsService, utilsService) {
            var requestParams = {
                projectId: $stateParams.projectId
            };

            $scope.tableParams = utilsService.createTableParams(
                financialProjectsService.getFinancialProjectOperationCategories, requestParams
            );

            $scope.categoriesActions = [
                { act: editCategory, name: 'Edit' },
                { act: deleteCategory, name: 'Remove' }
            ];

            function editCategory(categoryId) {
                $log.debug('Editing category. Id = ' + categoryId);
            }

            function deleteCategory(categoryId) {
                $log.debug('Deleting category. Id = ' + categoryId);
            }
        }
    });

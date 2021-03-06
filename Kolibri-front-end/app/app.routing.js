'use strict';

angular.module('kolibri')
    .config(['$stateProvider', '$urlRouterProvider', function ($stateProvider, $urlRouterProvider) {
        $stateProvider.state('projects', {
            url: '/projects',
            views: {
                header: {component: 'headerComponent'},
                body: {component: 'projectsComponent'}
            }
        });
        $stateProvider.state('templates', {
            url: '/templates',
            views: {
                header: {component: 'headerComponent'},
                body: {component: 'templatesComponent'}
            }
        });
        $stateProvider.state('commits', {
            url: '/commits',
            views: {
                header: {component: 'headerComponent'},
                body: {component: 'commitsComponent'}
            }
        });
        $stateProvider.state('settings', {
            url: '/settings',
            views: {
                header: {component: 'headerComponent'},
                body: {component: 'settingsComponent'}
            }
        });
        $stateProvider.state('financial-project', {
            url: '/financial-project/{projectId}',
            views: {
                header: {component: 'financialProjectHeaderComponent'},
                body: {component: 'financialProjectComponent'}
            }
        });
        $stateProvider.state('financial-project.operations', {
            url: '/operations',
            views: {
                financialProjectBody: {component: 'financialProjectOperationsComponent'}
            }
        });
        $stateProvider.state('financial-project.accounts', {
            url: '/accounts',
            views: {
                financialProjectBody: {component: 'financialProjectAccountsComponent'}
            }
        });
        $stateProvider.state('financial-project.categories', {
            url: '/categories',
            views: {
                financialProjectBody: {component: 'financialProjectCategoriesComponent'}
            }
        });
        $stateProvider.state('financial-project.tools', {
            url: '/tools',
            views: {
                financialProjectBody: {component: 'financialProjectToolsComponent'}
            }
        });

        $urlRouterProvider.otherwise('/projects');
    }]);

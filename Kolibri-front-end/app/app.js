'use strict';

angular.module('kolibri', [
    'ui.router',
    'ui.bootstrap'
])
    .config(['$stateProvider', function ($stateProvider) {
        $stateProvider.state({
            name: 'projects',
            url: '/projects',
            component: 'projectsComponent'
        });
        $stateProvider.state({
            name: 'templates',
            url: '/templates',
            component: 'templatesComponent'
        });
        $stateProvider.state({
            name: 'settings',
            url: '/settings',
            component: 'settingsComponent'
        });
        $stateProvider.state({
            name: 'financial-project',
            url: '/financial-project/{projectId}',
            component: 'financialProjectComponent'
        });
        $stateProvider.state({
            name: 'financial-project.operations',
            url: '/operations',
            component: 'financialProjectOperationsComponent'
        });
        $stateProvider.state({
            name: 'financial-project.accounts',
            url: '/accounts',
            component: 'financialProjectAccountsComponent'
        });
        $stateProvider.state({
            name: 'financial-project.categories',
            url: '/categories',
            component: 'financialProjectCategoriesComponent'
        });
        $stateProvider.state({
            name: 'financial-project.tools',
            url: '/tools',
            component: 'financialProjectToolsComponent'
        });
    }]);

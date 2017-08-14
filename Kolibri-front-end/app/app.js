'use strict';

angular.module('kolibri', [
    'ui.router'
])
    .config(['$stateProvider', function ($stateProvider) {
        $stateProvider.state({
            name: 'projects',
            url: '/projects',
            component: 'projects'
        });
        $stateProvider.state({
            name: 'settings',
            url: '/settings',
            component: 'settings'
        });
    }]);

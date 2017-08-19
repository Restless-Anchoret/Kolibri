'use strict';

angular.module('kolibri')
    .component('actionsListComponent', {
        templateUrl: 'components/actions-list/actions-list.html',
        bindings: {
            actions: '<',
            parameter: '<'
        }
    });

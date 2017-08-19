'use strict';

angular.module('kolibri')
    .component('operationsListComponent', {
        templateUrl: 'components/operations-list/operations-list.html',
        bindings: {
            operations: '<',
            parameter: '<'
        }
    });

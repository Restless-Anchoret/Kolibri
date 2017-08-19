'use strict';

angular.module('kolibri')
    .component('headerComponent', {
        templateUrl: 'components/header/header.html',
        bindings: {
            tabs: '<'
        }
    });

'use strict';

angular.module('kolibri')
    .factory('projectsService', function($http, httpFactoryService) {
        return {
            getProjects: httpFactoryService.create(
                '/projects', 'GET')
        };
    });

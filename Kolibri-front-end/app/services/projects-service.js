'use strict';

angular.module('kolibri')
    .factory('projectsService', function($http, httpFactoryService) {
        return {
            getAllActiveProjects: httpFactoryService.create(
                '/projects', 'GET'),

            getAllTemplateProjects: httpFactoryService.create(
                '/projects/templates', 'GET')
        };
    });

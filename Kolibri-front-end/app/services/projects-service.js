'use strict';

angular.module('kolibri')
    .factory('projectsService', function($http, httpFactoryService) {
        return {
            getActiveProjects: httpFactoryService.create(
                '/projects', 'GET'),

            getTemplateProjects: httpFactoryService.create(
                '/projects/templates', 'GET')
        };
    });

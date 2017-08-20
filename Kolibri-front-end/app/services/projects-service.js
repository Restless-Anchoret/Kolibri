'use strict';

angular.module('kolibri')
    .factory('projectsService', function($http, httpFactoryService) {
        return {
            getActiveProjects: httpFactoryService.create(
                '/projects', 'GET'),

            getAllActiveProjects: httpFactoryService.create(
                '/projects/all', 'GET'),

            getTemplateProjects: httpFactoryService.create(
                '/projects/templates', 'GET'),

            getAllTemplateProjects: httpFactoryService.create(
                '/projects/templates/all', 'GET')
        };
    });

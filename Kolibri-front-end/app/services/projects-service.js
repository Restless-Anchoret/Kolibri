'use strict';

angular.module('kolibri')
    .factory('projectsService', function($http, utilsService, backendSettings) {
        return {
            getAllActiveProjects: function(pageable, errorCallback) {
                return $http.get(backendSettings.url + '/projects?' + utilsService.pageableToUrl(pageable))
                    .then(utilsService.extractData, errorCallback);
            },
            getAllTemplateProjects: function(pageable, errorCallback) {
                return $http.get(backendSettings.url + '/projects/templates?' + utilsService.pageableToUrl(pageable))
                    .then(utilsService.extractData, errorCallback);
            }
        };
    });

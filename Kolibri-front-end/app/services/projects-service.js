'use strict';

angular.module('kolibri')
    .factory('projectsService', function($http, utilsService, backendSettings) {
        return {
            getAllActiveProjects: function(errorCallback) {
                return $http.get(backendSettings.url + '/projects')
                    .then(utilsService.extractData, errorCallback);
            },
            getAllTemplateProjects: function(errorCallback) {
                return $http.get(backendSettings.url + '/projects/templates')
                    .then(utilsService.extractData, errorCallback);
            }
        };
    });

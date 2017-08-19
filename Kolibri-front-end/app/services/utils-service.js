'use strict';

angular.module('kolibri')
    .factory('utilsService', function() {
        return {
            extractData: function(response) {
                return response.data;
            }
        };
    });

'use strict';

angular.module('kolibri')
    .factory('utilsService', function() {
        return {
            extractData: function(response) {
                return response.data;
            },
            pageableToUrl: function(pageable) {
                return 'page=' + (pageable.page - 1) + '&size=' + pageable.count;
            }
        };
    });

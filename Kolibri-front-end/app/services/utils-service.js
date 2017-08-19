'use strict';

angular.module('kolibri')
    .factory('utilsService', function($log, NgTableParams, RequestInfo) {
        return {
            createTableParams: function(loaderFunction, requestParams) {
                return new NgTableParams({}, {
                    getData: function(params) {
                        var preparedRequestParams = angular.extend({}, requestParams, {
                            pageable: params.url()
                        });
                        return loaderFunction(new RequestInfo(preparedRequestParams))
                            .then(function(data) {
                                params.total(data.totalElements);
                                return data.content;
                            });
                    }
                });
            }
        };
    });

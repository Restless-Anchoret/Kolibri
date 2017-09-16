'use strict';

angular.module('kolibri')
    .factory('utilsService', function($log, NgTableParams, RequestInfo) {

        return {
            createTableParams: createTableParams,
            getDateWithoutTime: getDateWithoutTime,
            getTimestampWithoutTime: getTimestampWithoutTime
        };

        function createTableParams(loaderFunction, requestParams) {
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

        function getDateWithoutTime(date) {
            if (!date) {
                date = new Date().getTime();
            } else if (typeof date === 'object') {
                date = date.getTime();
            }
            var dayMilliseconds = 24 * 60 * 60 * 1000;
            return new Date(date - (date % dayMilliseconds));
        }

        function getTimestampWithoutTime(date) {
            return getDateWithoutTime(date).getTime();
        }

    });

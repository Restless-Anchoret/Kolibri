'use strict';

angular.module('kolibri')
    .factory('RequestInfo', function() {
        return function(params, dto, errorCallback) {
            this.params = params;
            this.dto = dto;
            this.errorCallback = errorCallback;
        }
    })
    .factory('httpFactoryService', function($log, $http, backendSettings) {
        return {
            create: createHttpRequest
        };

        function createHttpRequest(url, method) {
            return function(requestInfo) {
                $log.debug('------ Sending Request ------');
                $log.debug('URL: ' + url + '; method: ' + method);
                $log.debug(requestInfo);
                var preparedUrl = insertParams(url, requestInfo.params);
                $log.debug('Prepared URL: ' + preparedUrl);
                var request = {
                    method: method,
                    url: preparedUrl,
                    data: requestInfo.dto
                };
                return $http(request).then(extractData, requestInfo.errorCallback);
            }
        }

        function insertParams(url, params) {
            var resultUrl = url;
            var query = '';
            for (var key in params) {
                var value = params[key];
                if (url.indexOf('{' + key + '}') > -1) {
                    resultUrl = resultUrl.replace('{' + key + '}', value);
                } else if (key === 'pageable') {
                    query = addQueryParam(query, "page", value.page - 1);
                    query = addQueryParam(query, "size", value.count);
                } else {
                    query = addQueryParam(query, key, value);
                }
            }
            return backendSettings.url + resultUrl + (query.length === 0 ? '' : '?') + query;
        }

        function addQueryParam(query, key, value) {
            return query + (query.length === 0 ? '' : '&') + key + '=' + value;
        }

        function extractData(response) {
            $log.debug(response.data);
            $log.debug('------ Request was received ------');
            return response.data;
        }
    });

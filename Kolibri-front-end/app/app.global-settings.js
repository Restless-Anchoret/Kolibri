'use strict';

angular.module('kolibri')
    .run(['ngTableDefaults', function(ngTableDefaults) {
        var defaultParams = {
            page: 1,
            count: 50
        };
        var defaultSettings = {
            counts: []
        };

        ngTableDefaults.settings = angular.extend({}, ngTableDefaults.settings, defaultSettings);
        ngTableDefaults.params = angular.extend({}, ngTableDefaults.params, defaultParams);
    }]);

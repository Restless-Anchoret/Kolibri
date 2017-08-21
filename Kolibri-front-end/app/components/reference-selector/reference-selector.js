'use strict';

angular.module('kolibri')
    .component('referenceSelectorComponent', {
        templateUrl: 'components/reference-selector/reference-selector.html',
        bindings: {
            model: '=',
            dataSet: '<',
            loadingFunction: '<',
            loadingParams: '<',
            viewFunction: '<',
            placeholder: '@',
            selectClass: '@'
        },
        controller: function($scope, $log, RequestInfo) {
            var ctrl = this;

            ctrl.$onInit = function() {
                if (ctrl.dataSet) {
                    $scope.items = dataset;
                } else {
                    ctrl.loadingFunction(new RequestInfo(ctrl.loadingParams))
                        .then(function (data) {
                            $log.debug('My data:');
                            $log.debug(data);
                            $scope.items = data;
                        });
                }

                $scope.viewFunction = (ctrl.viewFunction ? ctrl.viewFunction : function(item) {
                    return (item ? item.name : item);
                });

                $scope.comparator = function(actual, expected) {
                    var presentation = $scope.viewFunction(actual);
                    return (presentation ? presentation.toLowerCase().indexOf(expected.toLowerCase()) > -1 : false);
                };
            };
        }
    });

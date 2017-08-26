'use strict';

angular.module('kolibri')
    .component('referenceSelectorComponent', {
        templateUrl: 'components/reference-selector/reference-selector.html',
        bindings: {
            model: '=',
            type: '@',
            items: '<',
            loadingFunction: '<',
            pageSize: '<',
            queryTransformFunction: '<',
            criteria: '<',
            viewFunction: '<',
            viewProperty: '@',
            placeholder: '@',
            selectClass: '@'
        },
        controller: function($scope, $log, $q, RequestInfo) {
            var ctrl = this;

            ctrl.$onInit = function() {
                _.extend($scope, {
                    viewFunction: getViewFunction(),
                    pageSize: (ctrl.pageSize ? ctrl.pageSize : 50),
                    queryTransformFunction: getQueryTransformFunction(),
                    comparator: comparator
                });

                if (ctrl.type === 'dataSet') {
                    $scope.items = ctrl.items;
                } else if (ctrl.type === 'singleLoad') {
                    loadAllItems();
                } else if (ctrl.type === 'paginationLoad') {
                    preparePagination();
                }
            };

            function getViewFunction() {
                if (ctrl.viewFunction) {
                    return ctrl.viewFunction;
                } else if (ctrl.viewProperty) {
                    return function(item) {
                        return (item ? item[ctrl.viewProperty] : item);
                    }
                } else {
                    return function(item) {
                        return (item ? item.name : item);
                    }
                }
            }

            function getQueryTransformFunction() {
                if (ctrl.queryTransformFunction) {
                    return ctrl.queryTransformFunction;
                } else {
                    return function(selectQuery, params) {
                        params.name = selectQuery;
                    }
                }
            }

            function comparator(actual, expected) {
                var presentation = $scope.viewFunction(actual);
                return (presentation ? presentation.toLowerCase().indexOf(expected.toLowerCase()) > -1 : false);
            }

            function loadAllItems() {
                var params = ctrl.criteria ? ctrl.criteria : {};
                ctrl.loadingFunction(new RequestInfo(params))
                    .then(function (data) {
                        $scope.items = data;
                    });
            }

            function preparePagination() {
                clearPagination();
                _.assign($scope, {
                    refreshList: refreshList,
                    requestMoreItems: requestMoreItems,
                    lastQuery: null
                });
            }

            function clearPagination() {
                _.extend($scope, {
                    nextPage: 0,
                    hasNextPage: true,
                    items: []
                });
            }

            function loadNextPage(pageNumber, selectQuery) {
                var params = {
                    page: pageNumber,
                    size: $scope.pageSize
                };

                $scope.queryTransformFunction(selectQuery, params);
                _.extend(params, ctrl.criteria);

                $log.debug('Reference Selector request params:');
                $log.debug(params);

                return ctrl.loadingFunction(new RequestInfo(params));
            }

            function requestMoreItems(selectQuery, queryChanged) {
                if (!queryChanged && ($scope.isLoadingMore || !$scope.hasNextPage)) {
                    return $q.defer([]);
                }

                if (queryChanged) {
                    clearPagination();
                }

                $scope.isLoadingMore = true;
                return loadNextPage($scope.nextPage, selectQuery)
                    .then(function(newItemsPage) {
                        $scope.items = (queryChanged ? newItemsPage.content :
                            $scope.items.concat(newItemsPage.content));
                        $scope.nextPage++;
                        $scope.hasNextPage = !newItemsPage.last;
                        return newItemsPage;
                    }, function(err) {
                        return $q.reject(err);
                    })
                    .finally(function() {
                        $scope.isLoadingMore = false;
                    });
            }

            function refreshList(selectQuery) {
                var queryChanged = (selectQuery !== $scope.lastQuery);
                $scope.lastQuery = selectQuery;
                if (queryChanged) {
                    requestMoreItems(selectQuery, queryChanged);
                }
            }

        }
    });

'use strict';

angular.module('kolibri')
    .component('datePickerComponent', {
        templateUrl: 'components/date-picker/date-picker.html',
        bindings: {
            model: '=',
            required: '<'
        },
        controller: function() {
            var ctrl = this;

            ctrl.$onInit = function() {
                ctrl.required = (ctrl.required ? ctrl.required : false);
            };
        }
    });

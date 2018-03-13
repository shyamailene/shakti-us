(function() {
    'use strict';

    angular
        .module('shaktiusApp')
        .controller('EventsDialogController', EventsDialogController);

    EventsDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Events'];

    function EventsDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Events) {
        var vm = this;

        vm.events = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;
        vm.eventTypes = [
            {eventType : "Sports"},
            {eventType : "Fun Games"},
            {eventType : "Cultural"},
            {eventType : "Sankranthi"},
            {eventType : "Health"},
            {eventType : "Yoga Camps"},
            {eventType : "Education"},
            {eventType : "Crafts Work Shop"},
            {eventType : "Career"},
            {eventType : "IT training"},
            {eventType : "Empowerement Projects"},
            {eventType : "Projects that need a support"}
        ];

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.events.id !== null) {
                Events.update(vm.events, onSaveSuccess, onSaveError);
            } else {
                Events.save(vm.events, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('shaktiusApp:eventsUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        vm.datePickerOpenStatus.date = false;

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();

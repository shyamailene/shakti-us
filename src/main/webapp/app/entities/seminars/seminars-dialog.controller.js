(function() {
    'use strict';

    angular
        .module('shaktiusApp')
        .controller('SeminarsDialogController', SeminarsDialogController);

    SeminarsDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Seminars'];

    function SeminarsDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Seminars) {
        var vm = this;

        vm.seminars = entity;
        vm.clear = clear;
        vm.save = save;
        vm.months = [
            {month:"January"},
            {month:"February"},
            {month:"March"},
            {month:"April"},
            {month:"May"},
            {month:"June"},
            {month:"July"},
            {month:"August"},
            {month:"September"},
            {month:"October"},
            {month:"November"},
            {month:"December"}
        ]

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.seminars.id !== null) {
                Seminars.update(vm.seminars, onSaveSuccess, onSaveError);
            } else {
                Seminars.save(vm.seminars, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('shaktiusApp:seminarsUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();

(function() {
    'use strict';

    angular
        .module('shaktiusApp')
        .controller('LocationsDialogController', LocationsDialogController);

    LocationsDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Locations'];

    function LocationsDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Locations) {
        var vm = this;

        vm.locations = entity;
        vm.clear = clear;
        vm.save = save;

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.locations.id !== null) {
                Locations.update(vm.locations, onSaveSuccess, onSaveError);
            } else {
                Locations.save(vm.locations, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('shaktiusApp:locationsUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();

(function() {
    'use strict';

    angular
        .module('shaktiusApp')
        .controller('CommitteeDialogController', CommitteeDialogController);

    CommitteeDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Committee'];

    function CommitteeDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Committee) {
        var vm = this;

        vm.committee = entity;
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
            if (vm.committee.id !== null) {
                Committee.update(vm.committee, onSaveSuccess, onSaveError);
            } else {
                Committee.save(vm.committee, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('shaktiusApp:committeeUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();

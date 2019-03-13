(function() {
    'use strict';

    angular
        .module('shaktiusApp')
        .controller('fundraiserDialogController', fundraiserDialogController);

    fundraiserDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'fundraiser'];

    function fundraiserDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, fundraiser) {
        var vm = this;

        vm.fundraiser = entity;
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
            if (vm.fundraiser.id !== null) {
                fundraiser.update(vm.fundraiser, onSaveSuccess, onSaveError);
            } else {
                fundraiser.save(vm.fundraiser, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('shaktiusApp:fundraiserUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();

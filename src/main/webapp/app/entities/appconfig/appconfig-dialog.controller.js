(function() {
    'use strict';

    angular
        .module('shaktiusApp')
        .controller('AppconfigDialogController', AppconfigDialogController);

    AppconfigDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Appconfig'];

    function AppconfigDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Appconfig) {
        var vm = this;

        vm.appconfig = entity;
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
            if (vm.appconfig.id !== null) {
                Appconfig.update(vm.appconfig, onSaveSuccess, onSaveError);
            } else {
                Appconfig.save(vm.appconfig, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('shaktiusApp:appconfigUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();

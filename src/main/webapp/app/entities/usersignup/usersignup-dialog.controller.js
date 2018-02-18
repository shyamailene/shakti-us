(function() {
    'use strict';

    angular
        .module('shaktiusApp')
        .controller('UsersignupDialogController', UsersignupDialogController);

    UsersignupDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Usersignup'];

    function UsersignupDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Usersignup) {
        var vm = this;

        vm.usersignup = entity;
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
            if (vm.usersignup.id !== null) {
                Usersignup.update(vm.usersignup, onSaveSuccess, onSaveError);
            } else {
                Usersignup.save(vm.usersignup, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('shaktiusApp:usersignupUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();

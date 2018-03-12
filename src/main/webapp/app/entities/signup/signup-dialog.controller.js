(function() {
    'use strict';

    angular
        .module('shaktiusApp')
        .controller('SignupDialogController', SignupDialogController);

    SignupDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Signup'];

    function SignupDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Signup) {
        var vm = this;

        vm.signup = entity;
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
            if (vm.signup.id !== null) {
                Signup.update(vm.signup, onSaveSuccess, onSaveError);
            } else {
                Signup.save(vm.signup, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('shaktiusApp:signupUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();

(function() {
    'use strict';

    angular
        .module('shaktiusApp')
        .controller('UsersignupMenuController', UsersignupMenuController);

    UsersignupMenuController.$inject = ['$timeout', '$scope', '$state', '$stateParams', 'entity', 'Usersignup', 'reCAPTCHA'];

    function UsersignupMenuController ($timeout, $scope, $state, $stateParams, entity, Usersignup, reCAPTCHA) {
        var vm = this;
        vm.usersignup = entity;
        vm.clear = clear;
        vm.save = save;

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            //$uibModalInstance.dismiss('cancel');
        }

        reCAPTCHA.setPublicKey('6Le-AkcUAAAAAMG0USnFXQ1dx_tazqmdSC2QgX94');

        $scope.register = function () {
            if($scope.editForm.$valid) {
                $scope.showdialog = true;
                console.log('Form is valid');
            }
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
            //$scope.$emit('shaktiusApp:usersignupUpdate', result);
            //$uibModalInstance.close(result);
            vm.isSaving = false;
            $state.go('regSuccess')
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();

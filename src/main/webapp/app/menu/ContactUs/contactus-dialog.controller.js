(function() {
    'use strict';

    angular
        .module('shaktiusApp')
        .controller('ContactusMenuController', ContactusMenuController);

    ContactusMenuController.$inject = ['$timeout', '$scope', '$state', '$stateParams', 'entity', 'Contactus'];

    function ContactusMenuController ($timeout, $scope, $state, $stateParams, entity, Contactus) {
        var vm = this;

        vm.contactus = entity;
        vm.clear = clear;
        vm.save = save;
        vm.related = [
            {type:"Activities"},
            {type:"Events"},
            {type:"Seminars"},
            {type:"Locations"},
            {type:"Feedback"},
            {type:"Testimonial"},
            {type:"Other"}
        ]

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            //$uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.contactus.id !== null) {
                Contactus.update(vm.contactus, onSaveSuccess, onSaveError);
            } else {
                Contactus.save(vm.contactus, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            //$scope.$emit('shaktiusApp:contactusUpdate', result);
            //$uibModalInstance.close(result);
            vm.isSaving = false;
            $state.go('success')
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();

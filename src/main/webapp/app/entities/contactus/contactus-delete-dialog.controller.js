(function() {
    'use strict';

    angular
        .module('shaktiusApp')
        .controller('ContactusDeleteController',ContactusDeleteController);

    ContactusDeleteController.$inject = ['$uibModalInstance', 'entity', 'Contactus'];

    function ContactusDeleteController($uibModalInstance, entity, Contactus) {
        var vm = this;

        vm.contactus = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Contactus.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();

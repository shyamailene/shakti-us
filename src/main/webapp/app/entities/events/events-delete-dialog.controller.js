(function() {
    'use strict';

    angular
        .module('shaktiusApp')
        .controller('EventsDeleteController',EventsDeleteController);

    EventsDeleteController.$inject = ['$uibModalInstance', 'entity', 'Events'];

    function EventsDeleteController($uibModalInstance, entity, Events) {
        var vm = this;

        vm.events = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Events.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();

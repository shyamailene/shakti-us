(function() {
    'use strict';

    angular
        .module('shaktiusApp')
        .controller('CommitteeController', CommitteeController);

    CommitteeController.$inject = ['Committee'];

    function CommitteeController(Committee) {

        var vm = this;

        vm.committees = [];

        loadAll();

        function loadAll() {
            Committee.query(function(result) {
                vm.committees = result;
                vm.searchQuery = null;
            });
        }
    }
})();

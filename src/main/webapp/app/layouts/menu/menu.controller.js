(function() {
    'use strict';

    MenubarController.$inject = ['$state', 'Auth', 'Principal', 'ProfileService', 'LoginService'];

    angular
        .module('shaktiusApp')
        .controller('MenuController', MenubarController);

    function MenubarController ($state, Auth, Principal, ProfileService, LoginService) {
        var vm = this;

        vm.isMenubarCollapsed = true;
        vm.isAuthenticated = Principal.isAuthenticated;

        ProfileService.getProfileInfo().then(function(response) {
            vm.inProduction = response.inProduction;
            vm.swaggerEnabled = response.swaggerEnabled;
        });

        vm.login = login;
        vm.logout = logout;
        vm.toggleMenubar = toggleMenubar;
        vm.collapseMenubar = collapseMenubar;
        vm.$state = $state;

        function login() {
            collapseMenubar();
            LoginService.open();
        }

        function logout() {
            collapseMenubar();
            Auth.logout();
            $state.go('home');
        }

        function toggleMenubar() {
            vm.isMenubarCollapsed = !vm.isMenubarCollapsed;
        }

        function collapseMenubar() {
            vm.isMenubarCollapsed = true;
        }
    }
})();

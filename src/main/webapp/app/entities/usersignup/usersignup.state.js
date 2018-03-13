(function() {
    'use strict';

    angular
        .module('shaktiusApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('usersignup', {
            parent: 'entity',
            url: '/usersignup?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'shaktiusApp.usersignup.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/usersignup/usersignups.html',
                    controller: 'UsersignupController',
                    controllerAs: 'vm'
                }
            },
            params: {
                page: {
                    value: '1',
                    squash: true
                },
                sort: {
                    value: 'id,asc',
                    squash: true
                },
                search: null
            },
            resolve: {
                pagingParams: ['$stateParams', 'PaginationUtil', function ($stateParams, PaginationUtil) {
                    return {
                        page: PaginationUtil.parsePage($stateParams.page),
                        sort: $stateParams.sort,
                        predicate: PaginationUtil.parsePredicate($stateParams.sort),
                        ascending: PaginationUtil.parseAscending($stateParams.sort),
                        search: $stateParams.search
                    };
                }],
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('usersignup');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('usersignup-detail', {
            parent: 'usersignup',
            url: '/usersignup/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'shaktiusApp.usersignup.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/usersignup/usersignup-detail.html',
                    controller: 'UsersignupDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('usersignup');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Usersignup', function($stateParams, Usersignup) {
                    return Usersignup.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'usersignup',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('usersignup-detail.edit', {
            parent: 'usersignup-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/usersignup/usersignup-dialog.html',
                    controller: 'UsersignupDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Usersignup', function(Usersignup) {
                            return Usersignup.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('usersignup.new', {
            parent: 'usersignup',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/usersignup/usersignup-dialog.html',
                    controller: 'UsersignupDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                name: null,
                                phonenumber: null,
                                email: null,
                                notes: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('usersignup', null, { reload: 'usersignup' });
                }, function() {
                    $state.go('usersignup');
                });
            }]
        })
        .state('usersignup.edit', {
            parent: 'usersignup',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/usersignup/usersignup-dialog.html',
                    controller: 'UsersignupDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Usersignup', function(Usersignup) {
                            return Usersignup.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('usersignup', null, { reload: 'usersignup' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('usersignup.delete', {
            parent: 'usersignup',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/usersignup/usersignup-delete-dialog.html',
                    controller: 'UsersignupDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Usersignup', function(Usersignup) {
                            return Usersignup.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('usersignup', null, { reload: 'usersignup' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();

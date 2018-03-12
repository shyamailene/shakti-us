(function() {
    'use strict';

    angular
        .module('shaktiusApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('signup', {
            parent: 'entity',
            url: '/signup?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'shaktiusApp.signup.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/signup/signups.html',
                    controller: 'SignupController',
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
                    $translatePartialLoader.addPart('signup');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('signup-detail', {
            parent: 'signup',
            url: '/signup/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'shaktiusApp.signup.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/signup/signup-detail.html',
                    controller: 'SignupDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('signup');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Signup', function($stateParams, Signup) {
                    return Signup.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'signup',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('signup-detail.edit', {
            parent: 'signup-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/signup/signup-dialog.html',
                    controller: 'SignupDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Signup', function(Signup) {
                            return Signup.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('signup.new', {
            parent: 'signup',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/signup/signup-dialog.html',
                    controller: 'SignupDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                firstName: null,
                                lastName: null,
                                email: null,
                                phone: null,
                                line1: null,
                                line2: null,
                                city: null,
                                state: null,
                                country: null,
                                zipcode: null,
                                parentFName: null,
                                parentLName: null,
                                parentEmail: null,
                                parentPhone: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('signup', null, { reload: 'signup' });
                }, function() {
                    $state.go('signup');
                });
            }]
        })
        .state('signup.edit', {
            parent: 'signup',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/signup/signup-dialog.html',
                    controller: 'SignupDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Signup', function(Signup) {
                            return Signup.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('signup', null, { reload: 'signup' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('signup.delete', {
            parent: 'signup',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/signup/signup-delete-dialog.html',
                    controller: 'SignupDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Signup', function(Signup) {
                            return Signup.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('signup', null, { reload: 'signup' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();

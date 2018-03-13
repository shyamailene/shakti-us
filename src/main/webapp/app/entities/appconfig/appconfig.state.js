(function() {
    'use strict';

    angular
        .module('shaktiusApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('appconfig', {
            parent: 'entity',
            url: '/appconfig?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'shaktiusApp.appconfig.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/appconfig/appconfigs.html',
                    controller: 'AppconfigController',
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
                    $translatePartialLoader.addPart('appconfig');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('appconfig-detail', {
            parent: 'appconfig',
            url: '/appconfig/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'shaktiusApp.appconfig.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/appconfig/appconfig-detail.html',
                    controller: 'AppconfigDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('appconfig');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Appconfig', function($stateParams, Appconfig) {
                    return Appconfig.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'appconfig',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('appconfig-detail.edit', {
            parent: 'appconfig-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/appconfig/appconfig-dialog.html',
                    controller: 'AppconfigDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Appconfig', function(Appconfig) {
                            return Appconfig.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('appconfig.new', {
            parent: 'appconfig',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/appconfig/appconfig-dialog.html',
                    controller: 'AppconfigDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                key: null,
                                value: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('appconfig', null, { reload: 'appconfig' });
                }, function() {
                    $state.go('appconfig');
                });
            }]
        })
        .state('appconfig.edit', {
            parent: 'appconfig',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/appconfig/appconfig-dialog.html',
                    controller: 'AppconfigDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Appconfig', function(Appconfig) {
                            return Appconfig.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('appconfig', null, { reload: 'appconfig' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('appconfig.delete', {
            parent: 'appconfig',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/appconfig/appconfig-delete-dialog.html',
                    controller: 'AppconfigDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Appconfig', function(Appconfig) {
                            return Appconfig.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('appconfig', null, { reload: 'appconfig' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();

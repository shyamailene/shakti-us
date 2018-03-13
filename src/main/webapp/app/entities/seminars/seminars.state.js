(function() {
    'use strict';

    angular
        .module('shaktiusApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('seminars', {
            parent: 'entity',
            url: '/seminars?page&sort&search',
            data: {
                //authorities: ['ROLE_USER'],
                pageTitle: 'shaktiusApp.seminars.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/seminars/seminars.html',
                    controller: 'SeminarsController',
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
                    $translatePartialLoader.addPart('seminars');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
            .state('seminarss', {
                parent: 'entity',
                url: '/seminarss?page&sort&search',
                data: {
                    //authorities: ['ROLE_USER'],
                    pageTitle: 'shaktiusApp.seminars.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'app/menu/seminars/seminars.html',
                        controller: 'SeminarsController',
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
                        $translatePartialLoader.addPart('seminars');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
        .state('seminars-detail', {
            parent: 'seminars',
            url: '/seminars/{id}',
            data: {
                //authorities: ['ROLE_USER'],
                pageTitle: 'shaktiusApp.seminars.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/seminars/seminars-detail.html',
                    controller: 'SeminarsDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('seminars');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Seminars', function($stateParams, Seminars) {
                    return Seminars.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'seminars',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('seminars-detail.edit', {
            parent: 'seminars-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/seminars/seminars-dialog.html',
                    controller: 'SeminarsDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Seminars', function(Seminars) {
                            return Seminars.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('seminars.new', {
            parent: 'seminars',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/seminars/seminars-dialog.html',
                    controller: 'SeminarsDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                topic: null,
                                location: null,
                                contact: null,
                                month: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('seminars', null, { reload: 'seminars' });
                }, function() {
                    $state.go('seminars');
                });
            }]
        })
        .state('seminars.edit', {
            parent: 'seminars',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/seminars/seminars-dialog.html',
                    controller: 'SeminarsDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Seminars', function(Seminars) {
                            return Seminars.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('seminars', null, { reload: 'seminars' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('seminars.delete', {
            parent: 'seminars',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/seminars/seminars-delete-dialog.html',
                    controller: 'SeminarsDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Seminars', function(Seminars) {
                            return Seminars.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('seminars', null, { reload: 'seminars' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();

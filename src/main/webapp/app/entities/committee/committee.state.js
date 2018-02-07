(function() {
    'use strict';

    angular
        .module('shaktiusApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('committee', {
            parent: 'entity',
            url: '/committee',
            data: {
                //authorities: ['ROLE_USER'],
                pageTitle: 'shaktiusApp.committee.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/committee/committees.html',
                    controller: 'CommitteeController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('committee');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('committee-detail', {
            parent: 'committee',
            url: '/committee/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'shaktiusApp.committee.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/committee/committee-detail.html',
                    controller: 'CommitteeDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('committee');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Committee', function($stateParams, Committee) {
                    return Committee.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'committee',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('committee-detail.edit', {
            parent: 'committee-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/committee/committee-dialog.html',
                    controller: 'CommitteeDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Committee', function(Committee) {
                            return Committee.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('committee.new', {
            parent: 'committee',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/committee/committee-dialog.html',
                    controller: 'CommitteeDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                name: null,
                                title: null,
                                email: null,
                                mobile: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('committee', null, { reload: 'committee' });
                }, function() {
                    $state.go('committee');
                });
            }]
        })
        .state('committee.edit', {
            parent: 'committee',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/committee/committee-dialog.html',
                    controller: 'CommitteeDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Committee', function(Committee) {
                            return Committee.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('committee', null, { reload: 'committee' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('committee.delete', {
            parent: 'committee',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/committee/committee-delete-dialog.html',
                    controller: 'CommitteeDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Committee', function(Committee) {
                            return Committee.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('committee', null, { reload: 'committee' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();

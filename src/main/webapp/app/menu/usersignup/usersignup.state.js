(function() {
    'use strict';

    angular
        .module('shaktiusApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('usersignupmenu', {
            parent: 'entity',
            url: '/usersignupss?page&sort&search',
            data: {
                //authorities: ['ROLE_USER'],
                pageTitle: 'shaktiusApp.usersignup.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/menu/usersignup/usersignup.html',
                    controller: 'UsersignupMenuController',
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
                entity: function () {
                    return {
                        name: null,
                        phonenumber: null,
                        email: null,
                        notes: null,
                        id: null
                    };
                },
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
            .state('regSuccess', {
                parent: 'entity',
                url: '/regSuccess',
                data: {
                    //authorities: ['ROLE_USER'],
                    pageTitle: 'shaktiusApp.contactus.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'app/menu/usersignup/success.html',
                        controller: 'ContactusMenuController',
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
                    entity: function () {
                        return {
                            email: null,
                            mobile: null,
                            relatedto: null,
                            content: null,
                            id: null
                        };
                    },
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('contactus');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            });
    }

})();

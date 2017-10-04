(function() {
    'use strict';

    angular
        .module('webHipsterApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('filme', {
            parent: 'entity',
            url: '/filme',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'webHipsterApp.filme.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/filme/filmes.html',
                    controller: 'FilmeController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('filme');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('filme-detail', {
            parent: 'filme',
            url: '/filme/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'webHipsterApp.filme.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/filme/filme-detail.html',
                    controller: 'FilmeDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('filme');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Filme', function($stateParams, Filme) {
                    return Filme.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'filme',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('filme-detail.edit', {
            parent: 'filme-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/filme/filme-dialog.html',
                    controller: 'FilmeDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Filme', function(Filme) {
                            return Filme.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('filme.new', {
            parent: 'filme',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/filme/filme-dialog.html',
                    controller: 'FilmeDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                nome: null,
                                lancamento: null,
                                descricao: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('filme', null, { reload: 'filme' });
                }, function() {
                    $state.go('filme');
                });
            }]
        })
        .state('filme.edit', {
            parent: 'filme',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/filme/filme-dialog.html',
                    controller: 'FilmeDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Filme', function(Filme) {
                            return Filme.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('filme', null, { reload: 'filme' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('filme.delete', {
            parent: 'filme',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/filme/filme-delete-dialog.html',
                    controller: 'FilmeDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Filme', function(Filme) {
                            return Filme.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('filme', null, { reload: 'filme' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();

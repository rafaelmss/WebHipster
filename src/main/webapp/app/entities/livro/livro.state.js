(function() {
    'use strict';

    angular
        .module('webHipsterApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('livro', {
            parent: 'entity',
            url: '/livro',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'webHipsterApp.livro.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/livro/livros.html',
                    controller: 'LivroController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('livro');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('livro-detail', {
            parent: 'livro',
            url: '/livro/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'webHipsterApp.livro.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/livro/livro-detail.html',
                    controller: 'LivroDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('livro');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Livro', function($stateParams, Livro) {
                    return Livro.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'livro',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('livro-detail.edit', {
            parent: 'livro-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/livro/livro-dialog.html',
                    controller: 'LivroDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Livro', function(Livro) {
                            return Livro.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('livro.new', {
            parent: 'livro',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/livro/livro-dialog.html',
                    controller: 'LivroDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                nome: null,
                                autor: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('livro', null, { reload: 'livro' });
                }, function() {
                    $state.go('livro');
                });
            }]
        })
        .state('livro.edit', {
            parent: 'livro',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/livro/livro-dialog.html',
                    controller: 'LivroDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Livro', function(Livro) {
                            return Livro.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('livro', null, { reload: 'livro' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('livro.delete', {
            parent: 'livro',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/livro/livro-delete-dialog.html',
                    controller: 'LivroDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Livro', function(Livro) {
                            return Livro.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('livro', null, { reload: 'livro' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();

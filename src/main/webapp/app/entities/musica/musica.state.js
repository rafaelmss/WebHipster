(function() {
    'use strict';

    angular
        .module('webHipsterApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('musica', {
            parent: 'entity',
            url: '/musica',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'webHipsterApp.musica.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/musica/musicas.html',
                    controller: 'MusicaController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('musica');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('musica-detail', {
            parent: 'musica',
            url: '/musica/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'webHipsterApp.musica.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/musica/musica-detail.html',
                    controller: 'MusicaDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('musica');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Musica', function($stateParams, Musica) {
                    return Musica.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'musica',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('musica-detail.edit', {
            parent: 'musica-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/musica/musica-dialog.html',
                    controller: 'MusicaDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Musica', function(Musica) {
                            return Musica.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('musica.new', {
            parent: 'musica',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/musica/musica-dialog.html',
                    controller: 'MusicaDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                nome: null,
                                lancamento: null,
                                artista: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('musica', null, { reload: 'musica' });
                }, function() {
                    $state.go('musica');
                });
            }]
        })
        .state('musica.edit', {
            parent: 'musica',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/musica/musica-dialog.html',
                    controller: 'MusicaDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Musica', function(Musica) {
                            return Musica.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('musica', null, { reload: 'musica' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('musica.delete', {
            parent: 'musica',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/musica/musica-delete-dialog.html',
                    controller: 'MusicaDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Musica', function(Musica) {
                            return Musica.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('musica', null, { reload: 'musica' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();

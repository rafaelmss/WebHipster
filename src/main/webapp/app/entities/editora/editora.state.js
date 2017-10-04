(function() {
    'use strict';

    angular
        .module('webHipsterApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('editora', {
            parent: 'entity',
            url: '/editora',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'webHipsterApp.editora.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/editora/editoras.html',
                    controller: 'EditoraController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('editora');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('editora-detail', {
            parent: 'editora',
            url: '/editora/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'webHipsterApp.editora.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/editora/editora-detail.html',
                    controller: 'EditoraDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('editora');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Editora', function($stateParams, Editora) {
                    return Editora.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'editora',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('editora-detail.edit', {
            parent: 'editora-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/editora/editora-dialog.html',
                    controller: 'EditoraDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Editora', function(Editora) {
                            return Editora.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('editora.new', {
            parent: 'editora',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/editora/editora-dialog.html',
                    controller: 'EditoraDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                nome: null,
                                endereco: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('editora', null, { reload: 'editora' });
                }, function() {
                    $state.go('editora');
                });
            }]
        })
        .state('editora.edit', {
            parent: 'editora',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/editora/editora-dialog.html',
                    controller: 'EditoraDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Editora', function(Editora) {
                            return Editora.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('editora', null, { reload: 'editora' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('editora.delete', {
            parent: 'editora',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/editora/editora-delete-dialog.html',
                    controller: 'EditoraDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Editora', function(Editora) {
                            return Editora.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('editora', null, { reload: 'editora' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();

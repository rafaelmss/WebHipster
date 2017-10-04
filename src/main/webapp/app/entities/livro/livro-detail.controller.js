(function() {
    'use strict';

    angular
        .module('webHipsterApp')
        .controller('LivroDetailController', LivroDetailController);

    LivroDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Livro', 'Editora'];

    function LivroDetailController($scope, $rootScope, $stateParams, previousState, entity, Livro, Editora) {
        var vm = this;

        vm.livro = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('webHipsterApp:livroUpdate', function(event, result) {
            vm.livro = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();

(function() {
    'use strict';

    angular
        .module('webHipsterApp')
        .controller('EditoraDetailController', EditoraDetailController);

    EditoraDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Editora'];

    function EditoraDetailController($scope, $rootScope, $stateParams, previousState, entity, Editora) {
        var vm = this;

        vm.editora = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('webHipsterApp:editoraUpdate', function(event, result) {
            vm.editora = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();

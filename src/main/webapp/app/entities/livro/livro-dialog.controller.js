(function() {
    'use strict';

    angular
        .module('webHipsterApp')
        .controller('LivroDialogController', LivroDialogController);

    LivroDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Livro', 'Editora'];

    function LivroDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Livro, Editora) {
        var vm = this;

        vm.livro = entity;
        vm.clear = clear;
        vm.save = save;
        vm.editoras = Editora.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.livro.id !== null) {
                Livro.update(vm.livro, onSaveSuccess, onSaveError);
            } else {
                Livro.save(vm.livro, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('webHipsterApp:livroUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();

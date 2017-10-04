(function() {
    'use strict';

    angular
        .module('webHipsterApp')
        .controller('EditoraDialogController', EditoraDialogController);

    EditoraDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Editora'];

    function EditoraDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Editora) {
        var vm = this;

        vm.editora = entity;
        vm.clear = clear;
        vm.save = save;

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.editora.id !== null) {
                Editora.update(vm.editora, onSaveSuccess, onSaveError);
            } else {
                Editora.save(vm.editora, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('webHipsterApp:editoraUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();

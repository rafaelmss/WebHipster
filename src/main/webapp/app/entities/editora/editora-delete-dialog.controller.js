(function() {
    'use strict';

    angular
        .module('webHipsterApp')
        .controller('EditoraDeleteController',EditoraDeleteController);

    EditoraDeleteController.$inject = ['$uibModalInstance', 'entity', 'Editora'];

    function EditoraDeleteController($uibModalInstance, entity, Editora) {
        var vm = this;

        vm.editora = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Editora.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();

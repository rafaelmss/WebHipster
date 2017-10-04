(function() {
    'use strict';

    angular
        .module('webHipsterApp')
        .controller('FilmeDialogController', FilmeDialogController);

    FilmeDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Filme'];

    function FilmeDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Filme) {
        var vm = this;

        vm.filme = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.filme.id !== null) {
                Filme.update(vm.filme, onSaveSuccess, onSaveError);
            } else {
                Filme.save(vm.filme, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('webHipsterApp:filmeUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        vm.datePickerOpenStatus.lancamento = false;

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();

$appcontrolers.controller('PlaylistsCtrl', function($scope) {

	$scope.$on('$ionicView.beforeEnter', function() {
		BaasBox.setEndPoint("http://beerbarsapp.com:9000");
		BaasBox.appcode = "1234567890";
	    
		 this.show = function() {
		    $ionicLoading.show({
		      template: 'Carregando...'
		    });
		  };
		
		if (BaasBox.getCurrentUser().token != null){
			BaasBox.loadCollection("Estabelecimento")
			.done(function(res) {
				$scope.estabelecimentos = res;
				console.log("res ", res);
			}).fail(function(error) {
				console.log("error ", error);
			});
		}
		
		$scope.hide = function(){
			$ionicLoading.hide();
		};
	});
	

})

.controller('PlaylistCtrl', function($scope, $stateParams) {
});
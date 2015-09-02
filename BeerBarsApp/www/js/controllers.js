var $appcontrolers = angular.module('starter.controllers', [])

.controller('AppCtrl', function($scope, $ionicModal, $timeout, $translate) {

  // With the new view caching in Ionic, Controllers are only called
  // when they are recreated or on app start, instead of every page change.
  // To listen for when this page is active (for example, to refresh data),
  // listen for the $ionicView.enter event:
  //$scope.$on('$ionicView.enter', function(e) {
  //});

  // Form data for the login modal
  $scope.loginData = {};
  
  if (BaasBox.getCurrentUser()){
  	$scope.user = BaasBox.user;
  }

  // Create the login modal that we will use later
  $ionicModal.fromTemplateUrl('templates/login.html', {
    scope: $scope
  }).then(function(modal) {
    $scope.modal = modal;
  });

  // Triggered in the login modal to close it
  $scope.closeLogin = function() {
    $scope.modal.hide();
  };

  // Open the login modal
  $scope.login = function() {
    $scope.modal.show();
  };

  // Perform the login action when the user submits the login form
  $scope.doLogin = function() {
	$scope.loading = true;
	BaasBox.setEndPoint("http://beerbarsapp.com:9000");
	BaasBox.appcode = "1234567890";
    
    BaasBox.login($scope.loginData.username, $scope.loginData.password)
	.done(function (user) {
		$scope.user = user;
		
		console.log("Logged in ", user);
		$scope.closeLogin();
	})
	.fail(function (err) {
		console.log("error ", err);
	});
    
    $scope.loading = false;
	
  };
});

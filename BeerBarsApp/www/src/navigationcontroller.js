angular.module('controllers.navigation', [])
   
.controller('NavigationController', function($scope){
    var nc = this;

    $scope.$on('$stateChangeSuccess', function(event, toState) {
    	nc.currentTab = toState.data.selectedTab;
    });
	
});

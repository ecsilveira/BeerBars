 angular.module('starter', ['ngMaterial','pascalprecht.translate', 'controllers.navigation', 'ui.router', 'bares']) 
 
 .config(function($mdThemingProvider, $mdIconProvider){
	  $mdIconProvider.defaultIconSet("./assets/svg/avatars.svg", 128)
	  .icon("menu"       , "./assets/svg/menu.svg"        , 24)
	  .icon("share"      , "./assets/svg/share.svg"       , 24)
	  .icon("google_plus", "./assets/svg/google_plus.svg" , 512)
	  .icon("hangouts"   , "./assets/svg/hangouts.svg"    , 512)
	  .icon("twitter"    , "./assets/svg/twitter.svg"     , 512)
	  .icon("phone"      , "./assets/svg/phone.svg"       , 512);
	  
	  $mdThemingProvider.theme('default').primaryPalette('blue').accentPalette('red');
  })
  
.config(function($stateProvider, $urlRouterProvider) {
    
    $stateProvider
        
        // HOME STATES AND NESTED VIEWS ========================================
        .state('home', {
            url: '/',
            templateUrl: 'app/bares.html'
        })
        .state('login', {
            url: '/login',
            templateUrl: 'app/login.html',
        })
        .state('bares', {
            url: '/bares',
            templateUrl: 'app/bares.html',
            controller: 'BaresController',
            controllerAs: 'baresctrl'
        })
        
        // ABOUT PAGE AND MULTIPLE NAMED VIEWS =================================
        .state('about', {
            // we'll get to this in a bit
        });
    
    $urlRouterProvider.otherwise('/home');
  }
);
              
//  .config(function($stateProvider, $urlRouterProvider) {
//	  $stateProvider
//	    .state('login', {
//	        url: '/',
//	        data: {
//	            'selectedTab' : 0
//	        },
//	        templateUrl: 'app/login.html',
//	        controller: 'LoginControler as logincrtl'
//	    })
//	    .state('bares', {
//	        url: '/bares',
//	        data: {
//	            'selectedTab' : 1
//	        },
//	        templateUrl: 'app/bares.html',
//	        controller: 'PortfolioController as vm'
//	    });
//  });
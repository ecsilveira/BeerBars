 var $app = angular.module('starter', ['ngMaterial','ngRoute','pascalprecht.translate']) 
 
 .config(function($mdThemingProvider, $mdIconProvider){
	  $mdIconProvider.defaultIconSet("./assets/svg/avatars.svg", 128)
	  .icon("menu"       , "./assets/svg/menu.svg"        , 24)
	  .icon("share"      , "./assets/svg/share.svg"       , 24)
	  .icon("google_plus", "./assets/svg/google_plus.svg" , 512)
	  .icon("hangouts"   , "./assets/svg/hangouts.svg"    , 512)
	  .icon("twitter"    , "./assets/svg/twitter.svg"     , 512)
	  .icon("phone"      , "./assets/svg/phone.svg"       , 512);
	  
	  $mdThemingProvider.theme('default').primaryPalette('blue').accentPalette('red');
  });
              
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
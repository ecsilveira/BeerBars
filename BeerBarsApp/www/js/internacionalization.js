$app.config(['$translateProvider', function($translateProvider) {
	$translateProvider.translations('en', {
		'LABEL.GENERAL.CLOSE'  : 'Close',
		'LABEL.LOGIN.TITLE'    : 'Login',
		'LABEL.LOGIN.USERNAME' : 'Username',
		'LABEL.LOGIN.PASSWORD' : 'Password',
		'LABEL.LOGIN.LOGIN'    : 'Log In'
	})
	.translations('pt', {
		'LABEL.GENERAL.CLOSE'  : 'Fechar',
		'LABEL.LOGIN.TITLE'    : 'Login',
		'LABEL.LOGIN.USERNAME' : 'Usu\u00E1rio',
		'LABEL.LOGIN.PASSWORD' : 'Senha',
		'LABEL.LOGIN.LOGIN'    : 'Entrar'
	});

	$translateProvider.preferredLanguage('pt'); //temos que dizer qual é a default.
	$translateProvider.useSanitizeValueStrategy('escaped');
} ]);

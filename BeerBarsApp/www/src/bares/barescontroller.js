angular.module('bares', [])
       .controller('BaresController', function($scope){
    	   var self = this;
    	   
    	   self.bares = [{ nomeBar: 'Phone', imagemCapa: 'assets/svg/avatar-1.svg', endereco: 'Rua Caldas Jr. 120', bairro: 'Centro Historico', telefone: '51 3331-5544', distancia: 0.225, mediaAvalicao: 8.7, promocao: false},
    	                  { nomeBar: 'Cervejas Tops', imagemCapa: 'assets/svg/avatar-1.svg', endereco: 'Rua Alameda, 740', bairro: 'Cidade Baixa', telefone: '51 3434-3433', distancia: 0.800, mediaAvalicao: 9, promocao: true},
    	                  { nomeBar: 'Panela da Boa', imagemCapa: 'assets/svg/avatar-1.svg', endereco: 'Rua General Camara, 174', bairro: 'Bom Fim', telefone: '51 1231-1122', distancia: 1.425, mediaAvalicao: 7.4, promocao: false},
    	                  { nomeBar: 'Homebrewers', imagemCapa: 'assets/svg/avatar-1.svg', endereco: 'Rua Lima e Silva, 874', bairro: 'Cidade Baixa', telefone: '51 6666-870-', distancia: 0.600, mediaAvalicao: 5.5, promocao: false},
    	                  { nomeBar: 'Vendo Cerveja', imagemCapa: 'assets/svg/avatar-1.svg', endereco: 'Av. João Pessoa, 6120', bairro: 'Moinhos de Vento', telefone: '51 8888-9876', distancia: 3.554, mediaAvalicao: 8.1, promocao: true},
    	                  { nomeBar: 'Cerveja Free', imagemCapa: 'assets/svg/avatar-1.svg', endereco: 'Rua da Republica, 720', bairro: 'Moinhos de Vento', telefone: '54 2222-5322', distancia: 8.225, mediaAvalicao: 6.5, promocao: false},
    	                  { nomeBar: 'Beer Bar', imagemCapa: 'assets/svg/avatar-1.svg', endereco: 'Rua Caldas Jr. 136', bairro: 'Cidade Baixe', telefone: '51 3331-4444', distancia: 15.665, mediaAvalicao: 7.9, promocao: false}
         ];
    	   
       });
          
  
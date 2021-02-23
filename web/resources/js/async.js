function getTablero(){ 
$.ajax({
url     : '/ajaxhandler',
method     : 'GET',
data     : {PARTIDA : idpartida },
success    : refrescaTablero()});
alert("refresco");
}

modificaTablero(){

}

setInterval(function(){
getTablero() // se refresca cada 5 segundos
}, 5000);
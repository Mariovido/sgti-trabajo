function refresh(){ 
var id = $(this).attr('id');
var col = id.charAt(1);
$.ajax({
url     : '/ajaxhandler',
method     : 'GET',
data     : {PARTIDA : idpartida },
success    : modificaTablero()});
alert("refresco");
}

modificaTablero(){

}

setInterval(function(){
refresh() // se refresca cada 5 segundos
}, 5000);
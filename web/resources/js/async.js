function getTablero(){ 
$.ajax({
url     : '/ajaxhandler',
method     : 'GET',
data     : {PARTIDA : idpartida },
success    : refrescaTablero()});
alert("refresco");
}

getTablero();

function refrescaTablero(matrizjson){
    //sacar param
    var matriz = JSON.stringify(matrizjson);
    paintInit(matriz);
}

setInterval(function(){
getTablero() // se refresca cada 5 segundos
}, 5000);
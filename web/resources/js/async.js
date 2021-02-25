function getTablero(){ 
$.ajax({
url     : '/sgti-trabajo/ajaxhandler',
method     : 'GET',
data     : {PARTIDA : idpartida },
success    : function(response){
    var matriz = JSON.stringify(response);
    paintInit(matriz);
}
});
}

getTablero();

/*
function refrescaTablero(matrizjson){
    //sacar param
    var matriz = JSON.stringify(matrizjson);
    paintInit(matriz);
}
*/

setInterval(function(){
getTablero() // se refresca cada 5 segundos
}, 5000);
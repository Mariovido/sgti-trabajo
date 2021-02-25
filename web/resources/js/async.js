function getTablero(){ 
$.ajax({
url     : '/sgti-trabajo/ajaxhandler',
method     : 'GET',
data     : {PARTIDA : idpartida },
success    : function(response){
    let res = JSON.parse(JSON.stringify(response));
    let matriz = res.tablero;
    paintInit(matriz);
    turno = res.turno;
}
});
}

var turno = false;
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
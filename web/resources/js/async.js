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
    matrizSt = matriz;
}
});
}

var turno = false;
getTablero();

setInterval(function(){
getTablero() // se refresca cada 5 segundos
}, 5000);
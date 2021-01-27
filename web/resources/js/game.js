//matrizEjemplo[6][7] = 
var matrizSt = "0000000;0000000;0000000;0000000;0000000;0000000;0000000;0000000"; 

function paint(){
    id = paint.caller.arguments[0].target.id;
    NFila = parseInt(id.charAt(0));
    NCol = parseInt(id.charAt(1));
    col = matrizSt.substring(8*NCol,7+8*NCol);
    //alert(col);
    //alert("Fila"+NFila+"Columna"+NCol);
    for (var i=6; i>=0; i--){
        if (col.charAt(i) == "0"){
            //col.replaceAt(i,"1");
            col = col.replaceAt(i, "1");
            document.getElementById(i.toString()+NCol.toString()).className = "j1";
            //for (var j=0; j<6; j++){
            matrizSt=matrizSt.replaceAt(NCol*8+i,"1");
            //}
            break;
        }
    }
    

}

String.prototype.replaceAt = function(index, replacement) {
    return this.substr(0, index) + replacement + this.substr(index + replacement.length);
}
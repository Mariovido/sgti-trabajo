var matrizSt = "0000000;0000000;0000000;0000000;0000000;0000000;0000000"; 

function paint(){
    id = paint.caller.arguments[0].target.id;
    NFila = parseInt(id.charAt(0));
    NCol = parseInt(id.charAt(1));
    col = matrizSt.substring(7*NCol,7+7*NCol);
    for (var i=6; i>=0; i--){
        if (col.charAt(i) == "0"){
            col = col.replaceAt(i, "1");
            document.getElementById(i.toString()+NCol.toString()).className = "j1";
            matrizSt=matrizSt.replaceAt(NCol*7+i,"1");
            break;
        }
    }
}

String.prototype.replaceAt = function(index, replacement) {
    return this.substr(0, index) + replacement + this.substr(index + replacement.length);
}

function paintInit(matriz){
    
    for (var j=0; j<7; j++){
        //col = matrizSt.substring(7*j,7+7*j);
        col = matriz.substring(8*j,7+8*j);
        for (var i = 0; i<7; i++){
            if(col.charAt(i) == "1"){
                document.getElementById(i.toString()+j.toString()).className = "j1";
            }else if (col.charAt(i) == "2"){
                document.getElementById(i.toString()+j.toString()).className = "j2";  
            }
        }
    }
}
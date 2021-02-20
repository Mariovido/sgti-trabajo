function validarLogin(){
            var x = document.forms["formulario-login"]["USER"].value;
            var z = document.forms["formulario-login"]["PASS"].value;
            if (x == "" || z == "") {
            alert("Por favor, complete todos los campos");
            return false;
            }
        }
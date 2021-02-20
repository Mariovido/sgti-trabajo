
        function validarLogin(){
            var x = document.forms["formulario-login"]["USER"].value;
            var z = document.forms["formulario-login"]["PASS"].value;
            if (x == "" || z == "") {
            alert("Por favor, complete todos los campos");
            return false;
            }
        }

        function validarRegistro(){
            var x = document.forms["formulario"]["USER"].value;
            var y = document.forms["formulario"]["MAIL"].value;
            var z = document.forms["formulario"]["PASS"].value;
            if (x == "" || y == "" || z == "") {
            alert("Por favor, complete todos los campos");
            return false;
            }
        }
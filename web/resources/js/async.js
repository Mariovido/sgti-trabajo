(function() {
    var startingTime = new Date().getTime();
    // Load the script
    var script = document.createElement("SCRIPT");
    script.src = 'https://ajax.googleapis.com/ajax/libs/jquery/1.7.1/jquery.min.js';
    script.type = 'text/javascript';
    document.getElementsByTagName("head")[0].appendChild(script);

    // Poll for jQuery to come into existance
    var checkReady = function(callback) {
        if (window.jQuery) {
            callback(jQuery);
        }
        else {
            window.setTimeout(function() { checkReady(callback); }, 20);
        }
    };

    // Start polling...
    checkReady(function($) {
        $(function() {
            var endingTime = new Date().getTime();
            var tookTime = endingTime - startingTime;
            window.alert("jQuery is loaded, after " + tookTime + " milliseconds!");
        });
    });
})();

var script = document.createElement("SCRIPT");
    script.src = 'https://ajax.googleapis.com/ajax/libs/jquery/1.7.1/jquery.min.js';
    script.type = 'text/javascript';
    document.getElementsByTagName("head")[0].appendChild(script);
$(document).on("click","#test-element",function() {
    alert("click");
}); 

/*$(document).on("click", "td", function() { // Cuando hay un "click" en un td se ejecuta la siguiente funcion
                $.get("someservlet", function(responseText) {   // Execute Ajax GET request on URL of "someservlet" and execute the following function with Ajax response text...
                    $("#somediv").text(responseText);           // Locate HTML DOM element with ID "somediv" and set its text content with the response text.
                });
            });
*/
/*
$(document).on("click", "td", function() { // Cuando hay un "click" en un td se ejecuta la siguiente funcion
                $.ajax({
                	url: "/servlet",
                	method     : 'POST',
  					context: document.body
				}).done(function() {
  					$( this ).addClass( "done" );
                });
            });
*/
$(document).on("click", "td", function() { // Cuando hay un "click" en un td se ejecuta la siguiente funcion
$.ajax({
url     : '/ajaxHandler',
method     : 'POST',
data     : {columna : columna},
success    : paint()});
});

/*
function callJqueryAjax(){
var columna = $('#col').val();
$.ajax({
url     : '/ajaxHandler',
method     : 'POST',
data     : {columna : columna},
success    : function(resultText){
$('#result').html(resultText);
},
error : function(jqXHR, exception){
console.log('Error occured!!');
}
});
}
*/
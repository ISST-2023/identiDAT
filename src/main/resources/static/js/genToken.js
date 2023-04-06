//Variables globales
var token;
var years = ["1","2","3","4"];
var groups = ["1","2","3","4","5"];

//Función generadora de hash apartir de un string dado
function genToken(key){
    const splittedWord = key.toLowerCase().split("");
    const codes = splittedWord.map((letter) => `${letter}${String(letter).charCodeAt(0)}`);           
    let hash = codes.join("");
    document.getElementById("algo").innerHTML = hash;
    return hash;
}

//Función que crea el string para genToken(key) a partir de los valores de los select para la creación de los tokens
function changeString(){
    var degree = document.getElementById("grado").value;
    var year = document.getElementById("año").value;
    var group = document.getElementById("grupo").value;
    let concat;

    if (degree == "GITST") {
        if (year == "All") {

            if (group == "All") {

                years.forEach(elementY => {
                    groups.forEach(itemG => {
                        concat = degree + elementY + itemG;
                        token = genToken(concat);
                    });
                });

            } else {

                years.forEach(elementY => {
                    concat = degree + elementY + group;
                    token = genToken(concat);
                });

            }

        } else {
            
            if (group == "All") {

                groups.forEach(itemG => {
                    concat = degree + year + itemG;
                    token = genToken(concat);
                });

            } else {

                concat = degree + year + group;
                token = genToken(concat);
                
            }

        }

    } else if (degree == "GIB") {
        //preguntar a Pablo por si hay casos especiales
    } else if (degree == "MUIT") {
        //preguntar a Pablo por si hay casos especiales
    } else if (degree == "datos"){
        //preguntar a Pablo por datos
    }

}
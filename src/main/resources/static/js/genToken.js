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
    let key;
    
    if (degree == "GITST") {
        if (year == "All") {

            if (group == "All") {

                years.forEach(elementY => {
                    groups.forEach(itemG => {
                        key = degree + elementY + itemG;
                        token = genToken(key);
                    });
                });

            } else {

                years.forEach(elementY => {
                    key = degree + elementY + group;
                    token = genToken(key);
                });

            }

        } else {
            
            if (group == "All") {

                groups.forEach(itemG => {
                    key = degree + year + itemG;
                    token = genToken(key);
                });

            } else {

                key = degree + year + group;
                token = genToken(key);
                
            }

        }

    } else if (degree == "GIB") {
        //preguntar a Pablo por si hay casos especiales
    } else if (degree == "MUIT") {
        //preguntar a Pablo por si hay casos especiales
    } else {
        //preguntar a Pablo por datos
    }

}
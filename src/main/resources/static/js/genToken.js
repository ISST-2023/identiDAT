var boton = document.getElementById("bToken");
boton.addEventListener("click",genToken(boton.value));

function genToken(key){
    const splittedWord = key.toLowerCase().split("");
    const codes = splittedWord.map((letter) => `${letter}${String(letter).charCodeAt(0)}`);
    //return codes.join("");
    document.getElementById("algo").innerHTML = codes.join("");
    //console.log(codes.join(""))
}


var token;
var degree = document.getElementById(degree);
var year = document.getElementById(year);
var group = document.getElementById(group);
var years = ["1","2","3","4"];
var groups1 = ["11","12","13","14","15"];
var groups2 = ["21","22","23","24","25"];
var groups3 = ["31","32","33","34","35"];
var groups4 = ["41","42","43","44","45"];

function changeString(){
    if (degree == "GITST") {
        
        if (year == "All") {

            years.forEach(element => {
                if (element == "1"){
                    groups1.forEach(item => {
                        key = degree + element + item;
                        token = genToken(key);
                    });
                } else if (element == "2") {
                    groups2.forEach(item => {
                        key = degree + element + item;
                        token = genToken(key);
                    });
                } else if (element == "3") {
                    groups3.forEach(item => {
                        key = degree + element + item;
                        token = genToken(key);
                    });
                } else if (element == "4") {
                    groups4.forEach(item => {
                        key = degree + element + item;
                        token = genToken(key);
                    });
                }
            }); 
            
        } else {
            
            if (group == "All") {
                if (year == "1"){
                    groups1.forEach(item => {
                        key = degree + element + item;
                        token = genToken(key);
                    });
                } else if (year == "2") {
                    groups2.forEach(item => {
                        key = degree + element + item;
                        token = genToken(key);
                    });
                } else if (year == "3") {
                    groups3.forEach(item => {
                        key = degree + element + item;
                        token = genToken(key);
                    });
                } else if (year == "4") {
                    groups4.forEach(item => {
                        key = degree + element + item;
                        token = genToken(key);
                    });
                }
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
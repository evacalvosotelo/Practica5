const displayContadores = async () => {    
    const contadores = await getContadores();   // Cogemos los contadores
    contadores.forEach(element => {
        const elementContador = 
        `<div>
            <label class = "element" for="">Contador: `+element.nombre+` </label>
            <label class = "element" for="" >Valor: `+element.valor+`</label>
        </div>`;
        const lista = document.getElementById("div-contadores");
        lista.innerHTML = lista.innerHTML + elementContador;
    })
}

getContadores = async () =>{
    let request = await fetch("http://localhost:8080/contadores",{
        method: "GET",
    }); 
    if (request.status == 200){
        const data = await request.json();
        console.log(data)
        return data;
    }
}
displayContadores();

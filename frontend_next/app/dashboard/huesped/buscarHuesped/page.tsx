import { useState } from "react";

export default function buscarHuespedPage(){
    const[dni, setDni] = useState("");
    const[nombre, setNombre] = useState("");
    const[apellido, setApellido] = useState("");
    const[tipodni, setTipoDni] = useState("");
    const[resultados, setResultados] = useState([]);


    const buscarPorNombre = async () => {
        if (!nombre) return;
        const res = await fetch(`http://localhost:8081/api/huespedes/nombre?nombre=${nombre}`);
        const data = await res.json();
        setResultados(data);
    };

    const buscarPorTipoDniAndDni = async () => {
        if (!tipodni || !dni) return;
        const res = await fetch(`http://localhost:8080/api/huespedes/dni?tipodni=${tipodni}&dni=${dni}`);
        const data = await res.json();
        setResultados([data]); // lo pongo en array para usar la misma tabla
    };


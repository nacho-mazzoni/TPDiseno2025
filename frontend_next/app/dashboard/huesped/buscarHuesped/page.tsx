"use client";

import { useState } from "react";

export default function BuscarHuespedPage() {
    const [nombre, setNombre] = useState("");
    const [apellido, setApellido] = useState("");
    const [tipodni, setTipodni] = useState("");
    const [dni, setDni] = useState("");
    const [resultados, setResultados] = useState<any[]>([]);
    const [error, setError] = useState<string | null>(null);

    const handleBuscar = async () => {
        setError(null);

        try {
            let url = "http://localhost:8081/api/huespedes";

            if (nombre) {
                url += `/nombre?nombre=${nombre}`;
            } else if (tipodni && dni) {
                url += `/dni?tipodni=${tipodni}&dni=${dni}`;
            } else if (!nombre && !tipodni && !dni) {
                // buscar todos
                url = "http://localhost:8081/api/huespedes";
            } else {
                throw new Error("Combinación de filtros no válida");
            }

            const res = await fetch(url);

            if (!res.ok) {
                const text = await res.text();
                throw new Error(text);
            }

            const data = await res.json();
            setResultados(Array.isArray(data) ? data : [data]);
        } catch (err) {
            if (err instanceof Error) {
                setError(err.message);
            } else {
            setError("Error desconocido");
    }
        }
    };

    return (
        <div style={{ padding: 20 }}>
            <h1>Buscar Huésped</h1>

            <div>
                <input
                    placeholder="Nombre"
                    onChange={(e) => setNombre(e.target.value)}
                />
                <input
                    placeholder="Apellido"
                    onChange={(e) => setApellido(e.target.value)}
                />
                <input
                    placeholder="Tipo DNI"
                    onChange={(e) => setTipodni(e.target.value)}
                />
                <input
                    placeholder="DNI"
                    onChange={(e) => setDni(e.target.value)}
                />

                <button onClick={handleBuscar}>Buscar</button>
            </div>

            {error && <p style={{ color: "red" }}>{error}</p>}

            <table>
                <thead>
                    <tr>
                        <th>Tipo DNI</th>
                        <th>DNI</th>
                        <th>Nombre</th>
                        <th>Apellido</th>
                    </tr>
                </thead>
                <tbody>
                    {resultados.map((h) => (
                        <tr key={h.dni + h.tipoDni}>
                            <td>{h.tipoDni}</td>
                            <td>{h.dni}</td>
                            <td>{h.nombre}</td>
                            <td>{h.apellido}</td>
                        </tr>
                    ))}
                </tbody>
            </table>
        </div>
    );
}

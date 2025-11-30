// Ubicación: src/app/reservas/crearReserva/page.tsx
"use client";

import React, { useState, useEffect } from "react";
import { useRouter } from "next/navigation";

// --- TIPOS ---
interface CeldaCalendario {
  fecha: string;
  idHabitacion: number;
  estado: "LIBRE" | "OCUPADA" | "RESERVADA";
}

interface HuespedForm {
  dni: string;
  nombre: string;
  apellido: string;
  telefono: string;
}

export default function CrearReservaPage() {
    const router = useRouter();
    
    // --- ESTADOS ---
    const [paso, setPaso] = useState<1 | 2 | 3>(1);
    const [loading, setLoading] = useState(false);

    // Paso 1: Búsqueda y Selección
    const [fechaDesde, setFechaDesde] = useState("");
    const [fechaHasta, setFechaHasta] = useState("");
    const [disponibilidad, setDisponibilidad] = useState<CeldaCalendario[]>([]);
    
    // Selección temporal
    const [seleccion, setSeleccion] = useState<{
        idHabitacion: number;
        fechaInicio: string;
        fechaFin: string;
        cantNoches: number;
    } | null>(null);

    // Paso 3: Datos Huésped
    const [huesped, setHuesped] = useState<HuespedForm>({
        dni: "",
        nombre: "",
        apellido: "",
        telefono: "",
    });

    useEffect(() => {
        // Verificamos si hay una reserva pendiente en memoria
        const reservaPendiente = localStorage.getItem("reservaPendiente");
        
        if (reservaPendiente) {
            const data = JSON.parse(reservaPendiente);
            
            // Restauramos los datos
            setSeleccion(data.seleccion);
            setHuesped(prev => ({ ...prev, dni: data.dni }));
            setFechaDesde(data.seleccion.fechaInicio);
            setFechaHasta(data.seleccion.fechaFin);
            
            // Avanzamos directo al paso 3 (Confirmación)
            setPaso(3);
            
            // Opcional: Volver a buscar disponibilidad para asegurar que se pinte la grilla
            // buscarDisponibilidad(); 
        }
    }, []);

    // --- LOGICA PASO 1: MOSTRAR ESTADO HABITACIONES ---
    const buscarDisponibilidad = async () => {
        if(!fechaDesde || !fechaHasta) {
            alert("Por favor seleccione un rango de fechas.");
            return;
        }
        setLoading(true);
        try {
        // CORRECCIÓN: Puerto 8081
        const res = await fetch(`http://localhost:8081/reservas/disponibilidad?fechaInicio=${fechaDesde}&fechaFin=${fechaHasta}`);
        if (!res.ok) throw new Error("Error al conectar con backend");
        const data = await res.json();
        setDisponibilidad(data);
        } catch (e) {
        console.error(e);
        alert("Error al buscar disponibilidad. Verifica que el backend esté corriendo en el puerto 8081.");
        } finally {
        setLoading(false);
        }
    };

    const seleccionarCelda = (celda: CeldaCalendario) => {
        if (celda.estado !== "LIBRE") {
            alert("La habitación seleccionada no está disponible.");
            return;
        }

        const fInicio = new Date(fechaDesde);
        const fFin = new Date(fechaHasta);
        const diffTime = Math.abs(fFin.getTime() - fInicio.getTime());
        const diffDays = Math.ceil(diffTime / (1000 * 60 * 60 * 24)); 

        setSeleccion({
            idHabitacion: celda.idHabitacion,
            fechaInicio: fechaDesde,
            fechaFin: fechaHasta,
            cantNoches: diffDays
        });
    };

    // --- LOGICA PASO 2: VERIFICACIÓN ---
    const handleAceptarVerificacion = () => {
        setPaso(3);
    };

    const handleRechazarVerificacion = () => {
        setSeleccion(null);
        setPaso(1);
    };

    
        // --- LOGICA PASO 3: CONFIRMAR ---
    const finalizarReserva = async () => {
        if(!seleccion) return;
        
        // Validar campos del formulario
        if (!huesped.dni) {
            alert("Por favor ingrese el DNI del huésped.");
            return;
        }

        setLoading(true);

        try {
            // 1. PRIMERO: Verificar si el huésped existe
            // Ajusta la URL a tu controlador de Huéspedes
            const checkRes = await fetch(`http://localhost:8081/huespedes/getByDni?dni=${huesped.dni}`);

            if (checkRes.status === 404) {
                // --- CASO: HUÉSPED NO EXISTE ---
                const confirmar = confirm("El huésped no está registrado. ¿Desea ir a la pantalla de Alta de Huésped?");
                if (confirmar) {
                    const estadoAGuardar = {
                        seleccion: seleccion,
                        dni: huesped.dni
                    };
                    localStorage.setItem("reservaPendiente", JSON.stringify(estadoAGuardar));
                    // Redirigimos a la pantalla de creación de huésped
                    router.push("/dashboard/huesped/altaHuesped?returnTo=reserva"); 
                }
                setLoading(false);
                return; // Cortamos la ejecución aquí
            }

            // --- CASO: HUÉSPED SÍ EXISTE ---
            // Si llegamos acá, es porque el huésped existe. Procedemos a reservar.
            
            const payload = {
                reserva: {
                    cantHuesped: 1,
                    fechaInicio: seleccion.fechaInicio,
                    cantNoches: seleccion.cantNoches,
                    descuento: false,
                    estado: "Confirmada",
                    idHuesped: parseInt(huesped.dni) // Vinculamos por ID
                },
                // Como el huésped YA EXISTE, mandamos solo el DNI en el objeto huesped
                // El backend buscará el resto de datos en la BD.
                huesped: {
                    dni: parseInt(huesped.dni),
                    nombre: "",   // No hace falta enviarlo si ya existe
                    apellido: "", // No hace falta enviarlo si ya existe
                    tipoDni: "DNI",
                    // Enviamos dirección vacía o nula, el backend usará la de la BD
                    direccion: null 
                },
                habitacion: {
                    idHabitacion: seleccion.idHabitacion,
                    idTipo: 1, // Este ID se ignorará en el backend gracias a la corrección anterior
                    estado: "ocupada", 
                    nochesDescuento: 0
                }
            };

            const res = await fetch("http://localhost:8081/reservas/crear", {
                method: "POST",
                headers: { "Content-Type": "application/json" },
                body: JSON.stringify(payload)
            });

            if(res.ok) {
                alert("¡Éxito! La reserva ha sido registrada.");
                // LIMPIAR MEMORIA AL FINALIZAR 
                localStorage.removeItem("reservaPendiente");
                router.push("/dashboard/reservas");
            } else {
                const txt = await res.text();
                alert("Error al registrar: " + txt);
            }

        } catch (e) {
            console.error(e);
            alert("Error de conexión con el servidor.");
        } finally {
            setLoading(false);
        }
    };

    // --- RENDERIZADO ---
    const getDias = () => Array.from(new Set(disponibilidad.map(c => c.fecha))).sort();
    const getHabitaciones = () => Array.from(new Set(disponibilidad.map(c => c.idHabitacion))).sort((a,b) => a-b);

    return (
        <div className="p-6 max-w-6xl mx-auto font-sans text-gray-800">
        
        {/* HEADER PASOS */}
        <div className="mb-8 flex justify-between items-center bg-gray-100 p-4 rounded-lg">
            <div className={`font-bold ${paso === 1 ? 'text-blue-600' : 'text-gray-400'}`}>1. Selección</div>
            <div className="text-gray-400">→</div>
            <div className={`font-bold ${paso === 2 ? 'text-blue-600' : 'text-gray-400'}`}>2. Verificación</div>
            <div className="text-gray-400">→</div>
            <div className={`font-bold ${paso === 3 ? 'text-blue-600' : 'text-gray-400'}`}>3. Datos Huésped</div>
        </div>

        {/* --- VISTA 1: GRILLA DE DISPONIBILIDAD --- */}
        {paso === 1 && (
            <div className="bg-white shadow rounded-lg p-6 border">
                <h2 className="text-xl font-bold mb-4">Buscar Disponibilidad</h2>
                
                <div className="flex gap-4 mb-6 items-end">
                    <div>
                        <label className="block text-sm font-bold text-gray-700">Desde</label>
                        <input type="date" className="border p-2 rounded" value={fechaDesde} onChange={e => setFechaDesde(e.target.value)} />
                    </div>
                    <div>
                        <label className="block text-sm font-bold text-gray-700">Hasta</label>
                        <input type="date" className="border p-2 rounded" value={fechaHasta} onChange={e => setFechaHasta(e.target.value)} />
                    </div>
                    <button 
                        onClick={buscarDisponibilidad} 
                        className="bg-blue-600 text-white px-6 py-2 rounded hover:bg-blue-700 font-bold"
                        disabled={loading}
                    >
                        {loading ? "Cargando..." : "Buscar"}
                    </button>
                </div>

                {/* TABLA */}
                {disponibilidad.length > 0 && (
                    <div className="overflow-auto border rounded">
                        <table className="w-full text-center border-collapse">
                            <thead className="bg-gray-200">
                                <tr>
                                    <th className="p-3 border sticky left-0 bg-gray-200">Habitación</th>
                                    {getDias().map(dia => (
                                        <th key={dia} className="p-3 border min-w-[100px]">{dia}</th>
                                    ))}
                                </tr>
                            </thead>
                            <tbody>
                                {getHabitaciones().map(idHab => (
                                    <tr key={idHab}>
                                        <td className="p-3 border font-bold bg-gray-50">Hab {idHab}</td>
                                        {getDias().map(dia => {
                                            const celda = disponibilidad.find(c => c.idHabitacion === idHab && c.fecha === dia);
                                            const estado = celda ? celda.estado : "DESCONOCIDO";
                                            
                                            let bgClass = "bg-green-200 hover:bg-green-300 cursor-pointer"; 
                                            if(estado === "OCUPADA") bgClass = "bg-red-300 cursor-not-allowed";
                                            if(estado === "RESERVADA") bgClass = "bg-yellow-200 cursor-not-allowed";
                                            
                                            const isSelected = seleccion?.idHabitacion === idHab;

                                            return (
                                                <td 
                                                    key={dia} 
                                                    className={`p-3 border ${bgClass} ${isSelected ? 'ring-2 ring-blue-600' : ''}`}
                                                    onClick={() => celda && seleccionarCelda(celda)}
                                                >
                                                    {estado === "LIBRE" ? "Libre" : "Ocupada"}
                                                </td>
                                            )
                                        })}
                                    </tr>
                                ))}
                            </tbody>
                        </table>
                    </div>
                )}
                
                {seleccion && (
                    <div className="mt-4 flex justify-end">
                        <button onClick={() => setPaso(2)} className="bg-blue-600 text-white px-6 py-2 rounded font-bold">
                            Siguiente: Verificar
                        </button>
                    </div>
                )}
            </div>
        )}

        {/* --- VISTA 2: LISTADO DE VERIFICACIÓN --- */}
        {paso === 2 && seleccion && (
            <div className="bg-white shadow rounded-lg p-6 border max-w-3xl mx-auto">
                <h2 className="text-xl font-bold mb-6 text-center">Verificar Reserva</h2>
                
                <div className="bg-gray-50 p-4 rounded border mb-6">
                    <div className="grid grid-cols-2 gap-4">
                        <div className="font-bold text-gray-600">Habitación:</div>
                        <div>{seleccion.idHabitacion} (Tipo Estándar)</div>

                        <div className="font-bold text-gray-600">Ingreso:</div>
                        <div>{seleccion.fechaInicio}, 12:00hs</div>

                        <div className="font-bold text-gray-600">Egreso:</div>
                        <div>{seleccion.fechaFin}, 10:00hs</div>
                        
                        <div className="font-bold text-gray-600">Noches:</div>
                        <div>{seleccion.cantNoches}</div>
                    </div>
                </div>

                <div className="flex justify-between">
                    <button onClick={handleRechazarVerificacion} className="bg-gray-300 text-gray-800 px-6 py-2 rounded font-bold hover:bg-gray-400">
                        Rechazar
                    </button>
                    <button onClick={handleAceptarVerificacion} className="bg-blue-600 text-white px-6 py-2 rounded font-bold hover:bg-blue-700">
                        Aceptar
                    </button>
                </div>
            </div>
        )}

        {/* --- VISTA 3: DATOS DEL HUÉSPED --- */}
        {paso === 3 && (
            <div className="bg-white shadow rounded-lg p-6 border max-w-2xl mx-auto">
                <h2 className="text-xl font-bold mb-6 text-center border-b pb-2">Reserva a nombre de:</h2>
                
                <div className="space-y-4">
                    <div>
                        <label className="block text-gray-700 font-bold mb-1">DNI (*)</label>
                        <input 
                            type="number" 
                            className="w-full border p-2 rounded focus:ring-2 focus:ring-blue-500 outline-none"
                            value={huesped.dni}
                            onChange={e => setHuesped({...huesped, dni: e.target.value})}
                        />
                    </div>

                    <div>
                        <label className="block text-gray-700 font-bold mb-1">Apellido (*)</label>
                        <input 
                            type="text" 
                            className="w-full border p-2 rounded focus:ring-2 focus:ring-blue-500 outline-none"
                            value={huesped.apellido}
                            onChange={e => setHuesped({...huesped, apellido: e.target.value.toUpperCase()})}
                        />
                    </div>

                    <div>
                        <label className="block text-gray-700 font-bold mb-1">Nombre (*)</label>
                        <input 
                            type="text" 
                            className="w-full border p-2 rounded focus:ring-2 focus:ring-blue-500 outline-none"
                            value={huesped.nombre}
                            onChange={e => setHuesped({...huesped, nombre: e.target.value})}
                        />
                    </div>

                    <div>
                        <label className="block text-gray-700 font-bold mb-1">Teléfono (*)</label>
                        <input 
                            type="text" 
                            className="w-full border p-2 rounded focus:ring-2 focus:ring-blue-500 outline-none"
                            value={huesped.telefono}
                            onChange={e => setHuesped({...huesped, telefono: e.target.value})}
                        />
                    </div>
                </div>

                <div className="flex justify-between mt-8 pt-4 border-t">
                    <button onClick={() => setPaso(2)} className="text-gray-500 font-bold hover:text-gray-700">
                        ← Volver
                    </button>
                    <button 
                        onClick={finalizarReserva} 
                        disabled={loading}
                        className="bg-green-600 text-white px-8 py-2 rounded font-bold hover:bg-green-700 shadow-lg transform hover:-translate-y-0.5 transition-all"
                    >
                        {loading ? "Registrando..." : "Confirmar Reserva"}
                    </button>
                </div>
            </div>
        )}

        </div>
    );
}
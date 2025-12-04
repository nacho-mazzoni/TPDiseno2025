// Ubicación: src/app/dashboard/huesped/altaHuesped/page.tsx
"use client";

import React, { useState } from "react";
import { useRouter, useSearchParams } from "next/navigation";

// --- INTERFACES ---
interface HuespedForm {
  apellido: string;
  nombre: string;
  tipoDni: string;
  dni: string; 
  cuil: string;
  fechaNacimiento: string;
  posIva: string;
  telefono: string;
  email: string;
  nacionalidad: string;
  ocupacion: string;
  // Dirección desglosada
  calle: string;
  numero: string;
  departamento: string;
  piso: string;
  codPostal: string;
  localidad: string; // Obligatorio en BD
  provincia: string;
  pais: string;
}

export default function AltaHuespedPage() {
  const router = useRouter();
  const searchParams = useSearchParams();
  const returnTo = searchParams.get("returnTo"); // Detectar si venimos de Reserva

  // --- ESTADOS ---
  const [form, setForm] = useState<HuespedForm>({
    apellido: "", nombre: "", tipoDni: "DNI", dni: "", cuil: "",
    fechaNacimiento: "", posIva: "Consumidor Final", telefono: "",
    email: "", nacionalidad: "", ocupacion: "",
    calle: "", numero: "", departamento: "", piso: "",
    codPostal: "", localidad: "", provincia: "", pais: ""
  });

  const [errores, setErrores] = useState<string[]>([]);
  const [loading, setLoading] = useState(false);

  // Modales del Flujo
  const [showDuplicateModal, setShowDuplicateModal] = useState(false); 
  const [showCancelModal, setShowCancelModal] = useState(false);       
  const [showSuccessModal, setShowSuccessModal] = useState(false);     

  // --- MANEJO DE INPUTS (Con lógica de País) ---
  const handleChange = (e: React.ChangeEvent<HTMLInputElement | HTMLSelectElement>) => {
    const { name, value } = e.target;
    
    setForm(prev => {
        const newState = { ...prev, [name]: value };
        
        // REGLA DE NEGOCIO: Si cambia la nacionalidad, copiamos el valor al País automáticamente
        if (name === "nacionalidad") {
            newState.pais = value; 
        }
        
        return newState;
    });
  };

  // --- FUNCIÓN AUXILIAR: Calcular Edad ---
  const calcularEdad = (fechaNacimiento: string): number => {
    if (!fechaNacimiento) return 0;
    const hoy = new Date();
    const nacimiento = new Date(fechaNacimiento);
    let edad = hoy.getFullYear() - nacimiento.getFullYear();
    const m = hoy.getMonth() - nacimiento.getMonth();
    if (m < 0 || (m === 0 && hoy.getDate() < nacimiento.getDate())) {
        edad--;
    }
    return edad;
  };

  // --- VALIDACIONES (Caso 2.A) ---
  const validarFormulario = (): boolean => {
    const nuevosErrores: string[] = [];
    if (!form.apellido) nuevosErrores.push("Apellido es obligatorio");
    if (!form.nombre) nuevosErrores.push("Nombre es obligatorio");
    if (!form.dni) nuevosErrores.push("Número de documento es obligatorio");
    if (!form.fechaNacimiento) nuevosErrores.push("Fecha de nacimiento es obligatoria");
    if (!form.calle) nuevosErrores.push("Calle es obligatoria");
    if (!form.numero) nuevosErrores.push("Número de casa es obligatorio"); 
    if (!form.localidad) nuevosErrores.push("Localidad/Ciudad es obligatoria"); // <--- NUEVA VALIDACIÓN
    if (!form.telefono) nuevosErrores.push("Teléfono es obligatorio");
    if (!form.nacionalidad) nuevosErrores.push("Nacionalidad es obligatoria");
    if (!form.ocupacion) nuevosErrores.push("Ocupación es obligatoria");

    setErrores(nuevosErrores);
    return nuevosErrores.length === 0;
  };

  // --- LÓGICA DE GUARDADO ---
  const procesarGuardado = async () => {
    setLoading(true);
    
    // Calculamos edad antes de enviar
    const edadCalculada = calcularEdad(form.fechaNacimiento);

    // Armar payload para el backend
    const payload = {
        nombre: form.nombre,
        apellido: form.apellido,
        tipoDni: form.tipoDni,
        dni: parseInt(form.dni),
        fechaNacimiento: form.fechaNacimiento,
        edad: edadCalculada, // <--- CAMPO OBLIGATORIO PARA BACKEND
        email: form.email,
        telefono: form.telefono,
        ocupacion: form.ocupacion,
        posIva: form.posIva,
        
        direccion: {
            calle: form.calle,
            numero: parseInt(form.numero) || 0,
            departamento: form.departamento || null,
            piso: form.piso || null,
            codPostal: parseInt(form.codPostal) || 0,
            // Campos geográficos obligatorios
            localidad: form.localidad, 
            provincia: form.provincia,
            pais: form.pais            
        }
    };

    try {
        // Ajusta el puerto a 8081 si es donde corre tu backend
        const res = await fetch("http://localhost:8081/huespedes/crear", { 
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(payload)
        });

        if (res.ok) {
            setShowSuccessModal(true); // Caso 3: Éxito
        } else {
            const txt = await res.text();
            // Intentar mostrar mensaje limpio si es JSON
            try {
                const errJson = JSON.parse(txt);
                alert("Error: " + (errJson.message || errJson.error));
            } catch {
                alert("Error al guardar: " + txt);
            }
        }
    } catch (error) {
        console.error(error);
        alert("Error de conexión con el servidor (Verifica puerto 8081)");
    } finally {
        setLoading(false);
    }
  };

  // --- FLUJO PRINCIPAL ---
  const handleSiguiente = async () => {
    setErrores([]);
    if (!validarFormulario()) return;

    // Verificar duplicados
    try {
        const check = await fetch(`http://localhost:8081/huesped/getByDni?dni=${form.dni}`);
        if (check.ok) {
            setShowDuplicateModal(true);
            return; 
        }
    } catch (e) {
        // Ignorar error de conexión en checkeo
    }

    procesarGuardado();
  };

  const handleAceptarIgualmente = () => {
      setShowDuplicateModal(false);
      procesarGuardado(); 
  };

  // --- FLUJO ÉXITO Y RETORNO ---
  const handleCargarOtro = (cargarOtro: boolean) => {
      setShowSuccessModal(false);
      
      if (cargarOtro) {
          // Limpiar form
          setForm({
            apellido: "", nombre: "", tipoDni: "DNI", dni: "", cuil: "",
            fechaNacimiento: "", posIva: "Consumidor Final", telefono: "",
            email: "", nacionalidad: "", ocupacion: "",
            calle: "", numero: "", departamento: "", piso: "",
            codPostal: "", localidad: "", provincia: "", pais: ""
          });
          window.scrollTo(0, 0);
      } else {
          // TERMINAR CU
          if (returnTo === "reserva") {
              // Volver a la pantalla de crear reserva
              router.push("/dashboard/reservas/crearReserva");
          } else {
              // Volver al menú de huéspedes
              router.push("/dashboard/huesped"); 
          }
      }
  };

  // --- FLUJO CANCELAR ---
  const handleCancelarClick = () => setShowCancelModal(true);

  const confirmarCancelacion = (confirmar: boolean) => {
      setShowCancelModal(false);
      if (confirmar) {
          if (returnTo === "reserva") {
             router.push("/dashboard/reservas/crearReserva");
          } else {
             router.back();
          }
      }
  };

  // --- RENDERIZADO ---
  return (
    <div className="p-8 max-w-5xl mx-auto bg-gray-50 min-h-screen font-sans text-gray-800">
      <h1 className="text-3xl font-bold text-center mb-8 text-gray-800">Dar de Alta Huésped</h1>

      {/* ERRORES */}
      {errores.length > 0 && (
        <div className="bg-red-100 border-l-4 border-red-500 text-red-700 p-4 mb-6 rounded shadow-sm">
          <p className="font-bold">Por favor corrija los siguientes errores:</p>
          <ul className="list-disc pl-5">
            {errores.map((err, idx) => <li key={idx}>{err}</li>)}
          </ul>
        </div>
      )}

      <div className="grid grid-cols-1 md:grid-cols-2 gap-8">
        
        {/* COLUMNA IZQUIERDA */}
        <div className="space-y-4">
            <div>
                <label className="block text-sm font-bold text-gray-700 mb-1">Apellidos: <span className="text-red-500">(*)</span></label>
                <input name="apellido" value={form.apellido} onChange={handleChange} placeholder="Ej: Beltrán" className="w-full border p-2 rounded focus:ring-2 focus:ring-blue-400 outline-none" />
            </div>

            <div>
                <label className="block text-sm font-bold text-gray-700 mb-1">Documento: <span className="text-red-500">(*)</span></label>
                <div className="flex gap-2">
                    <select name="tipoDni" value={form.tipoDni} onChange={handleChange} className="border p-2 rounded w-1/3">
                        <option>DNI</option>
                        <option>LE</option>
                        <option>LC</option>
                        <option>Pasaporte</option>
                        <option>Otro</option>
                    </select>
                    <input name="dni" type="number" value={form.dni} onChange={handleChange} placeholder="Número" className="border p-2 rounded w-2/3" />
                </div>
            </div>

            <div>
                <label className="block text-sm font-bold text-gray-700 mb-1">Fecha de Nacimiento: <span className="text-red-500">(*)</span></label>
                <input name="fechaNacimiento" type="date" value={form.fechaNacimiento} onChange={handleChange} className="w-full border p-2 rounded" />
            </div>

            {/* DIRECCIÓN IZQUIERDA */}
            <div className="bg-white p-4 border rounded shadow-sm">
                <label className="block text-sm font-bold text-blue-800 mb-2 border-b pb-1">Dirección <span className="text-red-500">(*)</span></label>
                
                <div className="mb-2">
                    <input name="calle" value={form.calle} onChange={handleChange} placeholder="Calle (*)" className="w-full border p-2 rounded mb-2" />
                </div>
                
                <div className="flex gap-2 mb-2">
                    <input name="numero" value={form.numero} onChange={handleChange} placeholder="Núm (*)" className="w-1/3 border p-2 rounded" />
                    <input name="piso" value={form.piso} onChange={handleChange} placeholder="Piso" className="w-1/3 border p-2 rounded" />
                    <input name="departamento" value={form.departamento} onChange={handleChange} placeholder="Depto" className="w-1/3 border p-2 rounded" />
                </div>

                <div className="flex gap-2">
                    <input name="codPostal" value={form.codPostal} onChange={handleChange} placeholder="CP" className="w-1/3 border p-2 rounded" />
                    {/* CAMPO LOCALIDAD AGREGADO */}
                    <input name="localidad" value={form.localidad} onChange={handleChange} placeholder="Ciudad/Localidad (*)" className="w-2/3 border p-2 rounded focus:ring-2 focus:ring-blue-200" />
                </div>
            </div>

            <div>
                <label className="block text-sm font-bold text-gray-700 mb-1">Email:</label>
                <input name="email" type="email" value={form.email} onChange={handleChange} placeholder="ejemplo@mail.com" className="w-full border p-2 rounded" />
            </div>

            <div>
                <label className="block text-sm font-bold text-gray-700 mb-1">Nacionalidad: <span className="text-red-500">(*)</span></label>
                <input name="nacionalidad" value={form.nacionalidad} onChange={handleChange} placeholder="Ej: Argentino" className="w-full border p-2 rounded" />
            </div>
        </div>

        {/* COLUMNA DERECHA */}
        <div className="space-y-4">
            <div>
                <label className="block text-sm font-bold text-gray-700 mb-1">Nombre: <span className="text-red-500">(*)</span></label>
                <input name="nombre" value={form.nombre} onChange={handleChange} placeholder="Ej: Bautista" className="w-full border p-2 rounded focus:ring-2 focus:ring-blue-400 outline-none" />
            </div>

            <div>
                <label className="block text-sm font-bold text-gray-700 mb-1">CUIL:</label>
                <input name="cuil" value={form.cuil} onChange={handleChange} placeholder="Número" className="w-full border p-2 rounded" />
            </div>

            <div>
                <label className="block text-sm font-bold text-gray-700 mb-1">Posición Frente al IVA: <span className="text-red-500">(*)</span></label>
                <select name="posIva" value={form.posIva} onChange={handleChange} className="w-full border p-2 rounded">
                    <option>Consumidor Final</option>
                    <option>Responsable Inscripto</option>
                    <option>Monotributista</option>
                    <option>Exento</option>
                </select>
            </div>

            <div>
                <label className="block text-sm font-bold text-gray-700 mb-1">Teléfono: <span className="text-red-500">(*)</span></label>
                <input name="telefono" value={form.telefono} onChange={handleChange} placeholder="+54 9 ..." className="w-full border p-2 rounded" />
            </div>

            {/* DIRECCIÓN DERECHA */}
            <div className="bg-white p-4 border rounded shadow-sm mt-8"> 
                <div className="mb-2">
                    <label className="text-xs text-gray-500 font-bold">Provincia</label>
                    <select name="provincia" value={form.provincia} onChange={handleChange} className="w-full border p-2 rounded">
                        <option value="">Seleccione...</option>
                        <option value="Santa Fe">Santa Fe</option>
                        <option value="Cordoba">Córdoba</option>
                        <option value="Buenos Aires">Buenos Aires</option>
                        <option value="Entre Rios">Entre Ríos</option>
                        <option value="Mendoza">Mendoza</option>
                        <option value="Otra">Otra</option>
                    </select>
                </div>
                <div>
                    <label className="text-xs text-gray-500 font-bold">País (Autocompletado)</label>
                    <input 
                        name="pais" 
                        value={form.pais} 
                        onChange={handleChange} 
                        placeholder="País" 
                        className="w-full border p-2 rounded bg-gray-50"
                    />
                </div>
            </div>

            <div className="mt-4">
                <label className="block text-sm font-bold text-gray-700 mb-1">Ocupación: <span className="text-red-500">(*)</span></label>
                <input name="ocupacion" value={form.ocupacion} onChange={handleChange} placeholder="Ocupación" className="w-full border p-2 rounded" />
            </div>
        </div>
      </div>

      {/* BOTONES */}
      <div className="flex justify-between mt-10 pt-4 border-t border-gray-300">
        <button 
            onClick={handleCancelarClick} 
            className="px-6 py-2 bg-gray-300 text-gray-800 font-bold rounded hover:bg-gray-400 transition-colors"
        >
            Cancelar
        </button>
        <button 
            onClick={handleSiguiente} 
            disabled={loading}
            className="px-8 py-2 bg-gray-200 border border-gray-400 text-gray-800 font-bold rounded hover:bg-gray-300 transition-colors shadow-sm"
        >
            {loading ? "Guardando..." : "Siguiente"}
        </button>
      </div>

      {/* MODALES IGUALES AL ANTERIOR... */}
      {showDuplicateModal && (
        <div className="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center p-4 z-50">
            <div className="bg-white rounded-lg p-6 max-w-md w-full shadow-2xl border-l-8 border-yellow-500">
                <h3 className="text-xl font-bold text-yellow-600 mb-2">¡CUIDADO!</h3>
                <p className="text-gray-700 mb-6">El tipo y número de documento ya existen en el sistema.</p>
                <div className="flex justify-end gap-3">
                    <button onClick={() => setShowDuplicateModal(false)} className="px-4 py-2 bg-gray-200 rounded font-bold hover:bg-gray-300">CORREGIR</button>
                    <button onClick={handleAceptarIgualmente} className="px-4 py-2 bg-yellow-500 text-white rounded font-bold hover:bg-yellow-600">ACEPTAR IGUALMENTE</button>
                </div>
            </div>
        </div>
      )}

      {showCancelModal && (
        <div className="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center p-4 z-50">
            <div className="bg-white rounded-lg p-6 max-w-sm w-full shadow-2xl">
                <h3 className="text-lg font-bold mb-4">Confirmación</h3>
                <p className="text-gray-700 mb-6">¿Desea cancelar el alta del huésped?</p>
                <div className="flex justify-end gap-3">
                    <button onClick={() => confirmarCancelacion(false)} className="px-4 py-2 bg-gray-200 rounded font-bold hover:bg-gray-300">No</button>
                    <button onClick={() => confirmarCancelacion(true)} className="px-4 py-2 bg-red-600 text-white rounded font-bold hover:bg-red-700">Si</button>
                </div>
            </div>
        </div>
      )}

      {showSuccessModal && (
        <div className="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center p-4 z-50">
            <div className="bg-white rounded-lg p-6 max-w-md w-full shadow-2xl border-t-8 border-green-500 text-center">
                <h3 className="text-xl font-bold text-gray-800 mb-2">¡Huésped Registrado!</h3>
                <p className="text-gray-600 mb-6">El huésped ha sido satisfactoriamente cargado.<br/>¿Desea cargar otro?</p>
                <div className="flex justify-center gap-6">
                    <button onClick={() => handleCargarOtro(false)} className="px-6 py-2 bg-gray-200 text-gray-800 rounded font-bold hover:bg-gray-300">NO</button>
                    <button onClick={() => handleCargarOtro(true)} className="px-6 py-2 bg-blue-600 text-white rounded font-bold hover:bg-blue-700">SI</button>
                </div>
            </div>
        </div>
      )}

    </div>
  );
}
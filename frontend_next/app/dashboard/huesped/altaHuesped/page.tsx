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
  calle: string;
  numero: string;
  departamento: string;
  piso: string;
  codPostal: string;
  localidad: string;
  provincia: string;
  pais: string;
}

export default function AltaHuespedPage() {
  const router = useRouter();
  const searchParams = useSearchParams();
  const returnTo = searchParams.get("returnTo");

  // --- ESTADOS ---
  const [form, setForm] = useState<HuespedForm>({
    apellido: "", nombre: "", tipoDni: "DNI", dni: "", cuil: "",
    fechaNacimiento: "", posIva: "Consumidor Final", telefono: "",
    email: "", nacionalidad: "", ocupacion: "",
    calle: "", numero: "", departamento: "", piso: "",
    codPostal: "", localidad: "", provincia: "", pais: ""
  });

  // CAMBIO 1: Errores ahora es un objeto para mapear campo -> mensaje
  const [errores, setErrores] = useState<Record<string, string>>({});
  
  const [loading, setLoading] = useState(false);
  const [showDuplicateModal, setShowDuplicateModal] = useState(false); 
  const [showCancelModal, setShowCancelModal] = useState(false);       
  const [showSuccessModal, setShowSuccessModal] = useState(false);     

  // --- MANEJO DE INPUTS ---
  const handleChange = (e: React.ChangeEvent<HTMLInputElement | HTMLSelectElement>) => {
    const { name, value } = e.target;
    setForm(prev => {
        const newState = { ...prev, [name]: value };
        if (name === "nacionalidad") newState.pais = value; 
        return newState;
    });
    
    // Limpiar error del campo cuando el usuario escribe
    if (errores[name]) {
        setErrores(prev => {
            const newErrors = { ...prev };
            delete newErrors[name];
            return newErrors;
        });
    }
  };

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

  // --- VALIDACIONES POR CAMPO ---
  const validarFormulario = (): boolean => {
    const nuevosErrores: Record<string, string> = {};
    
    if (!form.apellido) nuevosErrores.apellido = "El apellido es obligatorio";
    if (!form.nombre) nuevosErrores.nombre = "El nombre es obligatorio";
    if (!form.dni) nuevosErrores.dni = "El número de documento es obligatorio";
    if (!form.fechaNacimiento) nuevosErrores.fechaNacimiento = "La fecha de nacimiento es obligatoria";
    if (!form.calle) nuevosErrores.calle = "La calle es obligatoria";
    if (!form.numero) nuevosErrores.numero = "La altura es obligatoria"; 
    if (!form.localidad) nuevosErrores.localidad = "La localidad es obligatoria";
    if (!form.telefono) nuevosErrores.telefono = "El teléfono es obligatorio";
    if (!form.nacionalidad) nuevosErrores.nacionalidad = "La nacionalidad es obligatoria";
    if (!form.ocupacion) nuevosErrores.ocupacion = "La ocupación es obligatoria";

    setErrores(nuevosErrores);
    return Object.keys(nuevosErrores).length === 0;
  };

  // --- LÓGICA DE GUARDADO (POST o PUT) ---
  const procesarGuardado = async (esActualizacion: boolean = false) => {
    setLoading(true);
    const edadCalculada = calcularEdad(form.fechaNacimiento);

    const payload = {
        nombre: form.nombre,
        apellido: form.apellido,
        tipoDni: form.tipoDni,
        dni: parseInt(form.dni),
        fechaNacimiento: form.fechaNacimiento,
        edad: edadCalculada, 
        email: form.email,
        telefono: form.telefono,
        ocupacion: form.ocupacion,
        posIva: form.posIva,
        direccion: {
            calle: form.calle,
            numero: parseInt(form.numero) || 0,
            departamento: form.departamento,
            piso: form.piso,
            codPostal: parseInt(form.codPostal) || 0,
            localidad: form.localidad, 
            provincia: form.provincia,
            pais: form.pais            
        }
    };

    try {
        let url = "http://localhost:8081/huespedes/crear";
        let method = "POST";

        // CAMBIO 2: Si el usuario eligió "Cargar Igualmente", hacemos PUT (Modificar)
        if (esActualizacion) {
            url = `http://localhost:8081/huespedes/${form.tipoDni}/${form.dni}`;
            method = "PUT";
        }

        const res = await fetch(url, { 
            method: method,
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(payload)
        });

        if (res.ok) {
            setShowSuccessModal(true); 
        } else {
            const txt = await res.text();
            alert("Error al guardar: " + txt);
        }
    } catch (error) {
        console.error(error);
        alert("Error de conexión con el servidor.");
    } finally {
        setLoading(false);
    }
  };

  // --- FLUJO PRINCIPAL ---
  const handleSiguiente = async () => {
    // 1. Validar campos vacíos
    if (!validarFormulario()) return;

    // 2. Verificar duplicados en Backend
    try {
        // Asumiendo que retorna 200 OK si existe, 404 si no.
        const check = await fetch(`http://localhost:8081/huespedes/getByDni?dni=${form.dni}`);
        
        if (check.ok) {
            // El DNI ya existe -> Mostramos Modal
            setShowDuplicateModal(true);
            return; 
        }
    } catch (e) {
        console.error("Error verificando DNI", e);
    }

    // 3. Si no existe, guardar (POST)
    procesarGuardado(false);
  };

  const handleAceptarIgualmente = () => {
      setShowDuplicateModal(false);
      // Forzamos la actualización (PUT)
      procesarGuardado(true); 
  };

  // --- NAVEGACIÓN ---
  const handleCargarOtro = (cargarOtro: boolean) => {
      setShowSuccessModal(false);
      if (cargarOtro) {
          setForm({
            apellido: "", nombre: "", tipoDni: "DNI", dni: "", cuil: "",
            fechaNacimiento: "", posIva: "Consumidor Final", telefono: "",
            email: "", nacionalidad: "", ocupacion: "",
            calle: "", numero: "", departamento: "", piso: "",
            codPostal: "", localidad: "", provincia: "", pais: ""
          });
          setErrores({});
          window.scrollTo(0, 0);
      } else {
          if (returnTo === "reserva") router.push("/dashboard/reservas/crearReserva");
          else router.push("/dashboard/huesped"); 
      }
  };

  const confirmarCancelacion = (confirmar: boolean) => {
      setShowCancelModal(false);
      if (confirmar) {
          if (returnTo === "reserva") router.push("/dashboard/reservas/crearReserva");
          else router.back();
      }
  };

  // Helper para renderizar input con error (para limpiar el JSX)
  const getInputClass = (fieldName: string) => {
      return `w-full border p-2 rounded focus:ring-2 focus:ring-blue-400 outline-none ${errores[fieldName] ? 'border-red-500 bg-red-50' : ''}`;
  };

  const ErrorMsg = ({ field }: { field: string }) => {
      return errores[field] ? <p className="text-red-500 text-xs mt-1 font-bold">{errores[field]}</p> : null;
  };

  return (
    <div className="p-8 max-w-5xl mx-auto bg-gray-50 min-h-screen font-sans text-gray-800">
      <h1 className="text-3xl font-bold text-center mb-8 text-gray-800">Dar de Alta Huésped</h1>

      <div className="grid grid-cols-1 md:grid-cols-2 gap-8">
        
        {/* COLUMNA IZQUIERDA */}
        <div className="space-y-4">
            <div>
                <label className="block text-sm font-bold text-gray-700 mb-1">Apellidos: <span className="text-red-500">(*)</span></label>
                <input name="apellido" value={form.apellido} onChange={handleChange} className={getInputClass("apellido")} placeholder="Ej: Beltrán" />
                <ErrorMsg field="apellido" />
            </div>

            <div>
                <label className="block text-sm font-bold text-gray-700 mb-1">Documento: <span className="text-red-500">(*)</span></label>
                <div className="flex gap-2">
                    <select name="tipoDni" value={form.tipoDni} onChange={handleChange} className="border p-2 rounded w-1/3 bg-white">
                        <option>DNI</option>
                        <option>LE</option>
                        <option>LC</option>
                        <option>Pasaporte</option>
                        <option>Otro</option>
                    </select>
                    <div className="w-2/3">
                        <input name="dni" type="number" value={form.dni} onChange={handleChange} className={getInputClass("dni")} placeholder="Número" />
                        <ErrorMsg field="dni" />
                    </div>
                </div>
            </div>

            <div>
                <label className="block text-sm font-bold text-gray-700 mb-1">Fecha de Nacimiento: <span className="text-red-500">(*)</span></label>
                <input name="fechaNacimiento" type="date" value={form.fechaNacimiento} onChange={handleChange} className={getInputClass("fechaNacimiento")} />
                <ErrorMsg field="fechaNacimiento" />
            </div>

            {/* DIRECCIÓN */}
            <div className={`bg-white p-4 border rounded shadow-sm ${errores.calle || errores.numero ? 'border-red-300' : ''}`}>
                <label className="block text-sm font-bold text-blue-800 mb-2 border-b pb-1">Dirección <span className="text-red-500">(*)</span></label>
                
                <div className="mb-2">
                    <input name="calle" value={form.calle} onChange={handleChange} placeholder="Calle (*)" className={getInputClass("calle")} />
                    <ErrorMsg field="calle" />
                </div>
                
                <div className="flex gap-2 mb-2">
                    <div className="w-1/3">
                        <input name="numero" value={form.numero} onChange={handleChange} placeholder="Núm (*)" className={getInputClass("numero")} />
                        <ErrorMsg field="numero" />
                    </div>
                    <input name="piso" value={form.piso} onChange={handleChange} placeholder="Piso" className="w-1/3 border p-2 rounded" />
                    <input name="departamento" value={form.departamento} onChange={handleChange} placeholder="Depto" className="w-1/3 border p-2 rounded" />
                </div>

                <div className="flex gap-2">
                    <input name="codPostal" value={form.codPostal} onChange={handleChange} placeholder="CP" className="w-1/3 border p-2 rounded" />
                    <div className="w-2/3">
                        <input name="localidad" value={form.localidad} onChange={handleChange} placeholder="Ciudad/Localidad (*)" className={getInputClass("localidad")} />
                        <ErrorMsg field="localidad" />
                    </div>
                </div>
            </div>

            <div>
                <label className="block text-sm font-bold text-gray-700 mb-1">Email:</label>
                <input name="email" type="email" value={form.email} onChange={handleChange} placeholder="ejemplo@mail.com" className="w-full border p-2 rounded" />
            </div>

            <div>
                <label className="block text-sm font-bold text-gray-700 mb-1">Nacionalidad: <span className="text-red-500">(*)</span></label>
                <input name="nacionalidad" value={form.nacionalidad} onChange={handleChange} className={getInputClass("nacionalidad")} placeholder="Ej: Argentino" />
                <ErrorMsg field="nacionalidad" />
            </div>
        </div>

        {/* COLUMNA DERECHA */}
        <div className="space-y-4">
            <div>
                <label className="block text-sm font-bold text-gray-700 mb-1">Nombre: <span className="text-red-500">(*)</span></label>
                <input name="nombre" value={form.nombre} onChange={handleChange} className={getInputClass("nombre")} placeholder="Ej: Bautista" />
                <ErrorMsg field="nombre" />
            </div>

            <div>
                <label className="block text-sm font-bold text-gray-700 mb-1">CUIL:</label>
                <input name="cuil" value={form.cuil} onChange={handleChange} placeholder="Número" className="w-full border p-2 rounded" />
            </div>

            <div>
                <label className="block text-sm font-bold text-gray-700 mb-1">Posición Frente al IVA: <span className="text-red-500">(*)</span></label>
                <select name="posIva" value={form.posIva} onChange={handleChange} className="w-full border p-2 rounded bg-white">
                    <option>Consumidor Final</option>
                    <option>Responsable Inscripto</option>
                    <option>Monotributista</option>
                    <option>Exento</option>
                </select>
            </div>

            <div>
                <label className="block text-sm font-bold text-gray-700 mb-1">Teléfono: <span className="text-red-500">(*)</span></label>
                <input name="telefono" value={form.telefono} onChange={handleChange} className={getInputClass("telefono")} placeholder="+54 9 ..." />
                <ErrorMsg field="telefono" />
            </div>

            <div className="bg-white p-4 border rounded shadow-sm mt-8"> 
                <div className="mb-2">
                    <label className="text-xs text-gray-500 font-bold">Provincia</label>
                    <select name="provincia" value={form.provincia} onChange={handleChange} className="w-full border p-2 rounded bg-white">
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
                    <input name="pais" value={form.pais} onChange={handleChange} placeholder="País" className="w-full border p-2 rounded bg-gray-50" />
                </div>
            </div>

            <div className="mt-4">
                <label className="block text-sm font-bold text-gray-700 mb-1">Ocupación: <span className="text-red-500">(*)</span></label>
                <input name="ocupacion" value={form.ocupacion} onChange={handleChange} className={getInputClass("ocupacion")} placeholder="Ocupación" />
                <ErrorMsg field="ocupacion" />
            </div>
        </div>
      </div>

      {/* BOTONES */}
      <div className="flex justify-between mt-10 pt-4 border-t border-gray-300">
        <button onClick={() => setShowCancelModal(true)} className="px-6 py-2 bg-gray-300 text-gray-800 font-bold rounded hover:bg-gray-400 transition-colors">
            Cancelar
        </button>
        <button onClick={handleSiguiente} disabled={loading} className="px-8 py-2 bg-blue-600 text-white font-bold rounded hover:bg-blue-700 transition-colors shadow-lg">
            {loading ? "Procesando..." : "Siguiente"}
        </button>
      </div>

      {/* MODAL DUPLICADO (YA EXISTE DNI) */}
      {showDuplicateModal && (
        <div className="fixed inset-0 bg-black bg-opacity-60 flex items-center justify-center p-4 z-50 animate-fadeIn">
            <div className="bg-white rounded-lg p-6 max-w-md w-full shadow-2xl border-l-8 border-yellow-500">
                <h3 className="text-xl font-bold text-yellow-700 mb-3 flex items-center gap-2">
                    ⚠️ Huésped Existente
                </h3>
                <p className="text-gray-700 mb-6">
                    El DNI <strong>{form.dni}</strong> ya se encuentra registrado en la base de datos.
                    <br/><br/>
                    ¿Desea cancelar para corregir el número, o actualizar los datos del huésped existente con la información ingresada?
                </p>
                <div className="flex justify-end gap-3">
                    <button 
                        onClick={() => setShowDuplicateModal(false)} 
                        className="px-4 py-2 bg-gray-200 text-gray-700 rounded font-bold hover:bg-gray-300"
                    >
                        Cancelar
                    </button>
                    <button 
                        onClick={handleAceptarIgualmente} 
                        className="px-4 py-2 bg-yellow-500 text-white rounded font-bold hover:bg-yellow-600 shadow"
                    >
                        Cargar Igualmente
                    </button>
                </div>
            </div>
        </div>
      )}

      {/* MODAL CONFIRMAR CANCELACIÓN */}
      {showCancelModal && (
        <div className="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center p-4 z-50">
            <div className="bg-white rounded-lg p-6 max-w-sm w-full shadow-xl">
                <h3 className="text-lg font-bold mb-4 text-gray-800">Confirmar Cancelación</h3>
                <p className="text-gray-600 mb-6">¿Está seguro que desea salir? Se perderán los datos no guardados.</p>
                <div className="flex justify-end gap-3">
                    <button onClick={() => confirmarCancelacion(false)} className="px-4 py-2 bg-gray-200 rounded font-bold hover:bg-gray-300">No, volver</button>
                    <button onClick={() => confirmarCancelacion(true)} className="px-4 py-2 bg-red-600 text-white rounded font-bold hover:bg-red-700">Sí, salir</button>
                </div>
            </div>
        </div>
      )}

      {/* MODAL ÉXITO */}
      {showSuccessModal && (
        <div className="fixed inset-0 bg-black bg-opacity-60 flex items-center justify-center p-4 z-50">
            <div className="bg-white rounded-lg p-8 max-w-md w-full shadow-2xl border-t-8 border-green-500 text-center transform transition-all scale-100">
                <div className="mx-auto flex items-center justify-center h-12 w-12 rounded-full bg-green-100 mb-4">
                    <svg className="h-6 w-6 text-green-600" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                        <path strokeLinecap="round" strokeLinejoin="round" strokeWidth="2" d="M5 13l4 4L19 7" />
                    </svg>
                </div>
                <h3 className="text-2xl font-bold text-gray-800 mb-2">¡Guardado Exitoso!</h3>
                <p className="text-gray-600 mb-8">El huésped ha sido registrado/actualizado correctamente.<br/>¿Desea cargar otro?</p>
                <div className="flex justify-center gap-4">
                    <button onClick={() => handleCargarOtro(false)} className="px-6 py-2 bg-gray-200 text-gray-800 rounded font-bold hover:bg-gray-300 border">NO (Salir)</button>
                    <button onClick={() => handleCargarOtro(true)} className="px-6 py-2 bg-blue-600 text-white rounded font-bold hover:bg-blue-700 shadow-md">SI (Cargar otro)</button>
                </div>
            </div>
        </div>
      )}

    </div>
  );
}
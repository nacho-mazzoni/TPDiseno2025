"use client";

import React, { useState, useEffect, use } from "react"; 
import { useRouter } from "next/navigation";
import Swal from 'sweetalert2'; 

interface HuespedForm {
  dni: string;
  tipoDni: string;
  apellido: string;
  nombre: string;
  fechaNacimiento: string;
  email: string;
  telefono: string;
  ocupacion: string;
  posIva: string;
  nacionalidad: string;
  calle: string;
  numero: string;
  departamento: string;
  piso: string;
  codPostal: string;
  localidad: string;
  provincia: string;
  pais: string;
}

export default function ModificarHuespedPage({ params }: { params: Promise<{ dni: string }> }) {
  const router = useRouter();
  
  
  const resolvedParams = use(params);
  const dniUrl = resolvedParams.dni; 

  const [loading, setLoading] = useState(true);
  const [guardando, setGuardando] = useState(false);
  
  const [form, setForm] = useState<HuespedForm>({
    dni: "", tipoDni: "DNI", apellido: "", nombre: "", fechaNacimiento: "",
    email: "", telefono: "", ocupacion: "", posIva: "Consumidor Final", nacionalidad: "",
    calle: "", numero: "", departamento: "", piso: "", codPostal: "",
    localidad: "", provincia: "", pais: ""
  });

  //CARGAR DATOS AL INICIAR
  useEffect(() => {
    const cargarHuesped = async () => {
      try {
        const res = await fetch(`http://localhost:8081/huespedes/getByDni?dni=${dniUrl}`);
        if (res.ok) {
          const data = await res.json();
          
          setForm({
            dni: data.dni.toString(),
            tipoDni: data.tipoDni,
            apellido: data.apellido,
            nombre: data.nombre,
            fechaNacimiento: data.fechaNacimiento || "",
            email: data.email || "",
            telefono: data.telefono || "",
            ocupacion: data.ocupacion || "",
            posIva: data.posIva || "Consumidor Final",
            nacionalidad: data.nacionalidad || "",
            calle: data.direccion?.calle || "",
            numero: data.direccion?.numero?.toString() || "",
            departamento: data.direccion?.departamento || "",
            piso: data.direccion?.piso || "",
            codPostal: data.direccion?.codPostal?.toString() || "",
            localidad: data.direccion?.localidad || "",
            provincia: data.direccion?.provincia || "",
            pais: data.direccion?.pais || ""
          });
        } else {
          Swal.fire("Error", "No se encontró el huésped", "error");
          router.back();
        }
      } catch (error) {
        console.error(error);
        Swal.fire("Error", "Fallo al conectar con el servidor", "error");
      } finally {
        setLoading(false);
      }
    };

    if (dniUrl) cargarHuesped();
  }, [dniUrl, router]);

  //MANEJAR CAMBIOS EN INPUTS
  const handleChange = (e: React.ChangeEvent<HTMLInputElement | HTMLSelectElement>) => {
    setForm({ ...form, [e.target.name]: e.target.value });
  };

  //GUARDAR CAMBIOS
  const handleGuardar = async () => {
    setGuardando(true);
    
    const payload = {
        nombre: form.nombre,
        apellido: form.apellido,
        tipoDni: form.tipoDni,
        dni: parseInt(form.dni),
        fechaNacimiento: form.fechaNacimiento,
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
        const res = await fetch(`http://localhost:8081/huespedes/${form.tipoDni}/${form.dni}`, {
            method: "PUT",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(payload)
        });

        if (res.ok) {
            await Swal.fire("¡Éxito!", "Huésped modificado correctamente", "success");
            router.push("/dashboard/huesped");
        } else {
            const txt = await res.text();
            Swal.fire("Error", "No se pudo guardar: " + txt, "error");
        }
    } catch (e) {
        Swal.fire("Error", "Error de conexión", "error");
    } finally {
        setGuardando(false);
    }
  };

  if (loading) return <div className="p-8 text-center">Cargando datos del huésped...</div>;

  return (
    <div className="p-8 max-w-5xl mx-auto bg-gray-50 min-h-screen">
      <h1 className="text-3xl font-bold text-center mb-8 text-gray-800">Modificar Huésped</h1>

      <div className="bg-white p-6 rounded shadow border">
        {/* DATOS PERSONALES */}
        <div className="grid grid-cols-1 md:grid-cols-2 gap-6 mb-6">
            <div>
                <label className="font-bold block mb-1">DNI (No editable)</label>
                <input value={form.dni} disabled className="w-full border p-2 rounded bg-gray-200 cursor-not-allowed" />
            </div>
            <div>
                <label className="font-bold block mb-1">Tipo Doc</label>
                <input value={form.tipoDni} disabled className="w-full border p-2 rounded bg-gray-200 cursor-not-allowed" />
            </div>
            <div>
                <label className="font-bold block mb-1">Nombre</label>
                <input name="nombre" value={form.nombre} onChange={handleChange} className="w-full border p-2 rounded" />
            </div>
            <div>
                <label className="font-bold block mb-1">Apellido</label>
                <input name="apellido" value={form.apellido} onChange={handleChange} className="w-full border p-2 rounded" />
            </div>
            <div>
                <label className="font-bold block mb-1">Fecha Nacimiento</label>
                <input type="date" name="fechaNacimiento" value={form.fechaNacimiento} onChange={handleChange} className="w-full border p-2 rounded" />
            </div>
            <div>
                <label className="font-bold block mb-1">Teléfono</label>
                <input name="telefono" value={form.telefono} onChange={handleChange} className="w-full border p-2 rounded" />
            </div>
        </div>

        {/* DIRECCIÓN */}
        <h3 className="font-bold text-lg mb-4 border-b pb-2 text-blue-700">Dirección</h3>
        <div className="grid grid-cols-1 md:grid-cols-3 gap-4 mb-6">
            <div className="md:col-span-2">
                <label className="block text-sm font-bold text-gray-600">Calle</label>
                <input name="calle" value={form.calle} onChange={handleChange} className="w-full border p-2 rounded" />
            </div>
            <div>
                <label className="block text-sm font-bold text-gray-600">Número</label>
                <input name="numero" value={form.numero} onChange={handleChange} className="w-full border p-2 rounded" />
            </div>
            <div>
                <label className="block text-sm font-bold text-gray-600">Localidad</label>
                <input name="localidad" value={form.localidad} onChange={handleChange} className="w-full border p-2 rounded" />
            </div>
            <div>
                <label className="block text-sm font-bold text-gray-600">Provincia</label>
                <input name="provincia" value={form.provincia} onChange={handleChange} className="w-full border p-2 rounded" />
            </div>
            <div>
                <label className="block text-sm font-bold text-gray-600">País</label>
                <input name="pais" value={form.pais} onChange={handleChange} className="w-full border p-2 rounded" />
            </div>
        </div>

        {/* BOTONES */}
        <div className="flex justify-between pt-4 border-t">
            <button 
                onClick={() => router.back()} 
                className="bg-gray-300 px-6 py-2 rounded font-bold hover:bg-gray-400"
            >
                Cancelar
            </button>
            <button 
                onClick={handleGuardar} 
                disabled={guardando}
                className="bg-blue-600 text-white px-8 py-2 rounded font-bold hover:bg-blue-700 shadow"
            >
                {guardando ? "Guardando..." : "Guardar Cambios"}
            </button>
        </div>
      </div>
    </div>
  );
}
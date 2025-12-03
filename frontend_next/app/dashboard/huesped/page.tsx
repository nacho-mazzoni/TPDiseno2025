'use client'; // 👈 IMPORTANTE: Esto permite usar interactividad

import { useRouter } from 'next/navigation';

export default function HuespedMenuPage() {
  const router = useRouter();

  const navegarA = (ruta: string) => {
    console.log("Intentando navegar a:", ruta); // 👈 MIRA LA CONSOLA (F12)
    router.push(ruta);
  };

  return (
    <div className="p-10 max-w-5xl mx-auto">
      <h1 className="text-4xl font-bold text-blue-900 mb-10 text-center border-b pb-4">Administración de Huéspedes</h1>

      <div className="grid grid-cols-1 md:grid-cols-3 gap-8">
        
        {/* TARJETA 1: BUSCAR */}
        <div 
          onClick={() => navegarA('/dashboard/huesped/buscarHuesped')}
          className="bg-white p-8 rounded-xl shadow-sm border border-gray-200 hover:shadow-lg hover:border-blue-500 transition duration-300 h-full flex flex-col items-center text-center cursor-pointer group"
        >
          <div className="w-16 h-16 bg-blue-100 text-blue-600 rounded-full flex items-center justify-center mb-4 text-3xl group-hover:scale-110 transition">
            S
          </div>
          <h2 className="text-xl font-bold text-gray-800 mb-2">Buscar Huésped</h2>
          <p className="text-gray-500 text-sm">Consulta el padrón y visualiza datos.</p>
        </div>

        {/* TARJETA 2: NUEVO */}
        <div 
          onClick={() => navegarA('/dashboard/huesped/altaHuesped')}
          className="bg-white p-8 rounded-xl shadow-sm border border-gray-200 hover:shadow-lg hover:border-green-500 transition duration-300 h-full flex flex-col items-center text-center cursor-pointer group"
        >
          <div className="w-16 h-16 bg-green-100 text-green-600 rounded-full flex items-center justify-center mb-4 text-3xl group-hover:scale-110 transition">
            R
          </div>
          <h2 className="text-xl font-bold text-gray-800 mb-2">Nuevo Huésped</h2>
          <p className="text-gray-500 text-sm">Registra un nuevo cliente.</p>
        </div>

        {/* TARJETA 3: ELIMINAR/EDITAR */}
        <div 
          onClick={() => navegarA('/dashboard/huesped/buscarHuesped')}
          className="bg-white p-8 rounded-xl shadow-sm border border-gray-200 hover:shadow-lg hover:border-red-500 transition duration-300 h-full flex flex-col items-center text-center cursor-pointer group"
        >
          <div className="w-16 h-16 bg-red-100 text-red-600 rounded-full flex items-center justify-center mb-4 text-3xl group-hover:scale-110 transition">
            D
          </div>
          <h2 className="text-xl font-bold text-gray-800 mb-2">Eliminar / Editar</h2>
          <p className="text-gray-500 text-sm">Modifica o da de baja.</p>
        </div>

      </div>
    </div>
  );
}
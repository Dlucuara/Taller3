package uniandes.dpoo.aerolinea.persistencia;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import uniandes.dpoo.aerolinea.exceptions.AeropuertoDuplicadoException;
import uniandes.dpoo.aerolinea.exceptions.InformacionInconsistenteException;
import uniandes.dpoo.aerolinea.modelo.Aerolinea;
import uniandes.dpoo.aerolinea.modelo.Aeropuerto;
import uniandes.dpoo.aerolinea.modelo.Ruta;
import uniandes.dpoo.aerolinea.modelo.Vuelo;
import uniandes.dpoo.aerolinea.modelo.cliente.Avion;

/**
 * Esta clase no está implementada - y no debería implementarse como parte del taller.
 * 
 * Su objetivo es sólo ilustrar que podría haber varias implementaciones de la misma interfaz, y que durante la ejecución alguien podría decidir cuál de estas implementaciones
 * usar.
 */
public class PersistenciaAerolineaPlaintext implements IPersistenciaAerolinea
{
    @Override
    public void cargarAerolinea( String archivo, Aerolinea aerolinea ) throws Exception
    {
    	try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                linea = linea.trim();
                if (linea.isEmpty() || linea.startsWith("#")) continue;

                String[] partes = linea.split(";");
                String tipo = partes[0];

                if ("AVION".equals(tipo)) {
                    String nombre = partes[1];
                    int capacidad = Integer.parseInt(partes[2]);
                    aerolinea.agregarAvion(new Avion(nombre, capacidad));

                } else if ("RUTA".equals(tipo)) {
                    String codigoRuta = partes[1];
                    Aeropuerto origen = new Aeropuerto(
                        partes[2], partes[3], partes[4],
                        Double.parseDouble(partes[5]),
                        Double.parseDouble(partes[6])
                    );
                    Aeropuerto destino = new Aeropuerto(
                        partes[7], partes[8], partes[9],
                        Double.parseDouble(partes[10]),
                        Double.parseDouble(partes[11])
                    );
                    if (origen.getCodigo().equals(destino.getCodigo())) {
                        throw new AeropuertoDuplicadoException(
                            "Ruta inválida: origen y destino iguales " + origen.getCodigo());
                    }
                    String horaSalida = partes[12];
                    String horaLlegada = partes[13];

                    if (aerolinea.getRuta(codigoRuta) != null) {
                        throw new InformacionInconsistenteException("Ruta duplicada: " + codigoRuta);
                    }
                    aerolinea.agregarRuta(new Ruta(origen, destino, horaSalida, horaLlegada, codigoRuta));

                } else if ("VUELO".equals(tipo)) {
                    String fecha = partes[1];
                    String codigoRuta = partes[2];
                    String codigoAvion = partes[3];
                    aerolinea.programarVuelo(fecha, codigoRuta, codigoAvion);

                } else {
                    throw new InformacionInconsistenteException("Línea desconocida: " + tipo);
                }
                }
            }
    }

    @Override
    public void salvarAerolinea( String archivo, Aerolinea aerolinea ) throws IOException
    {
    	try (BufferedWriter bw = new BufferedWriter(new FileWriter(archivo))) {
    	// Aviones
	        bw.write("# Aviones");
	        bw.newLine();
	        for (Avion a : aerolinea.getAviones()) {
	            bw.write("AVION;" + a.getNombre() + ";" + a.getCapacidad());
	            bw.newLine();
	        }
	
	        // Rutas
	        bw.newLine();
	        bw.write("# Rutas");
	        bw.newLine();
	        for (Ruta r : aerolinea.getRutas()) {
	            Aeropuerto o = r.getOrigen();
	            Aeropuerto d = r.getDestino();
	            bw.write(String.join(";",
	                    "RUTA",
	                    r.getCodigoRuta(),
	                    o.getNombre(), o.getCodigo(), o.getNombreCiudad(),
	                    String.valueOf(o.getLatitud()), String.valueOf(o.getLongitud()),
	                    d.getNombre(), d.getCodigo(), d.getNombreCiudad(),
	                    String.valueOf(d.getLatitud()), String.valueOf(d.getLongitud()),
	                    r.getHoraSalida(), r.getHoraLlegada()
	            ));
	            bw.newLine();
	        }
	
	        // Vuelos
	        bw.newLine();
	        bw.write("# Vuelos");
	        bw.newLine();
	        for (Vuelo v : aerolinea.getVuelos()) {
	            bw.write("VUELO;" + v.getFecha() + ";" +
	                     v.getRuta().getCodigoRuta() + ";" +
	                     v.getAvion().getNombre());
	            bw.newLine();
	        }
	    }
	    }

}

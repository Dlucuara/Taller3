package uniandes.dpoo.aerolinea.persistencia;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import uniandes.dpoo.aerolinea.exceptions.AeropuertoDuplicadoException;
import uniandes.dpoo.aerolinea.exceptions.InformacionInconsistenteException;
import uniandes.dpoo.aerolinea.modelo.Aerolinea;
import uniandes.dpoo.aerolinea.modelo.Aeropuerto;
import uniandes.dpoo.aerolinea.modelo.Ruta;
import uniandes.dpoo.aerolinea.modelo.Vuelo;
import uniandes.dpoo.aerolinea.modelo.cliente.Avion;

public class PersistenciaAerolineJson  implements IPersistenciaAerolinea {

	public void cargarAerolinea(String archivo, Aerolinea aerolinea) throws 
    AeropuertoDuplicadoException, IOException, InformacionInconsistenteException  {
	
	String jsonCompleto = new String( Files.readAllBytes( new File( archivo ).toPath( ) ) );
	JSONObject json = new JSONObject(jsonCompleto);
		
	
	//aviones
	JSONArray aviones = json.getJSONArray("aviones");
	for(int i=0; i< aviones.length();i++) {
		JSONObject avion = aviones.getJSONObject(i);
		String nombre = avion.getString("nombre");
        int capacidad = avion.getInt("capacidad");
        
        
		
        Avion a = new Avion(nombre, capacidad);
        aerolinea.agregarAvion(a);
        
	
	}
	
	// rutas y aeropuertos
	Map<String, Aeropuerto> mapaAeropuertos = new HashMap<>();
	JSONArray rutas = json.getJSONArray("rutas");
	for (int i = 0; i < rutas.length(); i++) {
		JSONObject jRuta = rutas.getJSONObject(i);
		
		JSONObject jOrigen = jRuta.getJSONObject("origen");
        Aeropuerto origen = mapaAeropuertos.get(jOrigen.getString("codigo"));
        if (origen == null) {
            origen = new Aeropuerto(
                jOrigen.getString("nombre"),
                jOrigen.getString("codigo"),
                jOrigen.getString("nombreCiudad"),
                jOrigen.getDouble("latitud"),
                jOrigen.getDouble("longitud")
            );
            mapaAeropuertos.put(origen.getCodigo(), origen);
        }

        
        JSONObject jDestino = jRuta.getJSONObject("destino");
        Aeropuerto destino = mapaAeropuertos.get(jDestino.getString("codigo"));
        if (destino == null) {
            destino = new Aeropuerto(
                jDestino.getString("nombre"),
                jDestino.getString("codigo"),
                jDestino.getString("nombreCiudad"),
                jDestino.getDouble("latitud"),
                jDestino.getDouble("longitud")
            );
            mapaAeropuertos.put(destino.getCodigo(), destino);
        }
        
        if (origen.getCodigo().equals(destino.getCodigo())) {
            throw new AeropuertoDuplicadoException(
                "La ruta no puede tener el mismo aeropuerto como origen y destino: " + origen.getCodigo()
            );
        }
        String codigoRuta = jRuta.getString("codigoRuta");
        String horaSalida = jRuta.getString("horaSalida");
        String horaLlegada = jRuta.getString("horaLlegada");

        Ruta ruta = new Ruta(origen, destino,horaSalida,horaLlegada,codigoRuta);
        
        if (aerolinea.getRuta(ruta.getCodigoRuta()) != null) {
            throw new InformacionInconsistenteException("Ruta duplicada: " + ruta.getCodigoRuta());
        }
        aerolinea.agregarRuta(ruta);
	}
		
		//vuelos
	JSONArray vuelos = json.getJSONArray("vuelos");
    for (int i = 0; i < vuelos.length(); i++) {
        JSONObject jVuelo = vuelos.getJSONObject(i);

        String fecha = jVuelo.getString("fecha");
        String codigoRuta = jVuelo.getString("codigoRuta");
        String codigoAvion = jVuelo.getString("avion");

        try {
            aerolinea.programarVuelo(fecha, codigoRuta, codigoAvion);
        } catch (Exception e) {
            throw new InformacionInconsistenteException("Error al programar vuelo: " + e.getMessage());
        }
    }
	
}
    
	



	public void salvarAerolinea(String archivo, Aerolinea aerolinea) {
		JSONObject json = new JSONObject();

	    // Aviones 
	    JSONArray aviones = new JSONArray();
	    for (Avion a : aerolinea.getAviones()) {
	        JSONObject avionJson = new JSONObject();
	        avionJson.put("nombre", a.getNombre()); 
	        avionJson.put("capacidad", a.getCapacidad());
	        aviones.put(avionJson);
	    }
	    json.put("aviones", aviones);
	    
	    //rutas
	    
	    JSONArray rutas = new JSONArray();
	    for (Ruta r : aerolinea.getRutas()) {
	        JSONObject rutaJson = new JSONObject();
	        rutaJson.put("codigoRuta", r.getCodigoRuta()); 
	        rutaJson.put("horaSalida", r.getHoraSalida());
	        rutaJson.put("horaLlegada", r.getHoraLlegada());
	        
	        
	        Aeropuerto o = r.getOrigen();
	        JSONObject origenJson = new JSONObject();
	        origenJson.put("nombre", o.getNombre());
	        origenJson.put("codigo", o.getCodigo());
	        origenJson.put("nombreCiudad", o.getNombreCiudad());
	        origenJson.put("latitud", o.getLatitud());
	        origenJson.put("longitud", o.getLongitud());
	        rutaJson.put("origen", origenJson);

	       
	        Aeropuerto d = r.getDestino();
	        JSONObject destinoJson = new JSONObject();
	        destinoJson.put("nombre", d.getNombre());
	        destinoJson.put("codigo", d.getCodigo());
	        destinoJson.put("nombreCiudad", d.getNombreCiudad());
	        destinoJson.put("latitud", d.getLatitud());
	        destinoJson.put("longitud", d.getLongitud());
	        rutaJson.put("destino", destinoJson);
	        
	        rutas.put(rutaJson);
	    }
	    json.put("rutas", rutas);
	    
	    
	    //vuelos
	    JSONArray vuelos = new JSONArray();
	    for (Vuelo v : aerolinea.getVuelos()) {
	        JSONObject vueloJson = new JSONObject();
	        vueloJson.put("fecha", v.getFecha());
	        vueloJson.put("codigoRuta", v.getRuta().getCodigoRuta());
	        vueloJson.put("avion", v.getAvion().getNombre()); 
	        vuelos.put(vueloJson);
	    }
	    
	    
		
		
		
		
	    try (FileWriter writer = new FileWriter(archivo)) {
	        writer.write(json.toString(4)); 
	    } catch (JSONException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
	}
}

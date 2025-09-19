package uniandes.dpoo.aerolinea.modelo;

import java.util.Collection;
import java.util.HashMap;

import uniandes.dpoo.aerolinea.modelo.cliente.Avion;
import uniandes.dpoo.aerolinea.modelo.cliente.Cliente;
import uniandes.dpoo.aerolinea.modelo.tarifas.CalculadoraTarifas;
import uniandes.dpoo.aerolinea.tiquetes.GeneradorTiquetes;
import uniandes.dpoo.aerolinea.tiquetes.Tiquete;

public class Vuelo {
	
	private String fecha;
	private Ruta ruta;
	private Avion avion;
	private HashMap<String, Tiquete> tiquetes;

	
	
	public Vuelo(Ruta ruta, String fecha, Avion avion) {
		this.avion = avion;
		this.fecha = fecha;
		this.ruta = ruta;
		this.tiquetes = new HashMap<String, Tiquete>();
		
		
	}
	
	public Ruta getRuta() {
		 return this.ruta;
	}
	
	public String getFecha() {
		return this.fecha;
		
	}
	
	
	public Avion getAvion() {
		return this.avion;
	}
	
	public Collection<Tiquete> getTiquetes() {
		return tiquetes.values();
		
	}
	
	public int venderTiquetes(Cliente cliente, CalculadoraTarifas calculadora, int cantidad) {
		int vendidos  =0;
		
		for(int i=0; i< cantidad;i++) {
			
			int tarifa = calculadora.calcularTarifa(this, cliente);
			Tiquete nuevo = GeneradorTiquetes.generarTiquete(this, cliente,tarifa);
		
			if (nuevo != null) {
				tiquetes.put(nuevo.getCodigo(), nuevo);
				vendidos++;
			}
		}
		return vendidos;
	}
	
	
	
	public boolean equals(Object obj) {
		if(this ==obj) {
			return true;
		}
		if (!(obj instanceof Vuelo)) {
			return false;
		}
        Vuelo otro = (Vuelo) obj;
        return this.ruta.equals(otro.ruta) &&
               this.fecha.equals(otro.fecha) &&
               this.avion.equals(otro.avion);
        
    }
	
}

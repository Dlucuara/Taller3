package uniandes.dpoo.aerolinea.modelo.cliente;

import java.util.ArrayList;

import uniandes.dpoo.aerolinea.modelo.Vuelo;
import uniandes.dpoo.aerolinea.tiquetes.Tiquete;

public abstract class  Cliente {
	
	 private ArrayList<Tiquete> tiquetesSinUsar = new ArrayList<Tiquete>();
	 private ArrayList<Tiquete> tiquetesUsados = new ArrayList<Tiquete>();
	
	
	public Cliente() {
		
	}
	
	public abstract String getTipoCliente();
	
	public abstract String getIdentificador();
	
	public void agregarTiquete(Tiquete tiquete) {
		tiquetesSinUsar.add(tiquete);
	}
	
	public int calcularValorTotalTiquetes() {
		int total = 0;
		for(Tiquete t: tiquetesSinUsar) {
			total+= t.getTarifa();
		}
		for(Tiquete t: tiquetesUsados) {
			total+= t.getTarifa();
		}
		
		return total;
	}
	
	public void usarTiquetes(Vuelo vuelo) {
		 ArrayList<Tiquete> usadosEnEsteVuelo = new ArrayList<>();
		 
		 for(Tiquete t: tiquetesSinUsar) {
			 
			 if(t.getVuelo().equals(vuelo)) {
				 t.marcarComoUsado();
				 usadosEnEsteVuelo.add(t);
			 }
			 
			 
		 }
		 
		 tiquetesSinUsar.removeAll(usadosEnEsteVuelo);
		 tiquetesUsados.addAll(usadosEnEsteVuelo);
		 
	}
	
	
	
}

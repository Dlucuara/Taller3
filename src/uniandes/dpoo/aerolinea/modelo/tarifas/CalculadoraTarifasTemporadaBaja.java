package uniandes.dpoo.aerolinea.modelo.tarifas;

import uniandes.dpoo.aerolinea.modelo.Vuelo;
import uniandes.dpoo.aerolinea.modelo.cliente.Cliente;
import uniandes.dpoo.aerolinea.modelo.cliente.ClienteCorporativo;

public class CalculadoraTarifasTemporadaBaja extends CalculadoraTarifas {
	protected static final int COSTO_POR_KM_NATURAL = 600;
	protected static final int COSTO_POR_KM_CORPORATIVO = 900;
	protected static final double DESCUENTO_PEQ = 0.02;
	protected static final double DESCUENTO_MEDIANAS = 0.1;
	protected static final double DESCUENTO_GRANDES = 0.2;
	
	
	
	@Override
	protected int calcularCostoBase(Vuelo vuelo, Cliente cliente) {
		 int distanciaVuelo = calcularDistanciaVuelo(vuelo.getRuta());
		 
		 int costoBase = 0;
		 if(cliente.getTipoCliente() == "Natural") {
			  costoBase = COSTO_POR_KM_NATURAL * distanciaVuelo;
		 }
		 if(cliente.getTipoCliente() == "Corporativo") {
			 costoBase = COSTO_POR_KM_CORPORATIVO * distanciaVuelo;
		 }
		 
		return costoBase;
	}

	@Override
	protected double calcularPorcentajeDescuento(Cliente cliente) {
		double descuento = 0;
		if (cliente.getTipoCliente().equals("Natural")) {
			 return descuento;
		}
		
		ClienteCorporativo corp = (ClienteCorporativo) cliente;
	    int tamano = corp.getTamano();

		 
		if (cliente.getTipoCliente().equals("Corporativo")) {
			 if(tamano == 3) {
				  descuento = DESCUENTO_PEQ; 
			 }
			 if(tamano == 2) {
				  descuento = DESCUENTO_MEDIANAS;
				 
			 }
			 if(tamano == 1) {
				  descuento = DESCUENTO_GRANDES;
				 
			 }
			 
		}
		return descuento;
		
	}

	
	
}

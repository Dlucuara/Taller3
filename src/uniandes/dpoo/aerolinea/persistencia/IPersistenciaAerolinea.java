package uniandes.dpoo.aerolinea.persistencia;

import java.io.IOException;

import uniandes.dpoo.aerolinea.exceptions.AeropuertoDuplicadoException;
import uniandes.dpoo.aerolinea.exceptions.InformacionInconsistenteException;
import uniandes.dpoo.aerolinea.modelo.Aerolinea;

public interface IPersistenciaAerolinea {
	
	public void cargarAerolinea(String arhivo, Aerolinea aerolinea) throws  AeropuertoDuplicadoException, IOException, InformacionInconsistenteException, Exception ;
	public void salvarAerolinea(String arhivo, Aerolinea aerolinea) throws IOException ;

}

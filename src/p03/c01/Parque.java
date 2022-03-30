package src.p03.c01;

import java.util.Enumeration;
import java.util.Hashtable;

public class Parque implements IParque{


	private static final int AFORO_MAX = 50;
	private static final int AFORO_MIN = 0;
	private int contadorPersonasTotales;
	private Hashtable<String, Integer> contadoresPersonasPuerta;
	
	
	public Parque() {
		contadorPersonasTotales = 0;
		contadoresPersonasPuerta = new Hashtable<String, Integer>();
	}


	@Override
	public synchronized void  entrarAlParque(String puerta){		//Se a�ade "synchronized"
		
		// Si no hay entradas por esa puerta, inicializamos
		if (contadoresPersonasPuerta.get(puerta) == null){
			contadoresPersonasPuerta.put(puerta, 0);
		}
		
		// Se a�ade la comprobaci�n
		comprobarAntesDeEntrar();
	
		// Aumentamos el contador total y el individual
		contadorPersonasTotales++;		
		contadoresPersonasPuerta.put(puerta, contadoresPersonasPuerta.get(puerta)+1);
		
		// Imprimimos el estado del parque
		imprimirInfo(puerta, "Entrada");
		
		// Se notifica al resto de hilos
		notifyAll();
		
		
		// Comprobamos invariantes
		checkInvariante();
		
	}
	
	
	
	// M�todo salirDelParque
	@Override
	public synchronized void salirDelParque(String puerta) {
		
		// Si no hay salidas por esa puerta, inicializamos
		if (contadoresPersonasPuerta.get(puerta) == null){
			contadoresPersonasPuerta.put(puerta, 0);
		}
		
		// Se a�ade la comprobaci�n
		comprobarAntesDeSalir();
	
		
		// Disminuimos el contador total y el individual
		contadorPersonasTotales--;		
		contadoresPersonasPuerta.put(puerta, contadoresPersonasPuerta.get(puerta)-1);
		
		// Imprimimos el estado del parque
		imprimirInfo(puerta, "Salida");
		
		// Se notifica al resto de hilos
		notifyAll();
		
		
		// Comprobamos invariantes
		checkInvariante();
		
	}
	
	
	private void imprimirInfo (String puerta, String movimiento){
		System.out.println(movimiento + " por puerta " + puerta);
		System.out.println("--> Personas en el parque " + contadorPersonasTotales); //+ " tiempo medio de estancia: "  + tmedio);
		
		// Iteramos por todas las puertas e imprimimos sus entradas
		for(String p: contadoresPersonasPuerta.keySet()){
			System.out.println("----> Por puerta " + p + " " + contadoresPersonasPuerta.get(p));
		}
		System.out.println(" ");
	}
	
	private int sumarContadoresPuerta() {
		int sumaContadoresPuerta = 0;
			Enumeration<Integer> iterPuertas = contadoresPersonasPuerta.elements();
			while (iterPuertas.hasMoreElements()) {
				sumaContadoresPuerta += iterPuertas.nextElement();
			}
		return sumaContadoresPuerta;
	}
	
	protected void checkInvariante() {
		assert sumarContadoresPuerta() == contadorPersonasTotales : "INV: La suma de contadores de las puertas debe ser igual al valor del contador del parte";
		//Se a�aden los dos invariantes osbre salidas y entradas al parque
		assert contadorPersonasTotales <= AFORO_MAX: "No se puede superar el aforo m�ximo";
		assert contadorPersonasTotales >= AFORO_MIN: "El aforo no puede ser menor que el aforo m�nimo";

		
		
		
	}

	protected void comprobarAntesDeEntrar(){

		while(contadorPersonasTotales >= AFORO_MAX) {
			try {
				wait();
				} catch (InterruptedException e) {
					System.out.print(e.toString());
				}	
		}	

	}

	protected void comprobarAntesDeSalir(){	

		while( contadorPersonasTotales <= AFORO_MIN) {
			try {
				wait();
				} catch (InterruptedException e) {
					System.out.print(e.toString());
				}	
		}	

	}


}

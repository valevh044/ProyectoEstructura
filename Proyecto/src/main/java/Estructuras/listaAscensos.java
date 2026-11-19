
package Estructuras;

import datos.EmpleadoAscenso;

public class listaAscensos {

    
    private nodoAscenso cabeza;

    
    public listaAscensos() {
        cabeza = null;
    }

    
    public boolean estaVacia() {
        return cabeza == null;
    }

    
    public void agregar(EmpleadoAscenso empleado) {

        nodoAscenso nuevo = new nodoAscenso(empleado);

        if (estaVacia()) {
            cabeza = nuevo;
        } else {

            nodoAscenso actual = cabeza;

            while (actual.getSiguiente() != null) {
                actual = actual.getSiguiente();
            }

            actual.setSiguiente(nuevo);
        }
    }
    
    public int cantidad() {

        int contador = 0;

        nodoAscenso actual = cabeza;

        while (actual != null) {
            contador++;
            actual = actual.getSiguiente();
        }

        return contador;
    }

    
    public EmpleadoAscenso obtener(int posicion) {

        if (posicion < 0) {
            return null;
        }

        nodoAscenso actual = cabeza;
        int contador = 0;

        while (actual != null) {

            if (contador == posicion) {
                return actual.getEmpleado();
            }

            actual = actual.getSiguiente();
            contador++;
        }

        return null;
    }
}

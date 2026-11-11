
package Estructuras;

import datos.EmpleadoAscenso;

public class ListaAscensos {

    
    private NodoAscenso cabeza;

    
    public ListaAscensos() {
        cabeza = null;
    }

    
    public boolean estaVacia() {
        return cabeza == null;
    }

    
    public void agregar(EmpleadoAscenso empleado) {

        NodoAscenso nuevo = new NodoAscenso(empleado);

        if (estaVacia()) {
            cabeza = nuevo;
        } else {

            NodoAscenso actual = cabeza;

            while (actual.getSiguiente() != null) {
                actual = actual.getSiguiente();
            }

            actual.setSiguiente(nuevo);
        }
    }

    
    public void mostrar() {

        NodoAscenso actual = cabeza;

        while (actual != null) {

            System.out.println(actual.getEmpleado());

            actual = actual.getSiguiente();
        }
    }

    
    public int cantidad() {

        int contador = 0;

        NodoAscenso actual = cabeza;

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

        NodoAscenso actual = cabeza;
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

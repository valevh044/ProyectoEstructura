/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Estructuras;

/**
 *
 * @author valev
 */
import datos.material;

public class listaEnlazada {

    private Nodo cabeza;
    private int cantidad;

    public listaEnlazada() {
        cabeza = null;
        cantidad = 0;
    }

    public boolean estaVacia() {
        return cabeza == null;
    }

    public int tamano() {
        return cantidad;
    }

    public void agregar(material dato) {
        Nodo nuevo = new Nodo(dato);
        if (estaVacia()) {
            cabeza = nuevo;
        } else {

            Nodo actual = cabeza;
            while (actual.getSiguiente() != null) {
                actual = actual.getSiguiente();
            }

            actual.setSiguiente(nuevo);
        }
        cantidad++;
    }

    public material obtener(int posicion) {

        if (posicion < 0 || posicion >= cantidad) {
            return null;
        }

        Nodo actual = cabeza;

        for (int i = 0; i < posicion; i++) {
            actual = actual.getSiguiente();
        }return actual.getDato();
    }

    public material eliminar(int posicion) {

        if (posicion < 0 || posicion >= cantidad) {
            return null;
        }

        material eliminado;

        if (posicion == 0) {
            eliminado = cabeza.getDato();
            cabeza = cabeza.getSiguiente();

        } else {

            Nodo actual = cabeza;
            for (int i = 0; i < posicion - 1; i++) {
                actual = actual.getSiguiente();
            }

            Nodo borrar = actual.getSiguiente();
            eliminado = borrar.getDato();
            actual.setSiguiente(borrar.getSiguiente());
        }
        cantidad--;
        return eliminado;
    }

    public void limpiar() {
        cabeza = null;
        cantidad = 0;
    }
}

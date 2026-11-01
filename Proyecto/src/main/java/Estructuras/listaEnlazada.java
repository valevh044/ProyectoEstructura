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
import datos.pedido;

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
    public void agregar(pedido nuevoPedido) {

        Nodo nuevo = new Nodo(nuevoPedido);

        if (cabeza == null) {
            cabeza = nuevo;
        } else {
            Nodo actual = cabeza;

            while (actual.getSiguiente() != null) {
                actual = actual.getSiguiente();
            }

            actual.setSiguiente(nuevo);
        }
    }

   
    public pedido buscar(String identificador) {

        Nodo actual = cabeza;

        while (actual != null) {

            if (actual.getDato().getIdentificador()
                    .equals(identificador)) {
                return actual.getDato();
            }

            actual = actual.getSiguiente();
        }

        return null;
    }

    
    public boolean actualizarEstado(String identificador, String estado) {

        pedido encontrado = buscar(identificador);

        if (encontrado != null) {
            if (estado.equals("Disponible")
                    || estado.equals("En construcción")
                    || estado.equals("Completado")) {

                encontrado.setEstado(estado);
                return true;
            }
        }

        return false;
    }

    
    public boolean eliminar(String identificador) {

        if (cabeza == null) {
            return false;
        }

        if (cabeza.getDato().getidentificador
                .equals(identificador)) {
            cabeza = cabeza.getSiguiente();
            return true;
        }

        Nodo actual = cabeza;

        while (actual.getSiguiente() != null) {

            if (actual.getSiguiente().getDato()
                    .getIdentificador().equals(identificador)) {

                actual.setSiguiente(
                        actual.getSiguiente().getSiguiente()
                );

                return true;
            }

            actual = actual.getSiguiente();
        }

        return false;
    }

    
    public void mostrarPedidos() {

        Nodo actual = cabeza;

        while (actual != null) {
            System.out.println(actual.getDato());
            actual = actual.getSiguiente();
        }
    }
}

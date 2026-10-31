/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package datos;
import Estructuras.Nodo;
import datos.material;
import datos.tipoMaterial;
/**
 *
 * @author valev
 */
public class CintaTransportadora {

    private Nodo cabeza;
    private int cantidadActual;
    private int capacidadMaxima;

    public CintaTransportadora(int numeroFabrica) {
        this.cabeza = null;
        this.cantidadActual = 0;
        configurarCapacidadPorFabrica(numeroFabrica);
    }

    public final void configurarCapacidadPorFabrica(int numeroFabrica) {
        if (numeroFabrica == 1) {
            this.capacidadMaxima = 3;
        } else if (numeroFabrica == 2) {
            this.capacidadMaxima = 4;
        } else {
            this.capacidadMaxima = 5;
        }
    }

    public boolean estaVacia() {
        return cabeza == null;
    }

    public int getCantidadActual() {
        return cantidadActual;
    }

    public int getCapacidadMaxima() {
        return capacidadMaxima;
    }

    public Nodo getCabeza() {
        return cabeza;
    }

    public boolean agregarMaterial(material nuevoMaterial) {
        if (cantidadActual >= capacidadMaxima) {
            return false;
        }
        Nodo nuevo = new Nodo(nuevoMaterial);
        if (estaVacia()) {
            cabeza = nuevo;
        } else {
            Nodo actual = cabeza;
            while (actual.getSiguiente() != null) {
                actual = actual.getSiguiente();
            }
            actual.setSiguiente(nuevo);
        }
        cantidadActual++;
        return true;
    }

    public boolean contieneMaterial(tipoMaterial tipoBuscado) {
        if (tipoBuscado == null) {
            return false;
        }
        Nodo actual = cabeza;
        while (actual != null) {
            if (actual.getDato().getTipo() == tipoBuscado) {
                return true;
            }
            actual = actual.getSiguiente();
        }
        return false;
    }


    public material retirarMaterial(tipoMaterial tipoBuscado) {
        if (estaVacia() || tipoBuscado == null) {
            return null;
        }

        if (cabeza.getDato().getTipo() == tipoBuscado) {
            material retirado = cabeza.getDato();
            cabeza = cabeza.getSiguiente();
            cantidadActual--;
            return retirado;
        }

        Nodo actual = cabeza;
        while (actual.getSiguiente() != null) {
            if (actual.getSiguiente().getDato().getTipo() == tipoBuscado) {
                material retirado = actual.getSiguiente().getDato();
                actual.setSiguiente(actual.getSiguiente().getSiguiente());
                cantidadActual--;
                return retirado;
            }
            actual = actual.getSiguiente();
        }
        return null;
    }

    public material retirarPorPosicion(int posicion) {
        if (estaVacia() || posicion < 0 || posicion >= cantidadActual) {
            return null;
        }

        material retirado;
        if (posicion == 0) {
            retirado = cabeza.getDato();
            cabeza = cabeza.getSiguiente();
        } else {
            Nodo actual = cabeza;
            for (int i = 0; i < posicion - 1; i++) {
                actual = actual.getSiguiente();
            }
            retirado = actual.getSiguiente().getDato();
            actual.setSiguiente(actual.getSiguiente().getSiguiente());
        }

        cantidadActual--;
        return retirado;
    }
}



package Estructuras;

/**
 *
 * @author valev
 */
import datos.material;

public class Nodo {

    private material dato;
    private Nodo siguiente;

    public Nodo(material dato) {
        this.dato = dato;
        this.siguiente = null;
    }

    public material getDato() {
        return dato;
    }

    public void setDato(material dato) {
        this.dato = dato;
    }

    public Nodo getSiguiente() {
        return siguiente;
    }

    public void setSiguiente(Nodo siguiente) {
        this.siguiente = siguiente;
    }
}

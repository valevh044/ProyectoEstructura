
package Estructuras;

/**
 *
 * @author valev
 */
import datos.pedido;
public class NodoPedido {
     private pedido dato;
    private NodoPedido siguiente;

    public NodoPedido(pedido dato) {
        this.dato = dato;
        this.siguiente = null;
    }

    public pedido getDato() {
        return dato;
    }

    public void setDato(pedido dato) {
        this.dato = dato;
    }

    public NodoPedido getSiguiente() {
        return siguiente;
    }

    public void setSiguiente(NodoPedido siguiente) {
        this.siguiente = siguiente;
    }
}

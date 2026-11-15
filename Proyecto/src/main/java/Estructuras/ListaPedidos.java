
package Estructuras;

/**
 *
 * @author valev
 */
import datos.pedido;
public class ListaPedidos {
     private NodoPedido cabeza;
    private int cantidad;

    public ListaPedidos() {
        cabeza = null;
        cantidad = 0;
    }

    public boolean estaVacia() {
        return cabeza == null;
    }

    public int tamano() {
        return cantidad;
    }

    public void agregar(pedido nuevoPedido) {

        if (nuevoPedido == null) {
            return;
        }

        NodoPedido nuevo = new NodoPedido(nuevoPedido);

        if (estaVacia()) {
            cabeza = nuevo;
        } else {
            NodoPedido actual = cabeza;

            while (actual.getSiguiente() != null) {
                actual = actual.getSiguiente();
            }

            actual.setSiguiente(nuevo);
        }

        cantidad++;
    }

    public pedido obtener(int posicion) {

        if (posicion < 0 || posicion >= cantidad) {
            return null;
        }

        NodoPedido actual = cabeza;

        for (int i = 0; i < posicion; i++) {
            actual = actual.getSiguiente();
        }

        return actual.getDato();
    }

    public pedido buscar(String identificador) {

        NodoPedido actual = cabeza;

        while (actual != null) {

            if (actual.getDato().getIdentificador()
                    .equals(identificador)) {
                return actual.getDato();
            }

            actual = actual.getSiguiente();
        }

        return null;
    }

    public boolean actualizarEstado(String identificador,
                                    String estado) {

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

        if (cabeza.getDato().getIdentificador()
                .equals(identificador)) {

            cabeza = cabeza.getSiguiente();
            cantidad--;
            return true;
        }

        NodoPedido actual = cabeza;

        while (actual.getSiguiente() != null) {

            if (actual.getSiguiente().getDato()
                    .getIdentificador().equals(identificador)) {

                actual.setSiguiente(
                        actual.getSiguiente().getSiguiente()
                );

                cantidad--;
                return true;
            }

            actual = actual.getSiguiente();
        }

        return false;
    }

    public NodoPedido getCabeza() {
        return cabeza;
    }
}

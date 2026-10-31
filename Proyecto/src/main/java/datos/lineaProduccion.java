/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package datos;

/**
 *
 * @author valev
 */
import Estructuras.listaEnlazada;

public class lineaProduccion {

    private int numero;
    private String estado;
    private pedido pedidoActual;
    private listaEnlazada materialesRecibidos;

    public lineaProduccion(int numero) {
        this.numero = numero;
        this.estado = "Disponible";
        this.pedidoActual = null;
        this.materialesRecibidos = new listaEnlazada();
    }

    public int getNumero() {
        return numero;
    }

    public String getEstado() {
        return estado;
    }

    public pedido getPedidoActual() {
        return pedidoActual;
    }

    public listaEnlazada getMaterialesRecibidos() {
        return materialesRecibidos;
    }

    public boolean estaLibre() {
        return pedidoActual == null;
    }

    public boolean asignarPedido(pedido nuevoPedido) {

        if (estaLibre() && nuevoPedido != null) {
            pedidoActual = nuevoPedido;
            estado = "Ocupada";
            return true;
        }

        return false;
    }

    public boolean agregarMaterial(material nuevoMaterial) {

    if (nuevoMaterial == null || estaLibre()) {
        return false;
    }
    materialesRecibidos.agregar(nuevoMaterial);
    return true;
}

    public boolean completarConstruccion() {

        if (estaLibre()) {
            return false;
        }
        pedidoActual = null;
        estado = "Disponible";
        materialesRecibidos.limpiar();
        return true;
    }

    public boolean cancelarConstruccion() {
        if (estaLibre()) {
            return false;
        }
        pedidoActual = null;
        estado = "Disponible";
        materialesRecibidos.limpiar();
        return true;
    }
}

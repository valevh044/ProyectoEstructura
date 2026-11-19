package datos;

import Estructuras.ListaPedidos;

/**
 *
 * @author felinava06
 */
public class Fabrica {

    private int numeroFabrica;
    private double objetivoEconomico;
    private double dineroAcumulado;

    private CintaTransportadora cinta;
    private basurero basurero;

    private lineaProduccion linea1;
    private lineaProduccion linea2;
    private lineaProduccion linea3;

    private ListaPedidos pedidos;

    public Fabrica(int numeroFabrica) {

        this.numeroFabrica = numeroFabrica;
        this.dineroAcumulado = 0;

        configurarObjetivo();

        cinta = new CintaTransportadora(numeroFabrica);
        basurero = new basurero();

        linea1 = new lineaProduccion(1);
        linea2 = new lineaProduccion(2);
        linea3 = new lineaProduccion(3);

        pedidos = new ListaPedidos();

        generarPedidosIniciales();
        abastecerCinta();
    }

    private void configurarObjetivo() {

        if (numeroFabrica == 1) {
            objetivoEconomico = 90000;
        } else if (numeroFabrica == 2) {
            objetivoEconomico = 125000;
        } else if (numeroFabrica == 3) {
            objetivoEconomico = 275000;
        } else {
            throw new IllegalArgumentException(
                    "Número de fábrica inválido"
            );
        }
    }

    public void generarPedidosIniciales() {

        for (int i = 0; i < 15; i++) {

            int indice = generarIndiceAuto();
            tipoAuto auto = tipoAuto.obtenerPorIndice(indice);
            pedido nuevo = new pedido(auto);
            pedidos.agregar(nuevo);
        }
    }

    private int generarIndiceAuto() {

        double aleatorio = Math.random();

        if (numeroFabrica == 1) {
            return aleatorio < 0.5 ? 1 : 2;
        }

        if (numeroFabrica == 2) {
            if (aleatorio < 0.33) {
                return 1;
            } else if (aleatorio < 0.66) {
                return 2;
            } else {
                return 3;
            }
        }

        if (aleatorio < 0.33) {
            return 2;
        } else if (aleatorio < 0.66) {
            return 3;
        } else {
            return 4;
        }
    }

    public boolean asignarPedido(
            String identificador, int numeroLinea) {

        pedido seleccionado = pedidos.buscar(identificador);

        if (seleccionado == null
                || !seleccionado.getEstado().equals("Disponible")) {
            return false;
        }
        lineaProduccion linea = obtenerLinea(numeroLinea);

        if (linea == null) {
            return false;
        }
        if (linea.asignarPedido(seleccionado)) {
            seleccionado.setEstado("En construcción");
            return true;
        }

        return false;
    }

    public boolean agregarMaterialALinea(int posicionMaterial, int numeroLinea) {

        lineaProduccion linea = obtenerLinea(numeroLinea);

        if (linea == null || linea.estaLibre()) {
            return false;
        }

        pedido pedidoActual = linea.getPedidoActual();

        tipoAuto auto = obtenerAutoPorNombre(
                pedidoActual.getTipoAutomovil()
        );

        if (auto == null) {
            return false;
        }

        material seleccionado
                = cinta.retirarPorPosicion(posicionMaterial);

        if (seleccionado == null) {
            return false;
        }

        String nombreMaterial
                = seleccionado.getTipo().getAuto();

        boolean necesario
                = nombreMaterial.equals(auto.getMaterialRequerido1())
                || nombreMaterial.equals(auto.getMaterialRequerido2())
                || (auto.getMaterialRequerido3() != null
                && nombreMaterial.equals(auto.getMaterialRequerido3()));

        if (!necesario) {
            cinta.agregarMaterial(seleccionado);
            return false;
        }
        if (linea.getMaterialesRecibidos()
                .contieneMaterial(nombreMaterial)) {
            cinta.agregarMaterial(seleccionado);
            return false;
        }
        linea.agregarMaterial(seleccionado);
        abastecerCinta();
        return true;
    }

    public boolean completarConstruccion(int numeroLinea) {
        lineaProduccion linea = obtenerLinea(numeroLinea);
        if (linea == null || linea.estaLibre()) {
            return false;
        }
        pedido actual = linea.getPedidoActual();
        tipoAuto auto = obtenerAutoPorNombre(actual.getTipoAutomovil());
        if (auto == null) {
            return false;
        }
        if (!linea.tieneMaterialesNecesarios(auto)) {
            return false;
        }
        actual.setEstado("Completado");
        dineroAcumulado += auto.getGanancia();
        linea.completarConstruccion();
        return true;
    }
    
    public boolean cancelarPedido(int numeroLinea) {
        lineaProduccion linea = obtenerLinea(numeroLinea);
        if (linea == null || linea.estaLibre()) {
            return false;
        }
        linea.getPedidoActual().setEstado("Disponible");
        return linea.cancelarConstruccion();
    }

    private tipoAuto obtenerAutoPorNombre(String nombre) {
        for (int i = 1; i <= 4; i++) {
            tipoAuto auto = tipoAuto.obtenerPorIndice(i);
            if (auto.getNombre().equals(nombre)) {
                return auto;
            }
        }

        return null;
    }

    private lineaProduccion obtenerLinea(int numero) {

        if (numero == 1) {
            return linea1;
        } else if (numero == 2) {
            return linea2;
        } else if (numero == 3) {
            return linea3;
        }

        return null;
    }

    //Si la cinta tiene 3 materiales o menos, la completa con materiales aleatorios.
    public void abastecerCinta() {
        if (cinta.getCantidadActual() <= 3) {
            while (cinta.getCantidadActual() < cinta.getCapacidadMaxima()) {
                cinta.agregarMaterial(new material(tipoMaterial.aleatorio()));
            }
        }
    }

    public double desecharMaterial(int posicion) {

        material eliminado = cinta.retirarPorPosicion(posicion);

        if (eliminado == null) {
            return 0;
        }

        double perdida = basurero.desecharMateriales(eliminado);
        dineroAcumulado -= perdida;
        abastecerCinta();
        return perdida;
    }

    public boolean objetivoAlcanzado() {
        return dineroAcumulado >= objetivoEconomico;
    }

    public int getNumeroFabrica() {
        return numeroFabrica;
    }

    public double getObjetivoEconomico() {
        return objetivoEconomico;
    }

    public double getDineroAcumulado() {
        return dineroAcumulado;
    }

    public CintaTransportadora getCinta() {
        return cinta;
    }

    public basurero getBasurero() {
        return basurero;
    }

    public lineaProduccion getLinea1() {
        return linea1;
    }

    public lineaProduccion getLinea2() {
        return linea2;
    }

    public lineaProduccion getLinea3() {
        return linea3;
    }

    public ListaPedidos getPedidos() {
        return pedidos;
    }
}

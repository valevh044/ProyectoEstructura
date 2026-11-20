package datos;


public class pedido {
    
    private static int contador = 1;

    private String identificador;
    private String tipoAutomovil;
    private String estado;
    private tipoAuto tipo;                       

    public pedido(String tipoAutomovil) {
        this.identificador = "PED-" + contador++;
        this.tipoAutomovil = tipoAutomovil;
        this.estado = "Disponible";
    }

    
    public pedido(tipoAuto tipo) {
        this(tipo.getNombre());                  
        this.tipo = tipo;
    }

    public String getIdentificador() {
        return identificador;
    }

    public String getTipoAutomovil() {
        return tipoAutomovil;
    }

    public void setTipoAutomovil(String tipoAutomovil) {
        this.tipoAutomovil = tipoAutomovil;
    }

    
    public tipoAuto getTipo() {
        return tipo;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        if (estado.equals("Disponible")
                || estado.equals("En construcción")
                || estado.equals("Completado")) {
            this.estado = estado;
        }
    }

    @Override
    public String toString() {
        return identificador + " - " + tipoAutomovil + " - " + estado;
    }
}
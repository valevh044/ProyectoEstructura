package datos;


public class pedido {
    
    private static int contador = 1;

    private String identificador;
    private String tipoAutomovil;
    private String estado;
    private tipoAuto tipo;                       // NUEVO: el auto completo (nombre, ganancia, materiales)

    public pedido(String tipoAutomovil) {
        this.identificador = "PED-" + contador++;
        this.tipoAutomovil = tipoAutomovil;
        this.estado = "Disponible";
    }

    // NUEVO: constructor que recibe el tipo de auto completo
    public pedido(tipoAuto tipo) {
        this(tipo.getNombre());                  // reutiliza el constructor de arriba (genera el ID y el estado)
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

    // NUEVO: permite obtener la ganancia y los materiales requeridos
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
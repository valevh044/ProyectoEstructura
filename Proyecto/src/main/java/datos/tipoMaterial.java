package datos;

/**
 *
 * @author valev
 */
public class tipoMaterial {

    private String auto;
    private double valor;

    //Tipos de materiales
    
   
    
    public tipoMaterial(String auto, double valor) {
        this.auto = auto;
        this.valor = valor;
    }

    public String getAuto() {
        return auto;
    }
    
    public void setAuto(String auto) {
        this.auto = auto;
    }

    public double getValor() {
        return valor;
    }

    public void setValor(double valor) {
        this.valor = valor;
    }
    
    public double calcularPerdida() {
        return valor * 0.60;
    }

    @Override
    public String toString() {
        return auto + " - $" + valor;
    }
}

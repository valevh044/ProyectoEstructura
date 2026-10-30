package datos;

/**
 *
 * @author valev
 */
public class tipoMaterial {

    private String auto;
    private double valor;

    //Tipos de materiales
    
    tipoMaterial motorLujo = new tipoMaterial("Motor de lujo",500);
    
    tipoMaterial motorEspecial = new tipoMaterial("Motor especial deportivo",2000);
    
    tipoMaterial carroceria = new tipoMaterial("Carrocería",500);
    
    tipoMaterial carroceriaEspecial = new tipoMaterial("Carrocería especial",800);
    
    tipoMaterial llantasDeportivas = new tipoMaterial("Llantas deportivas",2000);
    
    tipoMaterial llantasUnicas = new tipoMaterial("LLantas únicas de trabajo",800);
    
    
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

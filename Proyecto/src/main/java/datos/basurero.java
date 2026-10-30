
package datos;

/**
 *
 * @author valev
 */
public class basurero {
    private double perdidasAcumuladas;
    
    public basurero(){
        perdidasAcumuladas= 0;
    }
    
    public double getPerdidasAcumuladas() {
        return perdidasAcumuladas;
    }
    
    public double desecharMateriales(material material){
        if(material == null || material.getTipo() == null){
            return 0;
        }
        double perdida = material.getTipo().calcularPerdida();
        
        perdidasAcumuladas += perdida;
        
        return perdida;
    }

    public void reiniciar(){
        perdidasAcumuladas=0;
    }
}

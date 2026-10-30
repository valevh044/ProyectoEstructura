
package datos;

/**
 *
 * @author valev
 */
public class material {
    private tipoMaterial tipo;

    public material(tipoMaterial tipo) {
        this.tipo = tipo;
    }

    public tipoMaterial getTipo() {
        return tipo;
    }

    public void setTipo(tipoMaterial tipo) {
        this.tipo = tipo;
    }
    
    @Override
    public String toString() {

        if (tipo == null) {
            return "Material sin tipo";
        }

        return tipo.toString();
    }
}

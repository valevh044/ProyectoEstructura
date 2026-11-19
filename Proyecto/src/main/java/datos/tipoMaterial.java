package datos;

/**
 * Representa un tipo de material para construir autos.
 * Los 6 materiales del juego se definen como constantes (static final),
 * así existe una sola instancia de cada uno para todo el programa.
 *
 * @author valev
 */
public class tipoMaterial {

    private String auto;
    private double valor;

    // ===================== MATERIALES DEL JUEGO =====================
    public static final tipoMaterial MOTOR_LUJO = new tipoMaterial("Motor de lujo", 500);
    public static final tipoMaterial MOTOR_DEPORTIVO = new tipoMaterial("Motor especial deportivo", 2000);
    public static final tipoMaterial CARROCERIA = new tipoMaterial("Carrocería", 500);
    public static final tipoMaterial CARROCERIA_ESPECIAL = new tipoMaterial("Carrocería especial", 800);
    public static final tipoMaterial LLANTAS_DEPORTIVAS = new tipoMaterial("Llantas deportivas", 2000);
    public static final tipoMaterial LLANTAS_TRABAJO = new tipoMaterial("Llantas únicas de trabajo", 800);

    // ===================== CONSTRUCTOR =====================
    public tipoMaterial(String auto, double valor) {
        this.auto = auto;
        this.valor = valor;
    }

    // ===================== GETTERS Y SETTERS =====================
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

    // ===================== MÉTODOS =====================

    /** Pérdida por desechar el material: 60 % de su valor. */
    public double calcularPerdida() {
        return valor * 0.60;
    }

    /** Devuelve uno de los 6 materiales al azar (para la cinta transportadora). */
    public static tipoMaterial aleatorio() {
        switch ((int) (Math.random() * 6)) {
            case 0:
                return MOTOR_LUJO;
            case 1:
                return MOTOR_DEPORTIVO;
            case 2:
                return CARROCERIA;
            case 3:
                return CARROCERIA_ESPECIAL;
            case 4:
                return LLANTAS_DEPORTIVAS;
            default:
                return LLANTAS_TRABAJO;
        }
    }

    @Override
    public String toString() {
        return auto + " - $" + valor;
    }
}
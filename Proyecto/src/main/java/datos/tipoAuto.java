/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package datos;

/**
 *
 * @author valev
 */
public class tipoAuto {
    private String nombre;
    private double ganancia;
    private String materialRequerido1;
    private String materialRequerido2;
    private String materialRequerido3; 

    public tipoAuto(String nombre, double ganancia, String mat1, String mat2, String mat3) {
        this.nombre = nombre;
        this.ganancia = ganancia;
        this.materialRequerido1 = mat1;
        this.materialRequerido2 = mat2;
        this.materialRequerido3 = mat3;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public double getGanancia() {
        return ganancia;
    }

    public void setGanancia(double ganancia) {
        this.ganancia = ganancia;
    }

    public String getMaterialRequerido1() {
        return materialRequerido1;
    }

    public void setMaterialRequerido1(String materialRequerido1) {
        this.materialRequerido1 = materialRequerido1;
    }

    public String getMaterialRequerido2() {
        return materialRequerido2;
    }

    public void setMaterialRequerido2(String materialRequerido2) {
        this.materialRequerido2 = materialRequerido2;
    }

    public String getMaterialRequerido3() {
        return materialRequerido3;
    }

    public void setMaterialRequerido3(String materialRequerido3) {
        this.materialRequerido3 = materialRequerido3;
    }

    public int getCantidadMaterialesRequeridos() {
        return (materialRequerido3 == null) ? 2 : 3;
    }

    // Reglas de fabricación y ganancias establecidas en la especificación
    public static tipoAuto crearSedanDeLujo() {
        return new tipoAuto("Sedán de lujo", 5000, "Motor de lujo", "Carrocería", null);
    }

    public static tipoAuto crearPickupAltaGama() {
        return new tipoAuto("Pick-up de alta gama", 12000, "Motor de lujo", "Carrocería", null);
    }

    public static tipoAuto crearMaquinariaPesada() {
        return new tipoAuto("Maquinaria de alta gama para trabajos pesados", 17500,
                "Motor especial deportivo", "Carrocería especial", "Llantas únicas de trabajo");
    }

    public static tipoAuto crearSuperautoDeportivo() {
        return new tipoAuto("Superauto deportivo", 20000,
                "Motor especial deportivo", "Carrocería", "Llantas deportivas");
    }

    public static tipoAuto obtenerPorIndice(int indice) {
        switch (indice) {
            case 1:
                return crearSedanDeLujo();
            case 2:
                return crearPickupAltaGama();
            case 3:
                return crearMaquinariaPesada();
            case 4:
                return crearSuperautoDeportivo();
            default:
                return crearSedanDeLujo();
        }
    }
}

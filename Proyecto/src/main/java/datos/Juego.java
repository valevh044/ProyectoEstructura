/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package datos;

/**
 *
 * @author felinava06
 */
public class Juego {
    private int numeroFabricaActual;
    private Fabrica fabricaActual;
    private boolean juegoTerminado;

    public Juego() {

        numeroFabricaActual = 1;
        juegoTerminado = false;

        fabricaActual = new Fabrica(numeroFabricaActual);
    }

    public boolean avanzarFabrica() {

        if (juegoTerminado) {
            return false;
        }

        if (!fabricaActual.objetivoAlcanzado()) {
            return false;
        }

        if (numeroFabricaActual == 3) {
            juegoTerminado = true;
            return true;
        }

        numeroFabricaActual++;

        fabricaActual = new Fabrica(numeroFabricaActual);

        return true;
    }

    public Fabrica getFabricaActual() {
        return fabricaActual;
    }

    public int getNumeroFabricaActual() {
        return numeroFabricaActual;
    }

    public boolean isJuegoTerminado() {
        return juegoTerminado;
    }

    public boolean haGanado() {
        return juegoTerminado && numeroFabricaActual == 3
                && fabricaActual.objetivoAlcanzado();
    }
}

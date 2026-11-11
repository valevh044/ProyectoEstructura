
package datos;

public class EmpleadoAscenso {

    // Atributos 
    private String nombre;
    private String puesto;
    private boolean jugador;

   
    public EmpleadoAscenso(String nombre, String puesto, boolean jugador) {
        this.nombre = nombre;
        this.puesto = puesto;
        this.jugador = jugador;
    }

    
    public String getNombre() {
        return nombre;
    }

   
    public String getPuesto() {
        return puesto;
    }

   
    public boolean esJugador() {
        return jugador;
    }

    
    public void setPuesto(String puesto) {
        this.puesto = puesto;
    }

   
   
    public String toString() {
        return "Nombre: " + nombre
                + " | Puesto: " + puesto
                + " | Jugador: " + jugador;
    }
}


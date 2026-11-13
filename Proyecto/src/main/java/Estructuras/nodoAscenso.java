
package Estructuras;

import datos.EmpleadoAscenso;

public class nodoAscenso {

    
    private EmpleadoAscenso empleado;
    private nodoAscenso siguiente;

    
    public nodoAscenso(EmpleadoAscenso empleado) {
        this.empleado = empleado;
        this.siguiente = null;
    }

   
    public EmpleadoAscenso getEmpleado() {
        return empleado;
    }

    
    public void setEmpleado(EmpleadoAscenso empleado) {
        this.empleado = empleado;
    }

  
    public nodoAscenso getSiguiente() {
        return siguiente;
    }

   
    public void setSiguiente(nodoAscenso siguiente) {
        this.siguiente = siguiente;
    }
}


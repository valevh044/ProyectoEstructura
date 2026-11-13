
package Estructuras;

import datos.EmpleadoAscenso;

public class nodoAscenso {

    
    private EmpleadoAscenso empleado;
    private NodoAscenso siguiente;

    
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

  
    public NodoAscenso getSiguiente() {
        return siguiente;
    }

   
    public void setSiguiente(NodoAscenso siguiente) {
        this.siguiente = siguiente;
    }
}


/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package modelos;
/**
 *
 * @author GLARA
 */

public class mGrupo {

    private final String nombre;
    private final String id;
    

    public mGrupo(String nombre,String id) {
        this.nombre = nombre;
        this.id = id;
    }

    public String getID() {
        return id;
    }

        
    @Override
    public String toString() {
        return nombre;
    }

}

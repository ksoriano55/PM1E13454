package Models;

public class Contactos {
    private Integer Id;
    private String Pais;
    private String Nombre;
    private String Telefono;
    private String Nota;
    private String Imagen;

    public Contactos(Integer id, String pais, String nombre, String telefono, String nota, String imagen) {
        Id = id;
        Pais = pais;
        Nombre = nombre;
        Telefono = telefono;
        Nota = nota;
        Imagen = imagen;
    }

    public Contactos() {
    }

    public Integer getId() {
        return Id;
    }

    public void setId(Integer id) {
        Id = id;
    }

    public String getPais() {
        return Pais;
    }

    public void setPais(String pais) {
        Pais = pais;
    }

    public String getNombre() {
        return Nombre;
    }

    public void setNombre(String nombre) {
        Nombre = nombre;
    }

    public String getTelefono() {
        return Telefono;
    }

    public void setTelefono(String telefono) {
        Telefono = telefono;
    }

    public String getNota() {
        return Nota;
    }

    public void setNota(String nota) {
        Nota = nota;
    }

    public String getImagen() {
        return Imagen;
    }

    public void setImagen(String imagen) {
        Imagen = imagen;
    }
}

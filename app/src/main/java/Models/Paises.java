package Models;

public class Paises {
    private Integer Id;
    private Integer Codigo;
    private String Nombre;

    public Paises(Integer id, Integer codigo, String nombre) {
        Id = id;
        Codigo = codigo;
        Nombre = nombre;
    }

    public Paises() {
    }

    public Integer getId() {
        return Id;
    }

    public void setId(Integer id) {
        Id = id;
    }

    public Integer getCodigo() {
        return Codigo;
    }

    public void setCodigo(Integer codigo) {
        Codigo = codigo;
    }

    public String getNombre() {
        return Nombre;
    }

    public void setNombre(String nombre) {
        Nombre = nombre;
    }
}

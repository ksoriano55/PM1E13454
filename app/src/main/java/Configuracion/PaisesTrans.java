package Configuracion;

public class PaisesTrans {
    public static final String TablePaises = "paises";

    public static final String id = "id";
    public static final String codigo = "codigo";
    public static final String nombre = "nombre";

    public static final String CreateTablePaises = "Create table "+ TablePaises +" ("+
            "id INTEGER PRIMARY KEY AUTOINCREMENT, codigo TEXT, nombre TEXT)";

    public static final String DropTablePaises = "DROP TABLE IF EXISTS "+ TablePaises;
    public static final String SelectAllPaises = "SELECT * FROM " + TablePaises;
}

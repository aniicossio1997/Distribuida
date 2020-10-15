import java.io.Serializable;

public class Response implements IResponse{
    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private int cantidad;
    private byte[] datos;
    
    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public byte[] getDatos() {
        return datos;
    }

    public void setDatos(byte[] datos) {
        this.datos = datos;
    }

    public Response(int cantidad, byte[] datos) {
        this.cantidad = cantidad;
        this.datos = datos;
    }
    public Response() {
        super();
    }

}

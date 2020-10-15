import java.io.Serializable;

public interface IResponse extends Serializable{
    public int getCantidad();

    public void setCantidad(int cantidad);

    public byte[] getDatos();

    public void setDatos(byte[] datos);
}

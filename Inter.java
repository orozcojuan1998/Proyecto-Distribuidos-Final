package test;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

import modelo.Cliente;
import modelo.Producto;

public interface Inter {

	void leerArchivo(String nombrearchivo);
	boolean validar(String x, ArrayList<Cliente> clientes);
	String bytesToHex(byte[] hash);
	String hashearContra(String value);
	ArrayList<Producto> getProductos();
	ArrayList<Cliente> getClientes();
	public void agregarOp(int index, String operation);
    public void crearTransaccion(int index, List<String> TX_i) throws RemoteException;
	public int get_indexTX() throws RemoteException;
	public void set_indexTX() throws RemoteException;
}

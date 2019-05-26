package Producto;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Map;

import Cuenta.Cuenta;

public interface ProductoRMII extends Remote{
	public String saludar(String x) throws RemoteException;
	public void leerProductos(String x) throws RemoteException;
	public void imprimirProductos() throws RemoteException;
	public ArrayList<Producto> getProductos() throws RemoteException;
	public boolean comprarProductos(Map<Integer, String> carrito) throws RemoteException;
	
	

}

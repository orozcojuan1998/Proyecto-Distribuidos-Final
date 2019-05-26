package Producto;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;

public class ServidorProducto {
	public ServidorProducto() throws RemoteException, MalformedURLException {
		ProductoRMII i = new PImpleRMII();
		LocateRegistry.createRegistry(1900);
		Naming.rebind("rmi://localhost:1900//producto",i);
		System.out.println("Servidor producto corriendo");
	}
	public static void main(String args[]) throws RemoteException, MalformedURLException{
		new ServidorProducto();
	}
}

package Producto;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class ServidorProducto {
	public ServidorProducto() throws RemoteException, MalformedURLException {
		
		ProductoRMII i = new PImpleRMII();
		System.setProperty("java.security.policy", "./cliente.policy");
		
		 
		try {
			Registry registry = LocateRegistry.createRegistry(3002);
			registry.rebind("//127.0.0.1/Productos", i);
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
	}
	}
	public static void main(String args[]) throws RemoteException, MalformedURLException{
		new ServidorProducto();
	}
}

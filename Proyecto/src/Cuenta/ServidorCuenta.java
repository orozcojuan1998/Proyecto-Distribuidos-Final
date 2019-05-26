package Cuenta;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class ServidorCuenta {
	public ServidorCuenta() throws RemoteException, MalformedURLException, NotBoundException {
		CuentaRMII i = new CImpleRMII();
		System.setProperty("java.security.policy", "./cliente.policy");
		
		 
			try {
				Registry registry = LocateRegistry.createRegistry(3001);
				registry.rebind("//127.0.0.1/Cuentas", i);
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
		}
	}
	public static void main(String args[]) throws RemoteException, MalformedURLException, NotBoundException{
		new ServidorCuenta();
	}
}

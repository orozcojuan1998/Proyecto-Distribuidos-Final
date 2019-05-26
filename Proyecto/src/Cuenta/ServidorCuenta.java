package Cuenta;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;

public class ServidorCuenta {
	public ServidorCuenta() throws RemoteException, MalformedURLException {
		CuentaRMII i = new CImpleRMII();
		LocateRegistry.createRegistry(5000);
		Naming.rebind("rmi://localhost:5000//cuenta",i);
		System.out.println("Servidor cuenta corriendo");
	}
	public static void main(String args[]) throws RemoteException, MalformedURLException{
		new ServidorCuenta();
	}
}

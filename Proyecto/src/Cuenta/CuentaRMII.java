package Cuenta;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

public interface CuentaRMII extends Remote{
	public String saludar(String x) throws RemoteException;
	public void leerCuentas(String x) throws RemoteException;
	public void imprimirCuentas() throws RemoteException;
	public ArrayList<Cuenta> getCuentas() throws RemoteException;
	public boolean autenticarUsuario(String u,String c,String t)throws RemoteException;
	public int get_indexTX() throws RemoteException;
    public void add_operation(int index, String operation) throws RemoteException;
    public void set_indexTX() throws RemoteException;
    public void create_transaction(int index, List<String> TX_i) throws RemoteException;

}

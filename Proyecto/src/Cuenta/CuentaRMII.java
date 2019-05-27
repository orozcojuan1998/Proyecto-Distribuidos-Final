package Cuenta;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

import Cliente.ProductoCarrito;
import Cliente.Transaccion;

public interface CuentaRMII extends Remote{
	public String saludar(String x) throws RemoteException;
	public void leerCuentas(String x) throws RemoteException;
	public void imprimirCuentas() throws RemoteException;
	public ArrayList<Cuenta> getCuentas() throws RemoteException;
	public boolean autenticarUsuario(String c,String t)throws RemoteException;
	public int get_indexTX() throws RemoteException;
    public void add_operation(int index, String operation) throws RemoteException;
    public void set_indexTX() throws RemoteException;
    public boolean verificarRegistro(String tarjeta) throws RemoteException;
	public Cuenta getCuenta(String tarjeta)throws RemoteException;
	public float getSaldo(String tarjeta)throws RemoteException;
	public void setSaldo(String tarjeta, float f)throws RemoteException;
	public void setContrasena(String tarjeta,String contra)throws RemoteException;
	public Transaccion iniciarTransaccion(Transaccion tv) throws RemoteException;
	public void finalizarTransaccion(Transaccion tv)throws RemoteException;
	public Transaccion solicitarTransaccion()throws RemoteException;
    
   
    

}

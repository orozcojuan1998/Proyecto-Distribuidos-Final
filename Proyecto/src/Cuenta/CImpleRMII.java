package Cuenta;

import java.io.BufferedReader;
import java.io.FileReader;
import java.nio.charset.StandardCharsets;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import Cliente.Cliente;
import Cliente.Participante;
import Cliente.ProductoCarrito;
import Cliente.Transaccion;
import Coordinador.CoordinadorInterface;
import Producto.Producto;
import Cliente.Transaccion;

public class CImpleRMII extends UnicastRemoteObject implements CuentaRMII {

	private ArrayList<Transaccion> transaccionesActivas;
	private int portCoordinador = 3000;
	private CoordinadorInterface coordinador;
	private int numSecuencia = 0;
	
	public CImpleRMII() throws RemoteException, NotBoundException {
		super();
		// TODO Auto-generated constructor stub
		indexTX_user = 0;
		transaccionesActivas = new ArrayList<Transaccion>();
		System.setProperty("java.security.policy", "./cliente.policy");
		//Registry registry = LocateRegistry.getRegistry(portCoordinador);


		//coordinador = (CoordinadorInterface) registry.lookup("//127.0.0.1/Coordinador");

		
		
		leerCuentas("iniciocuentas.txt");
		imprimirCuentas();
	}

	ArrayList<Cuenta> cuentas = new ArrayList<Cuenta>(); 
	private static final long serialVersionUID = 1L;
    Map<String, String> card_value = new HashMap<String, String>();
    List<List<String>> TX = new ArrayList<List<String>>();
    
    
    
    
    int indexTX_user ;

	@Override
	public String saludar(String x) throws RemoteException {
		
		
		
		// TODO Auto-generated method stub
		return "Hola "+x+" desde cuenta";
	}
	
	public void leerCuentas(String x) throws RemoteException{
	try {
			FileReader fr = new FileReader(x);
			BufferedReader br = new BufferedReader(fr);
			String linea;
			linea = br.readLine();
			linea = br.readLine();
			int nCuentas=Integer.parseInt(linea);
			for(int i=0;i<nCuentas;i++) {
				linea = br.readLine();
				String[] parts = linea.split(",");
				MessageDigest digest = MessageDigest.getInstance("SHA-256");
	            byte[] encodedhash = digest.digest(parts[0].getBytes(StandardCharsets.UTF_8));
	            String hash = bytesToHex(encodedhash);
	            digest = MessageDigest.getInstance("SHA-256");
	            encodedhash = digest.digest(parts[3].getBytes(StandardCharsets.UTF_8));
	            String hash2 = bytesToHex(encodedhash);
				Cuenta cuenta= new Cuenta(parts[2],hash2,hash, Float.parseFloat(parts[1]));
				this.cuentas.add(cuenta);
			}
			imprimirCuentas();
			br.close();
		}catch(Exception e) {
			System.out.println(e);
		}	
	}

	private static String bytesToHex(byte[] hash) {
		  StringBuffer hexString = new StringBuffer();
        for (int i = 0; i < hash.length; i++) {
            String hex = Integer.toHexString(0xff & hash[i]);
            if(hex.length() == 1) hexString.append('0');
                hexString.append(hex);
        }
        return hexString.toString();
    }

	@Override
	public void imprimirCuentas() throws RemoteException {
		// TODO Auto-generated method stub
		for (Cuenta cuenta : cuentas) {
			System.out.println(cuenta.toString());
		}
	}

	@Override
	public ArrayList<Cuenta> getCuentas() throws RemoteException {
		return this.cuentas;
	}

	@Override
	public boolean autenticarUsuario(String u, String c,String t) throws RemoteException {
		for (Cuenta cuenta : cuentas) {
			MessageDigest digest;
			try {
				digest = MessageDigest.getInstance("SHA-256");
            byte[] encodedhash = digest.digest(c.getBytes(StandardCharsets.UTF_8));
            String hash = bytesToHex(encodedhash);
            MessageDigest digest2 = MessageDigest.getInstance("SHA-256");
            byte[] encodedhash2 = digest2.digest(t.getBytes(StandardCharsets.UTF_8));
            String hash2 = bytesToHex(encodedhash2);
			System.out.println(hash);
			System.out.println(hash2);
			System.out.println(u);

            if(cuenta.getContrasena().equals(hash)&&cuenta.getUsuario().equals(u)&&cuenta.getTarjeta().equals(hash2)) {
				return true;
			}
			} catch (NoSuchAlgorithmException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
            
		}
		return false;
	}

	@Override
    public int get_indexTX() throws RemoteException {                
		return indexTX_user; 
	}
    
    @Override
    public void set_indexTX() 	throws RemoteException {                
    	indexTX_user++; 
    }
   
    @Override
    public void add_operation(int index, String operation){
        TX.get(index).add(operation);
    }
    
    public boolean validarForward(Transaccion tv) throws RemoteException{
    	boolean valida = true;
    	
    	for(int i= 0; i < transaccionesActivas.size(); i++){
    		List<Object> conjuntoEscrituraTV = tv.getConjuntoEscritura();
    		List<Object> conjuntoLecturaActual = transaccionesActivas.get(i).getConjuntoLectura();
    		
    		for (Object cuentaTV : conjuntoEscrituraTV) {
				for (Object cuentaA : conjuntoLecturaActual) {
					Cuenta cuentaT = (Cuenta) cuentaTV;
					Cuenta cuentaAA = (Cuenta) cuentaA;
					if(cuentaAA.getTarjeta().equals(cuentaT.getTarjeta())){
						return false;
					}
				}
			}
    		
    		
    	}
    	
    	return valida;
    }

	@Override
	public synchronized Cuenta getCuenta(String usuario)throws RemoteException  {
		for (Cuenta cuenta : cuentas) {
			if(usuario.equals(cuenta.getUsuario())){
				return cuenta;
			}
		}
		return null;
	}

	@Override
	public float getSaldo(String usuario) throws RemoteException {
		// TODO Auto-generated method stub
		for (Cuenta cuenta : cuentas) {
			if(usuario.equals(cuenta.getUsuario())){
				return cuenta.getSaldo();
			}
		}
		return 0;
	}

	@Override
	public void setSaldo(String usuario, float f) throws RemoteException {
		for (Cuenta cuenta : cuentas) {
			if(usuario.equals(cuenta.getUsuario())){
				cuenta.setSaldo(f);
			}
		}
	
		
	}

	@Override
	public Transaccion iniciarTransaccion(Transaccion tv) throws RemoteException {
		tv.setNumTransaccion(numSecuencia++);
		tv.setEstado(1);
		System.out.println("estado transacción: "+tv.getEstado());
		if(validarForward(tv)){
			tv.setEstado(2);
			transaccionesActivas.add(tv);
		}else{
			tv.setEstado(3);
		}
		System.out.println("estado transacción: "+tv.getEstado());
		return tv;
	}



	@Override
	public void finalizarTransaccion(Transaccion tv) throws RemoteException {
		for (int i =0 ; i < transaccionesActivas.size(); i++) {
			if(tv.getNumTransaccion()==transaccionesActivas.get(i).getNumTransaccion()){
				transaccionesActivas.remove(i);
				tv.setEstado(4);
			}
		}
	
		System.out.println("estado transacción: "+tv.getEstado()+ " no. "+ tv.getNumTransaccion());
	}

	@Override
	public Transaccion solicitarTransaccion() throws RemoteException {
		return new Transaccion();
	}

	


	

	
    
}

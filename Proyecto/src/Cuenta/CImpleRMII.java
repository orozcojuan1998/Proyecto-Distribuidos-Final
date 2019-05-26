package Cuenta;

import java.io.BufferedReader;
import java.io.FileReader;
import java.nio.charset.StandardCharsets;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CImpleRMII extends UnicastRemoteObject implements CuentaRMII {

	
	
	public CImpleRMII() throws RemoteException {
		super();
		// TODO Auto-generated constructor stub
	}

	ArrayList<Cuenta> cuentas = new ArrayList<Cuenta>(); 
	private static final long serialVersionUID = 1L;
    Map<String, String> card_value = new HashMap<String, String>();
    List<List<String>> TX = new ArrayList<List<String>>();
    int indexTX_user = 0;

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
    public void create_transaction(int index, List<String> TX_i){ 
        TX.add(index, TX_i);
    }
    @Override
    public void add_operation(int index, String operation){
        TX.get(index).add(operation);
    }
}

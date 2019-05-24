package test;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.rmi.RemoteException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import modelo.Cliente;
import modelo.Producto;

public class InterImple implements Inter{

	private ArrayList<String> hashs = new ArrayList<String>();
	private ArrayList<Cliente> clientes = new ArrayList<Cliente>();
	private ArrayList<Producto> productos = new ArrayList<Producto>();
	private int indexTX_user = 0;
	private List<List<String>> TX = new ArrayList<List<String>>();
	@Override
	public void leerArchivo(String nombrearchivo) {
		// TODO Auto-generated method stub
		String linea;
		try {
			FileReader fr = new FileReader(nombrearchivo);
			BufferedReader br = new BufferedReader(fr);
			linea = br.readLine();
			linea = br.readLine();
			int nProductos=Integer.parseInt(linea);
			for(int i=0;i<nProductos;i++) {
				linea = br.readLine();
				String[] parts = linea.split(",");
				Producto producto= new Producto(parts[0], Float.parseFloat(parts[1]),Integer.parseInt(parts[2]));
				this.productos.add(producto);
			}
			linea = br.readLine();
			linea = br.readLine();
			int nTarjetas=Integer.parseInt(linea);
			for(int i=0;i<nTarjetas;i++) {
				linea = br.readLine();
				String[] parts = linea.split(",");
				Cliente cliente= new Cliente(hashearContra(parts[0]), Float.parseFloat(parts[1]));
				this.clientes.add(cliente);
			}

		}catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public boolean validar(String value, ArrayList<Cliente> clientes) {
		for (Cliente cliente : clientes) {
			if(cliente.tarjeta.equals(hashearContra(value))) {
				return true;
			}
		}
		return false;
	}

	@Override
	public String hashearContra(String value) {
		MessageDigest digest;
		try {
			digest = MessageDigest.getInstance("SHA-256");
			 byte[] encodedhash = digest.digest(value.getBytes(StandardCharsets.UTF_8));
		      String hash = bytesToHex(encodedhash);
		      return hash;
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "";
	}
	
	   public void agregarOp(int index, String operation){
           
           TX.get(index).add(operation);
           
       }
	
	@Override
	public String bytesToHex(byte[] hash) {
		  StringBuffer hexString = new StringBuffer();
		    for (int i = 0; i < hash.length; i++) {
		        String hex = Integer.toHexString(0xff & hash[i]);
		        if(hex.length() == 1) hexString.append('0');
		            hexString.append(hex);
		    }
		    return hexString.toString();
	}

	public ArrayList<String> getHashs() {
		return hashs;
	}

	public void setHashs(ArrayList<String> hashs) {
		this.hashs = hashs;
	}

	public ArrayList<Cliente> getClientes() {
		return clientes;
	}

	public void setClientes(ArrayList<Cliente> clientes) {
		this.clientes = clientes;
	}

	public ArrayList<Producto> getProductos() {
		return productos;
	}

	public void setProductos(ArrayList<Producto> productos) {
		this.productos = productos;
	}

	public void setIndexTX_user(int indexTX_user) {
		this.indexTX_user = indexTX_user;
	}

	@Override
	public int get_indexTX() throws RemoteException {
		return indexTX_user;
	}

	public void set_indexTX() throws RemoteException {
		this.indexTX_user++;
	}
	
	 public void crearTransaccion(int index, List<String> TX_i){
         TX.add(index, TX_i);
     }
}

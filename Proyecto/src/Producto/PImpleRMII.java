package Producto;

import java.io.BufferedReader;
import java.io.FileReader;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;


public class PImpleRMII extends UnicastRemoteObject implements ProductoRMII {

	public PImpleRMII() throws RemoteException {
		super();
		// TODO Auto-generated constructor stub
	}

	private static final long serialVersionUID = 1L;
	ArrayList<Producto> productos = new ArrayList<Producto>(); 

	@Override
	public String saludar(String x) throws RemoteException {
		// TODO Auto-generated method stub
		return "Hola "+x+" desde producto";
	}

	@Override
	public void leerProductos(String x) throws RemoteException {
		// TODO Auto-generated method stub
		String linea;
		System.out.println("Hola");
		try {
			FileReader fr = new FileReader(x);
			BufferedReader br = new BufferedReader(fr);
			linea = br.readLine();
			linea = br.readLine();
			int nProductos=Integer.parseInt(linea);
			for(int i=0;i<nProductos;i++) {
				linea = br.readLine();
				String[] parts = linea.split(",");
				Producto producto= new Producto(parts[0], Integer.parseInt(parts[2]),Float.parseFloat(parts[1]));		
				this.productos.add(producto);
			}
			br.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void imprimirProductos() throws RemoteException {
		// TODO Auto-generated method stub
		for (Producto producto : this.productos) {
			System.out.println(producto.toString());
		}
	}

	@Override
	public ArrayList<Producto> getProductos() throws RemoteException {
		return this.productos;
	} 

	
	
}

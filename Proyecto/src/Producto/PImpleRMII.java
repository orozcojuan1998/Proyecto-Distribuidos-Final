package Producto;

import java.io.BufferedReader;
import java.io.FileReader;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Map;




public class PImpleRMII extends UnicastRemoteObject implements ProductoRMII {

	public PImpleRMII() throws RemoteException {
		super();
		// TODO Auto-generated constructor stub
		leerProductos("inicioproductos.txt");
		imprimirProductos();	
	}

	private static final long serialVersionUID = 1L;
	private ArrayList<Producto> productos = new ArrayList<Producto>(); 

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
				Producto producto= new Producto(Integer.parseInt(parts[0]),parts[1], Integer.parseInt(parts[2]),Float.parseFloat(parts[3]));		
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
		ArrayList<Producto> productosTemporales = new ArrayList<>();
		for (Producto producto : productos) {
			productosTemporales.add(new Producto(producto.getID(), producto.getNombre(), producto.getCantidadDisponible(), producto.getPrecio()));
		}
		
		return productosTemporales;
	}
	
	@Override
	public boolean comprarProductos(Map<Integer, String> carrito) throws RemoteException {
	
			for(int i = 1; i <= productos.size(); i++){			
				if(carrito.get(i)!=null){
					if(productos.get(i-1).getCantidadDisponible()-Integer.parseInt(carrito.get(i))<0){
						throw new RemoteException("Se agotaron existencias");
					}
					productos.get(i-1).setCantidadDisponible(productos.get(i-1).getCantidadDisponible()-Integer.parseInt(carrito.get(i)));
				}		
			}
		for(int i = 0; i < productos.size(); i++){
			Producto producto = productos.get(i);
			System.out.println(i+1+" Cantidad despues de comprar "+producto.getCantidadDisponible());
		}
		
		return true;
	}

	
	
}

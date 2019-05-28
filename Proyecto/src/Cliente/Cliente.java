package Cliente;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import javax.swing.JOptionPane;
import javax.swing.JPasswordField;

import Coordinador.CoordinadorInterface;
import Coordinador.TransaccionCoordinador;
import Cuenta.Cuenta;
import Cuenta.CuentaRMII;
import Producto.Producto;
import Producto.ProductoRMII;

public class Cliente extends UnicastRemoteObject{

	private ArrayList<Cuenta> cuentas = new ArrayList<Cuenta>(); 
	private ArrayList<Producto> productos = new ArrayList<Producto>(); 
	private ArrayList<ProductoCarrito> carrito = new ArrayList<ProductoCarrito>();
	private CoordinadorInterface coordinador;
	private int portCliente =3003;
	private int portCoordinador = 3000;
	private static int portCuentas = 3001;
	private static int portProductos = 3002;
	private static CuentaRMII j;
	private static ProductoRMII i;

	protected Cliente() throws RemoteException, NotBoundException {
		super();

		iniciarSesion();

	}
	private void iniciarSesion() throws RemoteException {
		// TODO Auto-generated method stub
		
		String value = "0",tarjeta,contrasena,usuario, tarj, saldo;
		while(!value.equals("3")) {
			System.out.println("Seleccione una opción: ");
			System.out.println("1. Ingresar con cuenta existente");
			System.out.println("2. Ingresar por primera vez");
			System.out.println("3. Salir");
			value = JOptionPane.showInputDialog("Seleccione una opción");

			switch(value) {
			case "1":{
				tarjeta = JOptionPane.showInputDialog("Escribe tu tarjeta");
				 contrasena = JOptionPane.showInputDialog(null, "Contraseña" ,JOptionPane.OK_CANCEL_OPTION);
				if(tarjeta.equals("0000")&&contrasena.equals("0000")) {//ADMIN
					System.out.println(" Seleccione una opción");
					System.out.println("1. Agregar saldo");
					System.out.println("2. Agregar producto");
					System.out.println("3. Salir");

					String opcion = JOptionPane.showInputDialog("Seleccione una opción");
					switch(opcion) {
					case "1":{
						System.out.println("Ingrese la tarjeta que va a recargar");
						tarj = JOptionPane.showInputDialog("Seleccione una opción");
						System.out.println("Ingrese el saldo que va a recargar");
						saldo = JOptionPane.showInputDialog("Seleccione una opción");
						recargarTarjeta(tarj,saldo);
						break;
					}case "2":{
						String nombre,cantidad,precio;
						nombre = JOptionPane.showInputDialog("Ingrese el nombre del producto");
						cantidad = JOptionPane.showInputDialog("Ingrese la cantidad disponible");
						System.out.println("Ingrese el precio del producto");
						precio = JOptionPane.showInputDialog("Ingrese el precio del producto");
						productos = i.getProductos();
						agregarProducto(nombre,cantidad,precio);
						break;
					}
					case "3":{
						break;
					}
					}
				}
				else if(j.autenticarUsuario(contrasena,tarjeta)){
					Cuenta cuenta = new Cuenta();
					cuenta=j.getCuenta(tarjeta);
					System.out.println("Ingresó "+cuenta.getUsuario()+" con saldo: "+cuenta.getSaldo());
					JOptionPane.showMessageDialog(null, "Ha ingresado correctamente");
					carrito = new ArrayList<>();
					productos = i.getProductos();
					System.out.println("CATÁLOGO DE PRODUCTOS");
					System.out.println("Numero     Item        Precio        Disponibles");
					int k=0;
					for (Producto producto : productos) {
						System.out.println(k+"  "+producto.toString());
						k++;
					}

					Boolean nextItem = true;
					while(nextItem){

						String itemS = JOptionPane.showInputDialog("Ingrese el número del producto para agregarlo al carrito de compras...");
						int item = Integer.parseInt(itemS);

						if(!(productos.get(item).getCantidadDisponible()==0)){
							boolean addItem = true;
							while(addItem){

								String numS = JOptionPane.showInputDialog("Ingrese la cantidad");
								int num = Integer.parseInt(numS);

								if(num <= productos.get(item).getCantidadDisponible()){
									addItem = false;
									carrito.add(new ProductoCarrito(productos.get(item), num));
									productos.get(item).setCantidadDisponible(productos.get(item).getCantidadDisponible()-num);
									String next = JOptionPane.showInputDialog(null, "¿Desea agregar más productos? 1.Sí  2. No" ,"Producto agreado al carrito", JOptionPane.QUESTION_MESSAGE);

									if(next.equals("2")){
										nextItem = false;
									}
								}else{
									JOptionPane.showMessageDialog(null, "Error", "Ingrese una cantidad menor a " + productos.get(item).getCantidadDisponible(), JOptionPane.WARNING_MESSAGE);

								}


							}
						}else{
							JOptionPane.showMessageDialog(null, "Producto agotado", "Producto agotado", JOptionPane.WARNING_MESSAGE);
						}
					}
					transaccionDeCompra(tarjeta);
				}else {
					JOptionPane.showMessageDialog(null, "Error", "Información invalida", JOptionPane.WARNING_MESSAGE);

				}
				break;
			}
			case "2":{
				tarjeta = JOptionPane.showInputDialog("Escribe tu tarjeta");
				if(j.verificarRegistro(tarjeta)) {
					String contra = JOptionPane.showInputDialog("Escribe tu contraseña");
					j.setContrasena(tarjeta,contra);
					 JOptionPane.showMessageDialog(null, "Se ha registrado correctamente");
				}else {
					JOptionPane.showMessageDialog(null, "Error", "Información invalida", JOptionPane.ERROR_MESSAGE);
				}
				break;
			}
			}
		}

	}
	private void recargarTarjeta(String tarj, String saldo) throws RemoteException {
		Transaccion tvRecarga = j.solicitarTransaccion();
		Cuenta cuenta = j.getCuenta(tarj);
		tvRecarga.adicionarObjetoEscritura(j.getCuenta(tarj));
		tvRecarga.adicionarObjetoLectura(j.getCuenta(tarj));
		tvRecarga = j.iniciarTransaccion(tvRecarga);
		if(tvRecarga.getEstado()==2) {
			j.recargarTarjeta(tarj,saldo);
		}else {
			JOptionPane.showMessageDialog(null, "Error", "Transacción abortada", JOptionPane.ERROR_MESSAGE);
			j.finalizarTransaccion(tvRecarga);
		}
	}
	private void agregarProducto(String nombre, String cantidadD, String precio) throws RemoteException {
		Transaccion tvAgregar = j.solicitarTransaccion();
		this.productos=i.getProductos();
		tvAgregar.adicionarObjetoEscritura(productos.get(productos.size()-1));
		tvAgregar = j.iniciarTransaccion(tvAgregar);
		if(tvAgregar.getEstado()==2) {
			int id = productos.size();
			Producto p = new Producto(id, nombre, Integer.parseInt(cantidadD), Float.parseFloat(precio));
			
		}else {
			JOptionPane.showMessageDialog(null, "Error", "Transacción abortada", JOptionPane.ERROR_MESSAGE);
			j.finalizarTransaccion(tvAgregar);
		}
	}
	private void transaccionDeCompra(String tarjeta) throws RemoteException {


		float total = 0;
		boolean saldo = true;

		Transaccion tvCuenta = j.solicitarTransaccion();
		for (ProductoCarrito productoCarrito : carrito) {
			total+= productoCarrito.getP().getPrecio()*productoCarrito.getCantidad();
		}
		tvCuenta.adicionarObjetoEscritura(j.getCuenta(tarjeta));
		tvCuenta.adicionarObjetoLectura(j.getCuenta(tarjeta));
		tvCuenta = j.iniciarTransaccion(tvCuenta);
		Cuenta cuenta = j.getCuenta(tarjeta);
		
		
		if(cuenta.getSaldo()>=total&&tvCuenta.getEstado()!=3){
			
			
			
			Transaccion tvProductosALeer = i.solicitarTransaccion();
			for (ProductoCarrito productoCarrito : carrito) {
				tvProductosALeer.adicionarObjetoLectura(productoCarrito.getP());
			}
			tvProductosALeer = i.iniciarTransaccion(tvProductosALeer);
			
			if(tvProductosALeer.getEstado()==2){
				ArrayList<ProductoCarrito> productosComprados = new ArrayList<ProductoCarrito>();
				for (ProductoCarrito productoCarrito : carrito) {
					if(i.getProducto(productoCarrito.getP().getID()).getCantidadDisponible()>=productoCarrito.getCantidad()){
						productosComprados.add(productoCarrito);
					}else{
						JOptionPane.showMessageDialog(null, "Error", "El producto "+productoCarrito.getP().getNombre()+" se ha agotado", JOptionPane.ERROR_MESSAGE);

					}
				}
			
				i.finalizarTransaccion(tvProductosALeer);
				Transaccion tvProductosAComprar = i.solicitarTransaccion();
				for (ProductoCarrito productoCarrito : productosComprados) {
					tvProductosAComprar.adicionarObjetoEscritura(productoCarrito.getP());
				}
				tvProductosAComprar = i.iniciarTransaccion(tvProductosAComprar);
				if(tvProductosAComprar.getEstado()==2){
					total = 0;
					for (ProductoCarrito productoCarrito : productosComprados) {
						i.disminuirCantidadDisponible(productoCarrito.getP().getID(), productoCarrito.getCantidad());
						total+=productoCarrito.getP().getPrecio()*productoCarrito.getCantidad();
					}
					i.finalizarTransaccion(tvProductosAComprar);
					JOptionPane.showMessageDialog(null, "Su saldo: "+ (cuenta.getSaldo()-total));
					j.setSaldo(tarjeta, cuenta.getSaldo()-total);
					j.finalizarTransaccion(tvCuenta);
					
					
				}else{
					JOptionPane.showMessageDialog(null, "Error", "Transacción abortada (COMPRA "+tvProductosAComprar.getEstado() +") ... intente de nuevo ", JOptionPane.ERROR_MESSAGE);
					i.finalizarTransaccion(tvProductosAComprar);
					j.finalizarTransaccion(tvCuenta);
				}
				
				
			}else{
				JOptionPane.showMessageDialog(null, "Error", "Transacción abortada (LECTURA-COMPRA) ... intente de nuevo", JOptionPane.ERROR_MESSAGE);
				i.finalizarTransaccion(tvProductosALeer);
				j.finalizarTransaccion(tvCuenta);
			}
			
			
			
		}else if(tvCuenta.getEstado()==3){
			JOptionPane.showMessageDialog(null, "Error", "Transacción abortada (CUENTA)... intente de nuevo", JOptionPane.ERROR_MESSAGE);
			j.finalizarTransaccion(tvCuenta);
		}else{
			JOptionPane.showMessageDialog(null, "Error", "Saldo insuficienta", JOptionPane.ERROR_MESSAGE);
			j.finalizarTransaccion(tvCuenta);
		}



	}
	public static void main(String[] args) throws MalformedURLException, RemoteException, NotBoundException {


		System.setProperty("java.security.policy", "./cliente.policy");
		


		//coordinador = (CoordinadorInterface) registry.lookup("//127.0.0.1/Coordinador");

		j = null;
		try {
			//Registry registry = LocateRegistry.getRegistry(portCuentas);
			j = (CuentaRMII) Naming.lookup("rmi://"+"localhost:3001"+"/Cuentas");
			//j = (CuentaRMII) registry.lookup("10.192.101.31/Cuentas");
		}  catch (NotBoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}



		i = null;
		try {
			//Registry registry = LocateRegistry.getRegistry(portProductos);
			i = (ProductoRMII) Naming.lookup("rmi://"+"localhost:3002"+"/Productos");
			//i = (ProductoRMII) registry.lookup("10.192.101.31/Productos");
		}  catch (NotBoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Cliente c = new Cliente();
	}
	public static void imprimirProductos(ArrayList<Producto> productos) {
		int i=1;
		for (Producto producto : productos) {
			System.out.println(i+"  "+producto.toString());
			i++;
		}
	}



}

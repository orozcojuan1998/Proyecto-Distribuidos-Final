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






		/*
		String value,contrasena,usuario,tarjeta; 
        Scanner input = new Scanner(System.in);
        while(true) {
	        System.out.println("Seleccione una opción por favor: ");
	        System.out.println("1. Ingresar");
	        System.out.println("2. Salir");
	        value = input.next();
	        switch(value) {
	            case "1":{
		            	System.out.println("Ingrese su usuario por favor");
		            	  usuario = input.next();
		            	  System.out.println("Ingrese su contraseña por favor");
		            	  contrasena = input.next();
		            	  System.out.println("Ingrese su tarjeta por favor");
		            	  tarjeta = input.next();
		            	  if(j.autenticarUsuario(usuario, contrasena,tarjeta)) {
		            		  System.out.println("Ha ingresado correctamente");
		                      Map<String, String> card_value = new HashMap<String, String>();
		                      int indexTX;
		                      Map<Integer, String> carrito = new HashMap<Integer, String>();
		                      List<String> TX_i = new ArrayList<String>();
		                      String operation;
		                      System.out.println("1. Ver catalogo : ");
		                      System.out.println("2. Recargar tarjeta : ");
		                          value = input.next();
		                              if(value.equals("1")){
		                                  indexTX = j.get_indexTX();
		                                  j.set_indexTX();
		                                  j.create_transaction(indexTX, TX_i);
		    		            		  productos=i.getProductos();
		    		            		  System.out.println("CATÁLOGO DE PRODUCTOS");
		    		                      System.out.println("Numero     Item        Precio        Disponibles");
		    		                      int k=1;
			    		              		for (Producto producto : productos) {
			    		              			System.out.println(k+"  "+producto.toString());
			    		              			k++;
			    		              		}
			    		              		Boolean nextItem = true;
			    	                        while(nextItem){
			    	                            System.out.println("Ingrese el número del producto para agregarlo al carrito de compras...");
			    	                            int item = input.nextInt();
			    	                            String s_item = Integer.toString(item);
			    	                            operation = "W," + s_item;
			    	                            j.add_operation(indexTX, operation);
			    	                            System.out.println("index->" + indexTX + "operation->" + operation);
			    	                            Boolean addItem = true;
			    	                            while (addItem){
			    	                                System.out.println("¿Cuántos desea?..."); //valida si hay suficientes disponibles
			    	                                String num = input.next();
			    	                                if(Integer.parseInt(num) <= productos.get(item-1).getCantidadDisponible()){
			    	                                    carrito.put(item, num);
			    	                                    addItem = false;
			    	                                    productos.get(item-1).setCantidadDisponible(productos.get(item-1).getCantidadDisponible()-Integer.parseInt(num));
			    	                                    System.out.println("Producto agregado exitosamente");
			    	                                    System.out.println("¿Desea agregar más productos? 1. Si  2. No");
			    	                                    String next = input.next();
			    	                                    if(next.equals("2")){
			    	                                        nextItem = false;
			    	                                    }else {
			    	                                    	 System.out.println("CATÁLOGO DE PRODUCTOS");
			    			    		                      System.out.println("Numero     Item        Precio        Disponibles");
			    			    		                      k=1;
			    				    		              		for (Producto producto : productos) {
			    				    		              			System.out.println(k+"  "+producto.toString());
			    				    		              			k++;
			    				    		              		}
			    	                                    }
			    	                                }
			    	                                else{
			    	                                    System.out.println("No hay suficientes items disponibles, ingrese una cantidad inferior...");
			    	                                } 
			    	                            }


			    	                            }
			    	                        	while(true){
		    	                                // Imprimir map
		    	                                System.out.println("Su carrito de compras es:");
		    	                                Iterator it = carrito.keySet().iterator();
		    	                                System.out.println("Número     Item        Precio        Cantidad");
		    	                                while(it.hasNext()){
		    	                                  Integer key = (Integer) it.next();
		    	                                  System.out.println(key +"\t  " + productos.get(key-1).getNombre() +"\t  " + productos.get(key-1).getPrecio() +"\t  " + "\t  " + carrito.get(key));
		    	                                }

		    	                                System.out.println("¿Desea modificar un producto del carrito? \n 1. Si, eliminar  \n 2. Si, cambiar cantidad \n 3. No, finalizar compra");
		    	                                String fix = input.next();

		    	                                if(fix.equals("1")){

		    	                                    System.out.println("Ingrese el número del producto");
		    	                                    Integer item_del = input.nextInt();
		    	                                    carrito.remove(item_del);
		    	                                    System.out.println("Elemento eliminado del carrito exitosamente");
		    	                                }

		    	                                if(fix.equals("2")){

		    	                                    System.out.println("Ingrese el numero del item");
		    	                                    Integer item_set = input.nextInt();
		    	                                    System.out.println("La cantidad actual es -> " + carrito.get(item_set));              
		    	                                    Boolean fixQuantity = true;
		    	                                    while (fixQuantity){
		    	                                        System.out.println("Ingrese la nueva cantidad");
		    	                                        String new_quantity = input.next();
		    	                                        if(Integer.parseInt(new_quantity) <= productos.get(item_set-1).getCantidadDisponible()) {
		    	                                            if(carrito.replace(item_set, carrito.get(item_set), new_quantity) == true){
		    	                                                System.out.println("Cantidad modificada exitosamente");
		    	                                                fixQuantity = false;
		    	                                            }
		    	                                            else{
		    	                                                System.out.println("Error, no se pudo hacer el cambio");
		    	                                            }
		    	                                        }
		    	                                        else{
		    	                                            System.out.println("No hay suficientes items disponibles, ingrese una cantidad inferior...");
		    	                                        } 
		    	                                    }
		    	                                }
		    	                                if(fix.equals("3")){

		    	                                }

			    	                       }
		                             }
	            	}else {
		            	  	System.out.println("Datos inválidos");
	            	  }
	        	}
        	}

	    }
		 */
	}
	private void iniciarSesion() throws RemoteException {
		// TODO Auto-generated method stub
		String value = "0",contrasena,usuario,tarjeta; 
		Scanner input = new Scanner(System.in);
		while(!value.equals("2")) {
			System.out.println("Seleccione una opción: ");
			System.out.println("1. Ingresar");
			System.out.println("2. Salir");
			value = input.next();
			switch(value) {
			case "1":{
				System.out.println("Ingrese su usuario");
				usuario = input.next();
				System.out.println("Ingrese su contraseña");
				contrasena = input.next();
				System.out.println("Ingrese su tarjeta");
				tarjeta = input.next();
				if(j.autenticarUsuario(usuario, contrasena,tarjeta)){
					System.out.println("Ha ingresado correctamente");
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

						System.out.println("Ingrese el número del producto para agregarlo al carrito de compras...");
						int item = input.nextInt();

						if(!(productos.get(item).getCantidadDisponible()==0)){
							boolean addItem = true;
							while(addItem){

								System.out.println("Ingrese la cantidad"); 
								int num = input.nextInt();

								if(num <= productos.get(item).getCantidadDisponible()){
									addItem = false;
									carrito.add(new ProductoCarrito(productos.get(item), num));
									productos.get(item).setCantidadDisponible(productos.get(item).getCantidadDisponible()-num);
									System.out.println("Producto agregado al carrito");
									System.out.println("¿Desea agregar más productos? 1.Sí  2. No");
									String next = input.next();
									if(next.equals("2")){
										nextItem = false;
									}
								}else{
									System.out.println("Ingrese una cantidad menor o igual a: "+productos.get(item).getCantidadDisponible());
								}


							}
						}else{
							System.out.println("Producto agotado");
						}
					}
					transaccionDeCompra(usuario);
				}
			}
			case "2":{
				break;
			}
			}
		}

	}
	private void transaccionDeCompra(String usuario) throws RemoteException {


		float total = 0;
		boolean saldo = true;

		Transaccion tv = j.solicitarTransaccion();
		for (ProductoCarrito productoCarrito : carrito) {
			tv.adicionarObjetoEscritura(j.getCuenta(usuario));
			tv.adicionarObjetoLectura(j.getCuenta(usuario));
			total+= productoCarrito.getP().getPrecio();
		}
		tv = j.iniciarTransaccion(tv);
		Cuenta cuenta = j.getCuenta(usuario);
		Cuenta cuentaTemporal = new Cuenta(cuenta.getUsuario(), cuenta.getContrasena(), cuenta.getTarjeta(), cuenta.getSaldo());
		System.out.println("Su saldo: "+ cuenta.getSaldo());
		if(cuenta.getSaldo()<=total){
			cuentaTemporal.setSaldo(cuenta.getSaldo()-total);
			System.out.println("estado transacción: "+tv.getEstado());
			while(tv.getEstado()==1||tv.getEstado()==-1){
				System.out.println("estado transacción: "+tv.getEstado());
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if(tv.getEstado()==2){
				cuenta = cuentaTemporal;
				System.out.println("Iniciando compra de cada producto");
			}else if(tv.getEstado()==3){
				System.out.println("Transacción abortada ... verifique su saldo de nuevo");
				saldo = false;
			}
			j.finalizarTransaccion(tv);
		}else{
			System.out.println("Saldo insuficiente");
		}



	}
	public static void main(String[] args) throws MalformedURLException, RemoteException, NotBoundException {


		System.setProperty("java.security.policy", "./cliente.policy");
		Registry registry = null;



		//coordinador = (CoordinadorInterface) registry.lookup("//127.0.0.1/Coordinador");

		j = null;
		try {
			registry = LocateRegistry.getRegistry(portCuentas);
			j = (CuentaRMII) registry.lookup("//127.0.0.1/Cuentas");
		}  catch (NotBoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}



		i = null;
		try {
			registry = LocateRegistry.getRegistry(portProductos);
			i = (ProductoRMII) registry.lookup("//127.0.0.1/Productos");
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

package Cliente;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import Cuenta.Cuenta;
import Cuenta.CuentaRMII;
import Producto.Producto;
import Producto.ProductoRMII;

public class Cliente {
	
	public static void main(String[] args) throws MalformedURLException, RemoteException, NotBoundException {
		ArrayList<Cuenta> cuentas = new ArrayList<Cuenta>(); 
		ArrayList<Producto> productos = new ArrayList<Producto>(); 
		CuentaRMII j = (CuentaRMII) Naming.lookup("rmi://localhost:5000//cuenta");
		ProductoRMII i = (ProductoRMII) Naming.lookup("rmi://localhost:1900//producto");
		j.leerCuentas("iniciocuentas.txt");
		j.imprimirCuentas();
		i.leerProductos("inicioproductos.txt");
		i.imprimirProductos();
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
		    	                                	i.comprarProductos(carrito);
		    	                                	break;
		    	                                }
		    	                               
			    	                       }
		                             }
	            	}else {
		            	  	System.out.println("Datos inválidos");
	            	  }
	        	}
        	}
	       
	    }
	}
	public static void imprimirProductos(ArrayList<Producto> productos) {
		int i=1;
		for (Producto producto : productos) {
			System.out.println(i+"  "+producto.toString());
			i++;
		}
	}

}

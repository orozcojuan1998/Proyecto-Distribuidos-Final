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
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import modelo.Cliente;
import modelo.Producto;

public class RMIinit {

	public static void main(String[] args) throws RemoteException {
		Inter interfaz =  (Inter) new InterImple(); 
		interfaz.leerArchivo("inicio.txt");
		ArrayList<Cliente> clientes = interfaz.getClientes();
		ArrayList<Producto> productos = interfaz.getProductos();
		 while(true){
		        String value; 
		        Scanner input = new Scanner(System.in);
		        System.out.println("Seleccione una opción por favor: ");
		        System.out.println("1. Ingresar");
		        value = input.next();
		        switch(value) {
		            case "1":{
		            	  System.out.println("Ingrese su tarjeta por favor");
		            	  value = input.next();
		            	  if(interfaz.validar(value,clientes)) {
		            		  String hash=interfaz.hashearContra(value);
		                	  System.out.println("Press Any Key To Continue...");
		                      new java.util.Scanner(System.in).nextLine();
		                      comprar(interfaz,hash,productos);
		            	  }else {
		            		  System.out.println("No está registrado");
		            		  System.out.println("Press Any Key To Continue...");
		                      new java.util.Scanner(System.in).nextLine();
		            	  
		            	  }
		        
		            }
		            default:{
		            	System.out.println("Ingrese una opción válida por favor");
		            }
		        }
		 }
	}
	private static void comprar(Inter interfaz, String tarjetaHas1h,ArrayList<Producto> productos) throws RemoteException {
          Map<String, String> card_value = new HashMap<String, String>();
          int indexTX;
          Map<Integer, String> carrito = new HashMap<Integer, String>();
          List<String> TX_i = new ArrayList<String>();
          String operation;
          Scanner input = new Scanner(System.in);
          String value;
          System.out.println("1. Ver productos");
          System.out.println("2. Agregar saldo");
          value = input.next();
          if(value.equals("1")){
              indexTX = interfaz.get_indexTX();
              interfaz.set_indexTX();
              interfaz.crearTransaccion(indexTX, TX_i);
              System.out.println("ID     Producto        Precio        Disponibles");
              for(int i = 0;i<productos.size();i++)
              {
                  System.out.println(i+1 +"\t  " + productos.get(i).getNombre() +"\t  " +  productos.get(i).getPrecio() +"\t  " + "\t  " +  productos.get(i).getCantidadDisponible());
              }
              Boolean comprar = true;
              while(comprar){
                  System.out.println("Seleccione el ID del producto para agregar al carrito de compras...");
                  int item = input.nextInt();
                  String s_item = Integer.toString(item);
                  operation = "W," + s_item;
                  interfaz.agregarOp(indexTX, operation);
                  System.out.println("index->" + indexTX + "operation->" + operation);
                  Boolean agregar = true;
                  while (agregar){
                      System.out.println("¿Cuántos desea?..."); //valida si hay suficientes disponibles
                      String num = input.next();
                      int cant=productos.get(item-1).getCantidadDisponible();
                      if(Integer.parseInt(num) <= cant){
                          carrito.put(item, num);
                          agregar = false;
                          productos.get(item-1).setCantidadDisponible(cant-Integer.parseInt(num));
                          System.out.println("Producto agregado exitosamente");
                          System.out.println("ID     Producto        Precio        Disponibles");
                          for(int i = 0;i<productos.size();i++)
                          {
                              System.out.println(i+1 +"\t  " + productos.get(i).getNombre() +"\t  " +  productos.get(i).getPrecio() +"\t  " + "\t  " +  productos.get(i).getCantidadDisponible());
                          }
                          System.out.println("¿Desea agregar mas? \n 1. Si \n 2. No");
                          String next = input.next();
                          if(next.equals("2")){
                              comprar = false;
                          }
                      }
                      else{
                          System.out.println("No hay suficientes prodcutos disponibles, ingrese una cantidad inferior...");
                      } 
                  }
                  
                  while(true){
                      System.out.println("El carrito de compras act:");
                      Iterator it = carrito.keySet().iterator();
                      System.out.println("Numero     Item        Precio        Cantidad");
                      while(it.hasNext()){
                        Integer key = (Integer) it.next();
                        System.out.println(key +"\t  " + productos.get(key-1).getNombre() +"\t  " +productos.get(key-1).getPrecio() +"\t  " + "\t  " + carrito.get(key));
                      }
                      
                      System.out.println("\n 1. Eliminar producto \n 2. Cambiar cantidad producto \n 3. Finalizar compra");
                      String fix = input.next();

                      if(fix.equals("1")){
                          System.out.println("Ingrese el numero del item");
                          Integer item_del = input.nextInt();
                          String o = carrito.get(item_del);
                          System.out.println("xxxx "+o);
                          carrito.remove(item_del);
                          System.out.println("Elemento eliminado del carrito exitosamente");
                      }
                      
                  

                      /*if(fix.equals("2")){

                          System.out.println("Ingrese el numero del item");
                          Integer item_set = input.nextInt();
                          System.out.println("La cantidad actual es -> " + carrito.get(item_set));
                          
                          
                          Boolean fixQuantity = true;
                          while (fixQuantity){
                              System.out.println("Ingrese la nueva cantidad");
                              String new_quantity = input.next();
                              if(Integer.parseInt(new_quantity) <= Integer.parseInt(quantity[item_set - 1])){
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
                          

                      }*/
                      
                  }
              }
	
          }
          
	}
	
}

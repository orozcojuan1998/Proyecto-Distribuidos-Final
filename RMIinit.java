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
		        System.out.println("1. Ingresar : ");
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
		        }
		 }
	}
	private static void comprar(Inter interfaz, String tarjetaHas1h,ArrayList<Producto> productos) throws RemoteException {
		  String[] items, prices, quantity;
          Map<String, String> card_value = new HashMap<String, String>();
          int indexTX;
          Map<Integer, String> carrito = new HashMap<Integer, String>();
          List<String> TX_i = new ArrayList<String>();
          String operation;
          Scanner input = new Scanner(System.in);
          String value;
          System.out.println("1. Ver productos: ");
          System.out.println("2. Agregar saldo : ");
          value = input.next();
          if(value.equals("1")){
              indexTX = interfaz.get_indexTX();
              interfaz.set_indexTX();
              interfaz.crearTransaccion(indexTX, TX_i);
              System.out.println("Numero     Item        Precio        Disponibles");
              for(int i = 0;i<productos.size();i++)
              {
                  System.out.println(i+1 +"\t  " + productos.get(i).getNombre() +"\t  " +  productos.get(i).getPrecio() +"\t  " + "\t  " +  productos.get(i).getCantidadDisponible());
              }
              /*Boolean nextItem = true;
              while(nextItem){
                  System.out.println("Escoja un item para agregar al carrito de compras...");
                  int item = input.nextInt();
                  String s_item = Integer.toString(item);
                  operation = "W," + s_item;
                  access.add_operation(indexTX, operation);
                  System.out.println("index->" + indexTX + "operation->" + operation);
                  Boolean addItem = true;
                  while (addItem){
                      System.out.println("¿Cuantos desea?..."); //valida si hay suficientes disponibles
                      String num = input.next();
                      if(Integer.parseInt(num) <= Integer.parseInt(quantity[item - 1])){
                          carrito.put(item, num);
                          addItem = false;
                          System.out.println("Item agregado exitosamente");
                          System.out.println("¿Desea agregar mas? 1. Si  2. No");
                          String next = input.next();
                          if(next.equals("2")){
                              nextItem = false;
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

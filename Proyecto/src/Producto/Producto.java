package Producto;

import java.io.Serializable;

public class Producto implements Serializable{
	public String nombre;
	public int cantidadDisponible;
	public float precio;
	
	public Producto(String nombre, int cantidadDisponible, float precio) {
		super();
		this.nombre = nombre;
		this.cantidadDisponible = cantidadDisponible;
		this.precio = precio;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public int getCantidadDisponible() {
		return cantidadDisponible;
	}
	public void setCantidadDisponible(int cantidadDisponible) {
		this.cantidadDisponible = cantidadDisponible;
	}
	public float getPrecio() {
		return precio;
	}
	public void setPrecio(float precio) {
		this.precio = precio;
	}
	@Override
	public String toString() {
		return "Producto [nombre=" + nombre + ", cantidadDisponible=" + cantidadDisponible + ", precio=" + precio + "]";
	}
	
}

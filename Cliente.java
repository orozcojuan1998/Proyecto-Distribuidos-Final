package modelo;

public class Cliente {

	public String tarjeta;
	public float saldo;
	
	public String getTarjeta() {
		return tarjeta;
	}
	public void setTarjeta(String tarjeta) {
		this.tarjeta = tarjeta;
	}
	public float getSaldo() {
		return saldo;
	}
	public void setSaldo(float saldo) {
		this.saldo = saldo;
	}
	public Cliente(String tarjeta, float saldo) {
		super();
		this.tarjeta = tarjeta;
		this.saldo = saldo;
	}
	@Override
	public String toString() {
		return "Cliente [tarjeta=" + tarjeta + ", saldo=" + saldo + "]";
	}
	
	
	
}

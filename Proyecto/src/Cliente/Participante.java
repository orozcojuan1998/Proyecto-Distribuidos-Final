package Cliente;

import java.rmi.RemoteException;

import Coordinador.Coordinador;
import Coordinador.CoordinadorInterface;

public class Participante implements ParticipanteInterface {

	private Transaccion tv;
	private int num;
	private int estado;
	
	public Participante(Transaccion tv, CoordinadorInterface coordinador, int num) throws RemoteException {	
		estado = -1;
		this.tv = tv;
		this.num = num;
		coordinador.addParticipant(this, 0);
	}

	public Transaccion getTv() {
		return tv;
	}

	public void setTv(Transaccion tv) {
		this.tv = tv;
	}

	public int getNum() {
		return num;
	}

	public void setNum(int num) {
		this.num = num;
	}

	public synchronized int getEstado() {
		return estado;
	}

	public void setEstado(int estado) {
		this.estado = estado;
	}

	@Override
	public void prepareCommit() throws RemoteException {
		estado = 1;
	}

	@Override
	public void doAbort() throws RemoteException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void doCommit() throws RemoteException {
		// TODO Auto-generated method stub
		
	}


}

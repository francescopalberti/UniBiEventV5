package it.unibs;

import java.io.Serializable;

public class Campo<T> implements Serializable {
	
	private String nome;
	private String descrizione;
	private boolean obbligatoriet�;
	private T valore;
	
	public Campo(String _nome, String _descrizione, boolean _obbligatoriet�){
		
		nome=_nome;
		descrizione=_descrizione;
		obbligatoriet�=_obbligatoriet�;
		valore=null;
		
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getDescrizione() {
		return descrizione;
	}

	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}

	public boolean isObbligatorio() {
		return obbligatoriet�;
	}

	public void setObbligatoriet�(boolean obbligatoriet�) {
		this.obbligatoriet� = obbligatoriet�;
	}

	public T getValore() {
		return valore;
	}

	public void setValore(T valore) {
		this.valore = valore;
	}
	
	public String toStringValore() {
		return nome + ": " + valore;
	}
	
	public String toString() {
		String S;
		if (obbligatoriet�) {
			S = nome + "* : " + descrizione;
			}
		else {
			S = nome + " : " + descrizione;
		}
		return S;
	}
}
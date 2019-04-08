package it.unibs;

import java.io.*;
import java.util.Arrays;
import java.util.Vector;

public class Concerto extends Categoria implements Serializable{
	
	private static final int BAND_PRINCIPALE=0;
	private static final int BAND_ACCOMPAGNAMENTO=1;
	private static final int QUOTA_GADGET=2;
	private static final int QUOTA_CD=3;
	private static final int QUOTA_FREE_DRINK=4;

	private static final String descrizione = "Un concerto";
	private static final String nome = "Concerto";
	private Campo[] campiSpecifici;
	private Campo[] quoteFacoltative;
	
	private Vector<SpazioPersonale>listaPagantiGadget;
	private Vector<SpazioPersonale>listaPagantiCD;
	private Vector<SpazioPersonale>listaPagantiDrink;
	
	private static final String lineSeparator="\n";

	public Concerto(Campo[] _campiGenerici, Campo[] _campiSpecifici, SpazioPersonale _creatore) {
		super(nome, descrizione, _campiGenerici,_creatore);
		campiSpecifici = new Campo[5];
		campiSpecifici = _campiSpecifici;
		quoteFacoltative = new Campo[3];
		quoteFacoltative = Arrays.copyOfRange(_campiSpecifici, 2, 5);
		listaPagantiGadget=new Vector<SpazioPersonale>();
		listaPagantiCD=new Vector<SpazioPersonale>();
		listaPagantiDrink=new Vector<SpazioPersonale>();
	}

	public Campo[] getCampiSpecifici() {
		return campiSpecifici;
	}
	
	
	public String getCampiCompilati()
	{
		StringBuffer s = new StringBuffer();
		s.append(super.getCampiCompilati());
		for(int j=0; j<campiSpecifici.length; j++) {
			s.append("   " + campiSpecifici[j].toStringValore());
			s.append(lineSeparator);
		}
		s.append("Stato: " + getStato());
		return s.toString();
	}
	
	public void aggiungiPartecipante(SpazioPersonale partecipante) {
		super.aggiungiPartecipante(partecipante);
		for(int i=0;i<3;i++) {
			if(quoteFacoltative[i].getValore()!=null) {
				Boolean fine=false;
				String mex="Vuoi aderire alla quota aggiuntiva "
				+ quoteFacoltative[i].getNome()+" di "+quoteFacoltative[i].getValore()+"€? (SI/NO)";
				do {
					String lettura=Utility.leggiStringa(mex);
					if(lettura.equalsIgnoreCase("si")) {
						switch(i) {
							case 0:listaPagantiGadget.add(partecipante);
								break;
							case 1:listaPagantiCD.add(partecipante);
								break;
							case 2:listaPagantiDrink.add(partecipante);
								break;
						}
						fine=true;
					} else if(lettura.equalsIgnoreCase("no")) {	
						fine=true;
					} else System.out.println("Inserimento non valido, digitare SI o NO.");
				} while(!fine);

			}
		}
	}
	
	public String infoChiusura(SpazioPersonale profilo) {
		StringBuffer s = new StringBuffer();
			s.append(super.infoChiusura(profilo));  //chiama quello di Categoria
			s.append(lineSeparator);
			s.append(infoPagamentoConcerto(profilo));  
			s.append(lineSeparator);
		return s.toString();
	}
	
	private String infoPagamentoConcerto(SpazioPersonale profilo) {
		
		StringBuffer s = new StringBuffer();
		Integer quotaTotale=(Integer)campiBase[QUOTA].getValore();
		if(listaPagantiGadget.contains(profilo)) {
			quotaTotale=quotaTotale+(Integer)campiSpecifici[QUOTA_GADGET].getValore();
		}
		if(listaPagantiCD.contains(profilo)) {
			quotaTotale=quotaTotale+(Integer)campiSpecifici[QUOTA_CD].getValore();
		}
		if(listaPagantiDrink.contains(profilo)) {
			quotaTotale=quotaTotale+(Integer)campiSpecifici[QUOTA_FREE_DRINK].getValore();
		}
			s.append("Importo totale (iscrizione + extra) : "+ quotaTotale + "€");
			s.append(lineSeparator);
		return s.toString();
	}
	

}

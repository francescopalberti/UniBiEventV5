package it.unibs;

import java.io.*;
import java.util.Arrays;
import java.util.Vector;

public class Concerto extends Categoria implements Serializable{
	
	private static final int BAND_PRINCIPALE=14;
	private static final int BAND_ACCOMPAGNAMENTO=15;
	private static final int QUOTA_GADGET=16;
	private static final int QUOTA_CD=17;
	private static final int QUOTA_FREE_DRINK=18;

	private static final String descrizione = "Un concerto";
	private static final String nome = "Concerto";
	private Campo[] campiSpecifici;
	private Campo[] quoteFacoltative;
	
	private Vector<SpazioPersonale>listaGadget;
	private Vector<SpazioPersonale>listaCD;
	private Vector<SpazioPersonale>listaDrink;
	
	private static final String lineSeparator="\n";

	public Concerto(Campo[] _campiGenerici, Campo[] _campiSpecifici, SpazioPersonale _creatore) {
		super(nome, descrizione, _campiGenerici,_creatore);
		campiSpecifici = new Campo[5];
		campiSpecifici = _campiSpecifici;
		quoteFacoltative = Arrays.copyOfRange(_campiSpecifici, 2, 5);
		listaGadget=new Vector<SpazioPersonale>();
		listaCD=new Vector<SpazioPersonale>();
		listaDrink=new Vector<SpazioPersonale>();
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
		return s.toString();
	}
	
	public void aggiungiPartecipante(SpazioPersonale partecipante) {
		super.aggiungiPartecipante(partecipante);
		for(int i=0;i<quoteFacoltative.length;i++) {
			if(quoteFacoltative[i].getValore()!=null) {
				Boolean fine=false;
				String mex="Vuoi aderire alla quota aggiuntiva "
				+ quoteFacoltative[i].getNome()+" di "+quoteFacoltative[i].getValore()+"€? (SI/NO)";
				do {
					String lettura=Utility.leggiStringa(mex);
					if(lettura.equalsIgnoreCase("si")) {
						switch(i) {
							case 0:listaGadget.add(partecipante);
								break;
							case 1:listaCD.add(partecipante);
								break;
							case 2:listaDrink.add(partecipante);
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
	
	
	

}

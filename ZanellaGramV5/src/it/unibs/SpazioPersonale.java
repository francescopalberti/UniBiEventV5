package it.unibs;

import java.io.Serializable;
import java.util.Iterator;
import java.util.Vector;

public class SpazioPersonale implements Serializable {
	private Vector<Categoria> eventiPrenotati;
	private Vector<String> notifiche;
	private Vector<Categoria> eventiCreati;
	
	private String nomignolo;
	private FasciaDiEta eta;
	private Vector<String> categoriePreferite=new Vector<String>();
	
	
	public SpazioPersonale(String nomignolo) {
		this.nomignolo=nomignolo;
		this.eventiPrenotati = new Vector<Categoria>();
		this.eventiCreati= new Vector<Categoria>();
		this.notifiche = new Vector<String>();
	}
	
	public void addCategoriaPreferita(String preferita) {
		for(String s:categoriePreferite)
			if(s.equals(preferita))
				return;
		categoriePreferite.add(preferita);
	}
	
	public void deletePreferiti() {
		categoriePreferite.clear();
	}
	
	public Vector<String> getCategoriePreferite() {
		return categoriePreferite;
	}


	public void setCategoriePreferite(Vector<String> categoriePreferite) {
		this.categoriePreferite = categoriePreferite;
	}


	public String getNomignolo() {
		return nomignolo;
	}


	public void setEta(FasciaDiEta eta) {
		this.eta = eta;
	}


	/**
	 * @param evento da aggiungere
	 */
	public void addEventoPrenotato(Categoria eventoPrenotato) {
		eventiPrenotati.add(eventoPrenotato);
	}
	
	public void addEventoCreato(Categoria eventoCreato) {
		eventiCreati.add(eventoCreato);
	}
	
	public Vector<Categoria> getEventi(){
		return eventiPrenotati;
	}
	
	/**
	 * @return false se non c'è l'evento nel vector
	 */
	public boolean deleteEventoPrenotato(Categoria evento) {
		return eventiPrenotati.remove(evento);
	}
	
	public void deleteEventoPrenotato(int posEvento) {
		eventiPrenotati.remove(posEvento);
	}
	
	public boolean deleteEventoCreato(Categoria evento) {
		return eventiCreati.remove(evento);
	}
	
	public void deleteEventoCreato(int posEvento) {
		eventiCreati.remove(posEvento);
	}

	/**
	 * @param notifica da aggiungere
	 */
	public void addNotifica(String notifica) {
		notifiche.add(notifica);
	}
	
	/**
	 * @return false se non c'è la notifica nel vector
	 */
	public boolean deleteNotifica(String notifica) {
		return notifiche.remove(notifica);
	}
	
	public String deleteNotifica(int pos) {
		return notifiche.remove(pos);
	}
	
	public boolean noNotifiche() {
		return notifiche.isEmpty();
	}
	
	
	public void stampaNotifiche() {
		System.out.println("NOTIFICHE:");
		int i=0;
		for (String notifica : notifiche) {
			System.out.println(i+1 + ") " + notifica);
			i++;
		}
	}
	
	public void stampaEventiPrenotati() {
		System.out.println("EVENTI A CUI SONO ISCRITTO:");
		for(int i=0; i< eventiPrenotati.size(); i++) { 
			System.out.println(eventiPrenotati.get(i).getNome() + " " + (i+1));
			System.out.println(eventiPrenotati.get(i).getCampiCompilati());
		}
	}
	
	public void stampaEventiCreati() {
		System.out.println("EVENTI CHE HO CREATO:");
		for(int i=0; i< eventiCreati.size(); i++) { 
			System.out.println(eventiCreati.get(i).getNome() + " " + (i+1));
			System.out.println(eventiCreati.get(i).getCampiCompilati());
		}
	}

	public int getNumeroNotifiche() {
		return notifiche.size();
	}

	public Vector<Categoria> getEventiPrenotati() {
		return eventiPrenotati;
	}


	public Vector<Categoria> getEventiCreati() {
		return eventiCreati;
	}


	public boolean hasEventiPrenotati() {
		return eventiPrenotati.size()!=0;	
	}
	
	public boolean hasEventiCreati() {
		return eventiCreati.size()!=0;	
	}

	public boolean isPartecipante(Categoria evento) {
		for(Categoria c1: eventiPrenotati)
			if(evento.getCampiCompilati().equals(c1.getCampiCompilati())) return true;
		for(Categoria c2: eventiCreati)
			if(evento.getCampiCompilati().equals(c2.getCampiCompilati())) return true;
		return false;
	}
	
	public Vector<SpazioPersonale> getListaExPartecipanti(String categoria){
		Vector<SpazioPersonale> listaExPartecipanti = new Vector<SpazioPersonale>();
		for (Categoria eventoCreato : eventiCreati) {
			if(eventoCreato.getNome().equals(categoria)) listaExPartecipanti.addAll(eventoCreato.getListaPartecipanti());
		} 
		return listaExPartecipanti;
		
	}
	
	public void stampaExPartecipanti(String categoria) {
		Vector<SpazioPersonale> listaExPartecipanti = getListaExPartecipanti(categoria);
		System.out.println("Ex Partecipanti :");
		int i=0;
		for (SpazioPersonale spazioPersonale : listaExPartecipanti) {
			System.out.println(i+1 + ") " + spazioPersonale.getNomignolo());
			i++;
		}
	}
	
	public void notificaInteressamento(String categoria) {
		
		for (String categoriaPreferita : categoriePreferite) {
			if (categoria.equalsIgnoreCase(categoriaPreferita)) {
				notifiche.add("E' stato creato un nuovo evento consigliato per la categoria "+categoria+"\n");
			}
		}
		
	}
}

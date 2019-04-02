package it.unibs;

import java.io.*;
import java.util.Arrays;
import java.util.Date;
import java.util.Vector;


public class Application {
	
	public static String pathProfili = "C:\\Users\\zenry\\git\\ZanellaGramV5\\ZanellaGramV5\\data\\profili.dat";
	public static String pathPartite = "C:\\Users\\zenry\\git\\ZanellaGramV5\\ZanellaGramV5\\data\\partite.dat";
	public static String pathConcerti = "C:\\Users\\zenry\\git\\ZanellaGramV5\\ZanellaGramV5\\data\\concerti.dat";
	
	
	private static final int TITOLO=0;
	private static final int NUMERO_PARTECIPANTI=1;
	private static final int TERMINE_ISCRIZIONI=2;
	private static final int LUOGO=3;
	private static final int DATA=4;
	private static final int ORA=5;
	private static final int DURATA=6;
	private static final int QUOTA=7;
	private static final int COMPRESO_IN_QUOTA=8;
	private static final int DATA_CONCLUSIVA=9;
	private static final int ORA_CONCLUSIVA=10;
	private static final int NOTE=11;
	private static final int TOLLERANZA_PARTECIPANTI=12;
	private static final int TERMINE_RITIRO_ISCRIZIONE=13;
	
	
	private static final int GENERE=14;
	private static final int FASCIA_DI_ETA=15;
	
	private static final int BAND_PRINCIPALE=14;
	private static final int BAND_ACCOMPAGNAMENTO=15;
	private static final int QUOTA_GADGET=16;
	private static final int QUOTA_CD=17;
	private static final int QUOTA_FREE_DRINK=18;
	
	private String[] categorie = {"Partita di calcio","Concerto"};
	private Data dataOdierna;
	private Ora oraAttuale;
	private SpazioPersonale mioProfilo;
	private String titoloMain = "HOME";
	private String titoloLogin = "LOGIN/ACCEDI";
	private Vector<PartitaDiCalcio> listaPartite;
	private Vector<Concerto> listaConcerti;
	private Vector<SpazioPersonale> profili;
	private String[] vociMain = {"Esci e salva","Vedi eventi", "Crea evento", "Vedi profilo"};
	private String[] vociSpazioPersonale = {"Esci","Vedi eventi che ho creato","Vedi eventi a cui sono iscritto","Vedi notifiche","Modifica dati personali","Gestisci inviti"};
	private String[] vociLogin = {"Accedi","Registrati"};
	private static final String[] vociGestioneInviti = {"Esci","Invita tutti", "Seleziona profili"};
	
	private Campo[] campi;
	
	public Application(Data dataOdierna, Ora oraAttuale) throws ClassNotFoundException, IOException {
		initObjects();
		this.dataOdierna=dataOdierna;
		this.oraAttuale=oraAttuale;
	}
	

	@SuppressWarnings("unchecked")
	private void initObjects() throws ClassNotFoundException, IOException {
		
		//caricamento oggetti
		if(new File(pathProfili).exists())profili=(Vector<SpazioPersonale>)caricaOggetto(pathProfili, SpazioPersonale.class);
		else profili = new Vector<SpazioPersonale>();
		
		if(new File(pathPartite).exists())listaPartite=(Vector<PartitaDiCalcio>)caricaOggetto(pathPartite, PartitaDiCalcio.class);
		else listaPartite = new Vector<PartitaDiCalcio>();
		
		if(new File(pathConcerti).exists())listaConcerti=(Vector<Concerto>)caricaOggetto(pathConcerti, Concerto.class);
		else listaConcerti = new Vector<Concerto>();
	}
	
	@SuppressWarnings("unchecked")
	public Object caricaOggetto(String path, Class c) throws ClassNotFoundException, IOException
	{
		FileInputStream in = new FileInputStream(new File(path));
		ObjectInputStream objectIn=new ObjectInputStream(in);
		Object result=new Object();
		
		if(c==PartitaDiCalcio.class) {
			result = (Vector<Categoria>) objectIn.readObject();
			objectIn.close();
		}
		if(c==Concerto.class) {
			result = (Vector<Concerto>) objectIn.readObject();
			objectIn.close();
		}
		else if(c==SpazioPersonale.class){
			result = (Vector<SpazioPersonale>) objectIn.readObject();
			objectIn.close();
		}
		return result;
	}
	
	public void log() {
		boolean fine=false;
		while(!fine) {
			int i = Utility.scegli(titoloLogin,vociLogin,"Seleziona una voce",2);
			switch(i) {
				case 0:
					fine=accedi();
					break;
				case 1:
					registrati();
					fine=true;
					break;
				default: 
					System.out.println("Inserimento erraro!");
					break;
			}
		}
	}
	
	public boolean accedi() {	
		String nick=Utility.leggiStringa("Inserisci il nomignolo per accedere ");
		for(SpazioPersonale p:profili)
			if(p.getNomignolo().equals(nick)) {
				mioProfilo=p;
				return true;
			}
		System.out.println("Credenziali errate!");
		return false;
	}
	
	public void registrati() {
		System.out.println("REGISTRAZIONE");
		boolean fine=false;
		do {
			String nick=Utility.leggiStringa("Nomignolo*");
			if(nick=="") System.out.println("Il nomignolo è obbligatorio!");
			else {
				if(controlloNomignolo(nick)) {
					mioProfilo=new SpazioPersonale(nick);
					profili.add(mioProfilo);
					infoAggiuntive();
					fine=true;
				} else {
					System.out.println("Nomignolo già esistente!");
				}
			}
		}while(!fine);
	}
	
	public boolean controlloNomignolo(String nick) {
		if(profili.size()==0)return true;
		for(SpazioPersonale p:profili)
			if(nick.equals(p.getNomignolo()))return false;
		return true;
	}
	
	public void infoAggiuntive() {
	   FasciaDiEta fascia = new FasciaDiEta(Utility.leggiIntero("\nEtà min"), Utility.leggiIntero("Età max"));
	   mioProfilo.setEta(fascia);
	   mioProfilo.deletePreferiti();
	   stampaCategorie();
	   boolean fine;
	   int scelta;
	   do {
		   scelta = Utility.leggiIntero("Seleziona categoria d'interesse (0 per terminare)");
		   if(scelta>categorie.length) System.out.println("Scelta non valida!!");
		   else if (scelta==0) return;
		   else {
				   String preferita = categorie[scelta-1];
				   mioProfilo.addCategoriaPreferita(preferita);
		   		}
	   }while(scelta!=0);
	}
	
	public void runApplication() throws IOException {
		log();
		controlloEventi();
		boolean fine=false;
		while(!fine)
		{	
			int i = Utility.scegli(titoloMain,vociMain,"Seleziona una voce",4);
			switch(i) {
				case 0: {fine=true;
					esciEsalva();}
					break;
				case 1:vediCategorie();
					break;
				case 2:creaEvento();
					break;
				case 3: visualizzaSpazioPersonale();
					break;
				default: System.out.println("Scelta non valida!");
					break;
				
			}
		}
	}


	private void controlloEventi() {
		
		for (Categoria evento : listaPartite) {
			evento.aggiornaStato(dataOdierna);
		}
		for (Categoria evento2 : listaConcerti) {
			evento2.aggiornaStato(dataOdierna);
		}
	}

	private void creaEvento() {
		stampaCategorie();
		int scelta= Utility.sceltaDaLista("Seleziona categoria (0 per tornare alla home)",categorie.length);
		switch(scelta)
		{
			case 1: compilaEvento(PartitaDiCalcio.class);
				break;
			case 2: compilaEvento(Concerto.class);
				break;
			case 0: return;
		}
	}
	
	public void compilaEvento(Class c) {
		if(c==PartitaDiCalcio.class) {
			compilazionePartita();
		}
		if(c==Concerto.class) {
			compilazioneConcerto();
		}
		
	}
	
	
	private void compilazionePartita() {
		Campo[] campi =new Campo [16];
		assegnaPartitaDiCalcio(campi);
		compilazioneCampiGenerici(campi);
		for (int i = 14; i < 16; i++) {
			System.out.print(campi[i].toString());
			switch (i)
			{	
			   case GENERE:
				   campi[i].setValore(Utility.leggiStringa(""));
			      break;
			   case FASCIA_DI_ETA:
				   Integer min=Utility.leggiIntero("\nEtà min");
				   Integer max=Utility.leggiIntero("Età max");
				   if(!(min==null && max==null)) {
					   FasciaDiEta fascia = new FasciaDiEta(min, max);
					   campi[i].setValore(fascia);		   
				   }

				      break;
			}
		}
		if(controlloCompilazione(campi)){
			PartitaDiCalcio unaPartita = new PartitaDiCalcio(Arrays.copyOfRange(campi, 0, 14), Arrays.copyOfRange(campi, 14, 16),mioProfilo);
			listaPartite.add(unaPartita);
			mioProfilo.addEventoCreato(unaPartita);
			notificaInteressati("Partita di calcio"); //da correggere
		} else {
			System.out.println("Non hai compilato alcuni campi obbligatori");
		}
	}
	
	
	public void compilazioneConcerto() {
		Campo[] campi =new Campo [19];
		assegnaConcerto(campi);
		compilazioneCampiGenerici(campi);
		for (int i = 14; i < 19; i++) {
			System.out.print(campi[i].toString());
			switch (i)
			{	
			   case BAND_PRINCIPALE:
			   case BAND_ACCOMPAGNAMENTO:
				   campi[i].setValore(Utility.leggiStringa(""));
			      break;
			   case QUOTA_GADGET:
			   case QUOTA_CD:
			   case QUOTA_FREE_DRINK:
				   campi[i].setValore(Utility.leggiIntero(""));
				      break;
			}
		}
		if(controlloCompilazione(campi)){
			Concerto unConcerto = new Concerto(Arrays.copyOfRange(campi, 0, 14), Arrays.copyOfRange(campi, 14, 19),mioProfilo);
			listaConcerti.add(unConcerto);
			mioProfilo.addEventoCreato(unConcerto);
			notificaInteressati("Concerto"); 
		} else {
			System.out.println("Non hai compilato alcuni campi obbligatori");
		}
		
	}
	
	
	public void compilazioneCampiGenerici(Campo [] campi){
		for (int i = 0; i < 14; i++) {
			System.out.print(campi[i].toString());
			switch (i)
			{
			   case NUMERO_PARTECIPANTI:
			   case QUOTA:
			   case TOLLERANZA_PARTECIPANTI:
			      campi[i].setValore(Utility.leggiIntero(""));
			      break;
			   case TITOLO:
			   case LUOGO:
			   case COMPRESO_IN_QUOTA:
			   case NOTE:
				   campi[i].setValore(Utility.leggiStringa(""));
			      break;
			   case TERMINE_ISCRIZIONI:
			   case DATA:
			   case DATA_CONCLUSIVA:
			   case TERMINE_RITIRO_ISCRIZIONE:
				   Boolean formatoDataErrato=false;
				   Data date;
				   Integer gg, mm, aa;
				   do {
					   gg=Utility.leggiIntero("\nGiorno");
					   mm=Utility.leggiIntero("Mese");
					   aa=Utility.leggiIntero("Anno");
					   if(gg==null && mm==null && aa==null) {
						   date=null;
						   formatoDataErrato=false;
					   }
					   else {
						   date = new Data(gg, mm, aa);
						   formatoDataErrato=!date.controlloData();
						   if (formatoDataErrato) System.out.println("Hai inserito una data nel formato errato!");
						   else campi[i].setValore(date); 
					   }
				   } while(formatoDataErrato);
				      break;
			   case ORA:
			   case DURATA:
			   case ORA_CONCLUSIVA:
				   Boolean formatoOraErrato=false;
				   Ora orario;
				   Integer ora,min;
				   do {
					   ora=Utility.leggiIntero("\nOra");
					   min=Utility.leggiIntero("Minuti");
					   if(ora==null && min==null) {
						   orario=null;
						   formatoOraErrato=false;
					   }
					   else {
						   orario = new Ora(ora, min);
						   formatoOraErrato=!orario.controlloOra();
						   if (formatoOraErrato) System.out.println("Hai inserito un orario nel formato errato!");
						   else campi[i].setValore(orario);
					   }
				   } while(formatoOraErrato);
				      break;
			}
		}
		
	}
	
	public Boolean controlloCompilazione(Campo [] campi) {
		for (int i = 0; i < campi.length; i++) {
			if(campi[i].isObbligatorio()) {
				if(campi[i].getValore()==null) return false;
			}
		}
		return true;
		
	}
	
	private void notificaInteressati(String categoria) {
		for (SpazioPersonale profilo : profili) {
			if(profilo!=mioProfilo) profilo.notificaInteressamento(categoria);
		}
		
	}

	public void vediCategorie()
	{
		stampaCategorie();
		int scelta= Utility.sceltaDaLista("Seleziona categoria (0 per tornare alla home)",categorie.length);
		switch(scelta)
		{
			case 1: if(vediEventi(getEventiDisponibili(PartitaDiCalcio.class)))
						scegliEvento(getEventiDisponibili(PartitaDiCalcio.class));
				break;
			case 2: if(vediEventi(getEventiDisponibili(Concerto.class)))
						scegliEvento(getEventiDisponibili(Concerto.class));
				break;
			case 0: return;
		}
	}
	
	
	public void stampaCategorie()
	{
		for (int i = 0; i < categorie.length; i++) {
			System.out.println(i+1 + ") " + categorie[i]);
		}
	}
	
	private Vector<Categoria> getEventiDisponibili(Class c){
		Vector<Categoria> disponibili = new Vector<Categoria>();
		if(c==PartitaDiCalcio.class) {
			for(Categoria p:listaPartite) 
				if(!mioProfilo.isPartecipante(p) && p.isAperto()) disponibili.add(p);		
		}
		if(c==Concerto.class) {
			for(Categoria p:listaConcerti) 
				if(!mioProfilo.isPartecipante(p) && p.isAperto()) disponibili.add(p);		
		}
		return disponibili;
	}
	
	public boolean vediEventi(Vector<Categoria> disponibili)
	{
		if(disponibili.size()==0) {
			System.out.println("Non ci sono eventi disponibili per questa categoria!");
			return false;
		}
		else {
			for(int i=0; i<disponibili.size(); i++) { 
				System.out.println(disponibili.get(i).getNome() + " " + (i+1));
				System.out.println(disponibili.get(i).getCampiCompilati());
			}
			return true;
		}
	}
	
	public void scegliEvento(Vector<Categoria> disponibili) {
		int a = Utility.sceltaDaLista("Seleziona evento a cui vuoi aderire (0 per uscire):", disponibili.size());
		
			if(a==0) return;
			else{
				partecipaEvento(disponibili.get(a-1));
			}	
		
	}
	
	private void visualizzaSpazioPersonale() {
		boolean fine=false;
		do {
			int i = Utility.scegli("SPAZIO PERSONALE DI --> "+mioProfilo.getNomignolo(),vociSpazioPersonale,"Seleziona una voce",6);
			switch(i) {
				case 0:fine=true;
					break;
				case 1:
					gestioneEventiCreati();
					fine=true;
					break;
				case 2:
					gestioneEventiPrenotati();
					fine=true;
					break;
				case 3:
					gestioneNotifiche();
					fine=true;
					break;
				case 4: 
					infoAggiuntive();
					fine=true;
					break;
				case 5:
					gestioneInviti();
					fine=true;
					break;
				default: System.out.println("Scelta non valida!");
					break;
			
				}
		}while(!fine);
	}
		
	

	private void gestioneEventiCreati() {
		int a;
		do {
			if(mioProfilo.hasEventiCreati()) { 
				mioProfilo.stampaEventiCreati();
				a = Utility.sceltaDaLista("Seleziona evento che vuoi ritirare (0 per uscire):", mioProfilo.getEventiCreati().size());
				if(a==0) return;
				else { 
					Categoria ritirato = mioProfilo.getEventiCreati().get(a-1);
					if(ritirato.isRitirabile(dataOdierna)) {
						mioProfilo.deleteEventoCreato(a-1); 
						ritirato.ritiraEvento();
					}else System.out.println("Non è più possibile ritirare quest'evento");
				}
			}else {
				System.out.println("Non hai creato nessun evento!");
				a=0;
			}
		}while(a!=0);
	}

	private void gestioneEventiPrenotati() {
		int a;
		do {
			if(mioProfilo.hasEventiPrenotati()) { 
				mioProfilo.stampaEventiPrenotati();
				a = Utility.sceltaDaLista("Seleziona evento a cui vuoi disiscriverti (0 per uscire):", mioProfilo.getEventiPrenotati().size());
				if(a==0) return;
				else {
					Categoria rimosso = mioProfilo.getEventiPrenotati().get(a-1);
					if(rimosso.isRitirabile(dataOdierna)){
						mioProfilo.deleteEventoPrenotato(a-1); 				
						rimosso.removePartecipante(mioProfilo);
					}else System.out.println("Non è più possibile disiscriversi da quest'evento");
				}
			}else {
				System.out.println("Non sei iscritto a nessun evento!");
				a=0;
			}
		}while(a!=0);
	}

	public void gestioneNotifiche() {
		int a;
		if(mioProfilo.noNotifiche()) {
			System.out.println("NON hai notifiche da visualizzare");
		}else {
			mioProfilo.stampaNotifiche();
			do {
				a = Utility.sceltaDaLista("Seleziona notifica che vuoi eliminare (0 per uscire):", mioProfilo.getNumeroNotifiche());
				if(a==0) return;
				else{
					mioProfilo.deleteNotifica(a-1); 
				}
			}while(a!=0);
		}
	}
	
	private void gestioneInviti() {
		int a;
		do {
			if(mioProfilo.hasEventiCreati()) { 
				mioProfilo.stampaEventiCreati();
				a = Utility.sceltaDaLista("Seleziona l'evento per cui vuoi invitare (0 per uscire)", mioProfilo.getEventiCreati().size());
				if(a==0) return;
				else { 
					
					Categoria eventoSelezionato = mioProfilo.getEventiCreati().get(a-1);
					String categoria = eventoSelezionato.getNome();
					Vector<SpazioPersonale> listaExPartecipanti = mioProfilo.getListaExPartecipanti(categoria);
						if(listaExPartecipanti.size()!=0) {
						boolean fine=false;
						do {
							int i = Utility.scegli("GESTIONE INVITI",vociGestioneInviti,"Scegli quale operazione effettuare",3);
							switch(i) {
								case 0:fine=true;
									break;
								case 1:
									for (SpazioPersonale profilo : listaExPartecipanti) {
										profilo.addNotifica("Sei invitato a: " + eventoSelezionato);
									}
									fine=true;
									break;
								case 2:
								int s;
								mioProfilo.stampaExPartecipanti(categoria);
								do {
										s = Utility.sceltaDaLista("Seleziona il profilo che vuoi invitare (0 per uscire):", listaExPartecipanti.size());
										if(s==0) return;
										else{
											listaExPartecipanti.get(s-1).addNotifica("Sei invitato a: " + eventoSelezionato); 
										}
									}while(s!=0);
									fine=true;
									break;
								default: System.out.println("Scelta non valida!");
									break;
								}
						}while(!fine);
					}else System.out.println("Non ci sono ex partecipanti a tale categoria!");
				}
			}else {
				System.out.println("Non hai creato nessun evento!");
				a=0;
			}
		}while(a!=0);
	}

	
	


	private void partecipaEvento(Categoria evento) {
		evento.aggiungiPartecipante(mioProfilo);
		mioProfilo.addEventoPrenotato(evento);
		controlloEventi();
	}
	
	
	
	public void assegnaEvento(Campo[] campi) 
	{
		campi[TITOLO]= new Campo<String>("Titolo","Titolo dell'evento",false);
		campi[NUMERO_PARTECIPANTI]=new Campo<Integer>("Numero partecipanti","Indica il numero massimo di partecipanti",true);
		campi[TERMINE_ISCRIZIONI]=new Campo<Data>("Data termine iscrizione","Indica la data limite entro cui iscriversi",true);
		campi[LUOGO]=new Campo<String>("Luogo","Indica il luogo dell'evento",true);
		campi[DATA]=new Campo<Data>("Data","Indica la data di svolgimento dell'evento",true);
		campi[ORA]=new Campo<Ora>("Ora","Indica l'ora di inizio dell'evento",true);
		campi[DURATA]=new Campo<Ora>("Durata","Indica la durata dell'evento",false);
		campi[QUOTA]=new Campo<Integer>("Quota iscrizione","Indica la spesa da sostenere per partecipare all'evento",true);
		campi[COMPRESO_IN_QUOTA]=new Campo<String>("Compreso in quota","Indica le voci di spesa comprese nella quota",false);
		campi[DATA_CONCLUSIVA]=new Campo<Data>("Data conclusiva","Indica la data di conclusione dell'evento",false);
		campi[ORA_CONCLUSIVA]=new Campo<Ora>("Ora conclusiva","Indica l'ora conclusiva dell'evento",false);
		campi[NOTE]=new Campo<String>("Note","Informazioni aggiuntive",false);		
		campi[TOLLERANZA_PARTECIPANTI]=new Campo<Integer>("Tolleranza numero di partecipanti","Indica quanti partecipanti siano accettabili in esubero a numero di partecipanti",false);
		campi[TERMINE_RITIRO_ISCRIZIONE]=new Campo<Data>("Termine ultimo di ritiro iscrizione","Indica la data entro cui ogni fruitore può cancellare la sua iscrizione",false);
	}
	
	public void assegnaPartitaDiCalcio(Campo[] campi) {
		
		assegnaEvento(campi);
		campi[GENERE]=new Campo<String>("Genere","Indica il genere dei giocatori",true);
		campi[FASCIA_DI_ETA]=new Campo<FasciaDiEta>("Fascia di età","Indica la fascia di età dei giocatori",true);
		
	}
	
	public void assegnaConcerto(Campo[] campi) {
		assegnaEvento(campi);
		campi[BAND_PRINCIPALE]=new Campo<String>("Band principale","Indica la band principale del concerto",true);
		campi[BAND_ACCOMPAGNAMENTO]=new Campo<FasciaDiEta>("Band di accompagnamento","Indica la band di accompagnamento del concerto",false);
		campi[QUOTA_GADGET]=new Campo<Integer>("Costo Gadget","Indica il costo dei gadget della band",false);
		campi[QUOTA_CD]=new Campo<Integer>("Costo CD","Indica il costo del CD della band",false);
		campi[QUOTA_FREE_DRINK]=new Campo<Integer>("Costo free drink","Indica il costo del free drink durante il concerto",false);
	}
	
	public void esciEsalva() throws IOException
	{
		System.out.println("Salvataggio...");
		
		ObjectOutputStream writerPartite=new ObjectOutputStream(new FileOutputStream(new File(pathPartite)));
		writerPartite.writeObject(listaPartite);
		writerPartite.close();
		
		ObjectOutputStream writerConcerti=new ObjectOutputStream(new FileOutputStream(new File(pathConcerti)));
		writerConcerti.writeObject(listaConcerti);
		writerConcerti.close();
		
		ObjectOutputStream writerProfili=new ObjectOutputStream(new FileOutputStream(new File(pathProfili)));
		writerProfili.writeObject(profili);
		writerProfili.close();
	}

		
}

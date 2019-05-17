package it.polito.tdp.meteo;

import java.util.ArrayList;
import java.util.List;

import it.polito.tdp.meteo.bean.Citta;
import it.polito.tdp.meteo.bean.SimpleCity;
import it.polito.tdp.meteo.db.MeteoDAO;

public class Model {
	
	private final static int COST =100;
	private final static int NUMERO_GIORNI_CITTA_CONSECUTIVI_MIN = 3;
	private final static int NUMERO_GIORNI_CITTA_MAX = 6;
	private final static int NUMERO_GIORNI_TOTALI = 15;
	
	private List<Citta> leCitta;
	private List<Citta> sequenzaMigliore;

	public Model() {
		MeteoDAO dao=new MeteoDAO();
		this.leCitta=dao.getAllCitta();
	}
	
	public List<Citta> allCitta(){
		return leCitta;
	}

	public Double getUmiditaMedia(int mese,Citta c) {
		MeteoDAO dao=new MeteoDAO();
		return dao.calcolaUmidita(mese,c);
	}

	public List<Citta> trovaSequenza(int mese) {
        List<Citta> parziale=new ArrayList<Citta>();
        this.sequenzaMigliore=null;
        MeteoDAO dao=new MeteoDAO();
        
        for(Citta c:leCitta) {
        	c.setRilevamenti(dao.getAllRilevamentiLocalitaMese(mese, c.getNome()));
        }
        cercaSoluzione(parziale,0);
		return sequenzaMigliore;
	}
	
	public void cercaSoluzione(List<Citta> parziale,int livello) {
		if(livello==NUMERO_GIORNI_TOTALI) {
			Double costo=calcolaCosto(parziale);
			if(sequenzaMigliore==null||costo<calcolaCosto(sequenzaMigliore)) {
				sequenzaMigliore=new ArrayList<>(parziale);
			}
		}
		else
			for(Citta prova: leCitta) {
				if(aggiuntaValida(prova,parziale)==true) {
					parziale.add(prova);
					cercaSoluzione(parziale,livello+1);
					parziale.remove(parziale.size()-1);
				}
			}
	}

	private Double calcolaCosto(List<Citta> parziale) {

		double costo = 0.0;

		// sommatoria delle umidit� in ciascuna citt�, considerando il rilevamendo del
		// giorno giusto
		// SOMMA parziale.get(giorno-1).getRilevamenti().get(giorno-1)
		for (int giorno = 1; giorno <= NUMERO_GIORNI_TOTALI; giorno++) {
			// dove mi trovo?
			Citta c = parziale.get(giorno - 1);
			// che umidit� ho in quel giorno in quella citt�?
			double umid = c.getRilevamenti().get(giorno - 1).getUmidita();
			costo += umid;

			// ATTENZIONE: c.getRilevamenti().get(giorno-1) assume che siano presenti TUTTI
			// i giorni nel database
			// Se vi fossero dei giorni mancanti (e nel nostro DB ce ne sono!), allora il
			// giorno 'giorno-1' potrebbe
			// non corrispondere al dato giusto!
		}

		// a cui sommo 100 * numero di volte in cui cambio citt�
		for (int giorno = 2; giorno <= NUMERO_GIORNI_TOTALI; giorno++) {
			if (!parziale.get(giorno - 1).equals(parziale.get(giorno - 2))) {
				costo += COST;
			}
		}

		return costo;
	}



	private boolean aggiuntaValida(Citta prova, List<Citta> parziale) {

		// verifica giorni massimi
		int conta = 0;
		for (Citta precedente : parziale)
			if (precedente.equals(prova))
				conta++;
		if (conta >= NUMERO_GIORNI_CITTA_MAX)
			return false;

		// verifica giorni minimi
		if (parziale.size() == 0) // primo giorno
			return true;
		if (parziale.size() == 1 || parziale.size() == 2) { // secondo o terzo giorno: non posso cambiare
			return parziale.get(parziale.size() - 1).equals(prova);
		}
		if (parziale.get(parziale.size() - 1).equals(prova)) // giorni successivi, posso SEMPRE rimanere
			return true;
		// sto cambiando citta
		if (parziale.get(parziale.size() - 1).equals(parziale.get(parziale.size() - 2))
				&& parziale.get(parziale.size() - 2).equals(parziale.get(parziale.size() - 3)))
			return true;

		return false;
	}

}

package it.polito.tdp.meteo;

import java.net.URL;
import java.time.Month;
import java.time.format.TextStyle;
import java.util.Locale;
import java.util.ResourceBundle;

import it.polito.tdp.meteo.bean.Citta;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextArea;
import javafx.util.StringConverter;

public class MeteoController {
	Model model=new Model();

	@FXML
	private ResourceBundle resources;

	@FXML
	private URL location;

	@FXML
	private ChoiceBox<Month> boxMese;

	@FXML
	private Button btnCalcola;

	@FXML
	private Button btnUmidita;

	@FXML
	private TextArea txtResult;

	@FXML
	void doCalcolaSequenza(ActionEvent event) {
		if(btnCalcola.isArmed()) {
			txtResult.clear();
			for(Citta c:model.trovaSequenza(boxMese.getValue().getValue())) {
				txtResult.appendText(c.getNome()+"\n");
			}
		}

	}

	@FXML
	void doCalcolaUmidita(ActionEvent event) {
		if(btnUmidita.isArmed()) {
			txtResult.clear();
			for(Citta c:model.allCitta())
			  txtResult.appendText(String.format("Citta: %s    Umidita: %f\n",c.getNome(),model.getUmiditaMedia(boxMese.getValue().getValue(),c)));
		}

	}

	@FXML
	void initialize() {
		assert boxMese != null : "fx:id=\"boxMese\" was not injected: check your FXML file 'Meteo.fxml'.";
		assert btnCalcola != null : "fx:id=\"btnCalcola\" was not injected: check your FXML file 'Meteo.fxml'.";
		assert btnUmidita != null : "fx:id=\"btnUmidita\" was not injected: check your FXML file 'Meteo.fxml'.";
		assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'Meteo.fxml'.";
	    for(int mese=1;mese<=12;mese++)
		  boxMese.getItems().add(Month.of(mese));
	    
	    boxMese.setConverter(new StringConverter<Month>() {
			@Override
			public String toString(Month m) {
				return m.getDisplayName(TextStyle.FULL, Locale.ITALIAN) ;
			}
			
			@Override
			public Month fromString(String string) {
				return null;
			}
		});
	}

	public void setModel(Model model) {
		this.model = model;
	}
	
	
	
	
	

}

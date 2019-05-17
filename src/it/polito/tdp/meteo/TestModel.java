package it.polito.tdp.meteo;

public class TestModel {

	public static void main(String[] args) {

		Model m = new Model();
		
		System.out.println(m.getUmiditaMedia(12,m.allCitta().get(1)));
		
		System.out.println(m.trovaSequenza(5));
		
//		System.out.println(m.trovaSequenza(4));
	}

}

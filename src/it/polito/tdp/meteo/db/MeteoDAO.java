package it.polito.tdp.meteo.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import it.polito.tdp.meteo.bean.Citta;
import it.polito.tdp.meteo.bean.Rilevamento;

public class MeteoDAO {
	
	public List<Citta> getAllCitta(){
		final String sql = "SELECT DISTINCT localita FROM situazione ORDER BY localita";

		List<Citta> leCitta = new ArrayList<Citta>();

		try {
			Connection conn = DBConnect.getInstance().getConnection();
			PreparedStatement st = conn.prepareStatement(sql);

			ResultSet rs = st.executeQuery();

			while (rs.next()) {

				 leCitta.add(new Citta(rs.getString("Localita")));
				
			}

			conn.close();
			return leCitta;

		} catch (SQLException e) {

			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	public List<Rilevamento> getAllRilevamenti() {

		final String sql = "SELECT Localita, Data, Umidita FROM situazione ORDER BY data ASC";

		List<Rilevamento> rilevamenti = new ArrayList<Rilevamento>();

		try {
			Connection conn = DBConnect.getInstance().getConnection();
			PreparedStatement st = conn.prepareStatement(sql);

			ResultSet rs = st.executeQuery();

			while (rs.next()) {

				Rilevamento r = new Rilevamento(rs.getString("Localita"), rs.getDate("Data"), rs.getInt("Umidita"));
				rilevamenti.add(r);
			}

			conn.close();
			return rilevamenti;

		} catch (SQLException e) {

			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}
	
	public Double calcolaUmidita(int m,Citta c) {
		final String sql="SELECT AVG(umidita) as u FROM situazione WHERE MONTH(DATA)=? AND localita=?";
		Double umidita=0.0;
		
		try {
			Connection conn = DBConnect.getInstance().getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
            st.setInt(1, m);
            st.setString(2, c.getNome());
			ResultSet rs = st.executeQuery();
			while(rs.next()) {
				umidita=rs.getDouble("u");
			}
			conn.close();
			return umidita;
            
		}
		catch(SQLException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	public List<Rilevamento> getAllRilevamentiLocalitaMese(int mese, String localita) {
		
		final String sql = "SELECT Localita, Data, Umidita FROM situazione  WHERE Localita=? AND MONTH(DATA)=? ORDER BY data ASC";

		List<Rilevamento> rilevamenti = new ArrayList<Rilevamento>();

		try {
			Connection conn = DBConnect.getInstance().getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
            st.setString(1,localita);
            st.setInt(2, mese);
			ResultSet rs = st.executeQuery();

			while (rs.next()) {

				Rilevamento r = new Rilevamento(rs.getString("Localita"), rs.getDate("Data"), rs.getInt("Umidita"));
				rilevamenti.add(r);
			}

			conn.close();
			return rilevamenti;

		} catch (SQLException e) {

			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	public Double getAvgRilevamentiLocalitaMese(int mese, String localita) {
		final String sql = "SELECT AVG(Umidita) as u FROM situazione WHERE Localita=? AND MONTH(DATA)=?";

		Double avg=0.0;

		try {
			Connection conn = DBConnect.getInstance().getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
            st.setString(1,localita);
            st.setInt(2, mese);
			ResultSet rs = st.executeQuery();

			while (rs.next()) {
               avg=rs.getDouble("u");
			}

			conn.close();
			return avg;

		} catch (SQLException e) {

			e.printStackTrace();
			throw new RuntimeException(e);
		}
		
	}

}

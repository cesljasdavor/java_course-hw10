package hr.fer.zemris.java.gui.layouts;

import java.awt.Container;

/**
 * Razred koji predstavlja strukturu podataka za razred {@link CalcLayout}.
 * Razred je nepromijenjiv i sadrži dvije privatne članske varijable koje
 * predstavljaju redak i stupac. Razred se koristi kao ograničenje (engl
 * contraint) prilikom poziva metode
 * {@link Container#add(java.awt.Component, Object)}, ukoliko je tom primjerku
 * razreda {@link Container} postavljen upravljač razmještaja na primjerak
 * razreda {@link CalcLayout}.
 * 
 * @see CalcLayout
 * @see Container
 * 
 * @author Davor Češljaš
 */
public class RCPosition {

	/** Članska varijabla koja predstavlja redak */
	private final int row;

	/** Članska varijabla koja predstavlja stupac */
	private final int column;

	/**
	 * Konstruktor koji inicijalizira primjerak ovog razreda. Unutar
	 * konstrukotra se članske varijable postavljaju na predane argumente.
	 * <b>row</b> i <b>column</b>.
	 *
	 * @param row
	 *            redak koji treba postaviti u člansku varijablu
	 * @param column
	 *            stupac koji treba postaviti u člansku varijablu
	 */
	public RCPosition(int row, int column) {
		this.row = row;
		this.column = column;
	}

	/**
	 * Metoda koja dohvaća vrijednost redka
	 *
	 * @return vrijednost redka
	 */
	public int getRow() {
		return row;
	}

	/**
	 * Metoda koja dohvaća vrijednost stupca
	 *
	 * @return vrijednost stupca
	 */
	public int getColumn() {
		return column;
	}

	/**
	 * Statička metoda koja iz predanog parametra koji je primjerak razreda
	 * {@link String} <b>input</b> parsira novi primjerak razreda
	 * {@link RCPosition}. Kako bi ovo bilo moguće predani parametar mora biti
	 * formata: <b><i>"row,column"</i></b>
	 *
	 * @param input
	 *            parametar koji se parsira u novi primjerak razreda
	 *            {@link RCPosition}
	 * @return novi primjerak razreda {@link RCPosition} parsiran iz
	 *         <b>input</b>
	 * 
	 * @throws NumberFormatException
	 *             ukoliko se predani parametar ne može parsirati u točno dva
	 *             primjerka razreda {@link Integer}
	 * @throws ArrayIndexOutOfBoundsException
	 *             ukoliko se preda pogrešan format ulaznog parametra
	 */
	public static RCPosition parse(String input) {
		input = input.trim();
		String[] coordinates = input.split(",");
		return new RCPosition(Integer.parseInt(coordinates[0].trim()), Integer.parseInt(coordinates[1].trim()));
	}

	@Override
	public String toString() {
		return String.format("(%d, %d)", row, column);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + column;
		result = prime * result + row;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		RCPosition other = (RCPosition) obj;
		if (column != other.column)
			return false;
		if (row != other.row)
			return false;
		return true;
	}

}

package hr.fer.zemris.java.gui.charts;

import java.util.ArrayList;
import java.util.List;

/**
 * Razred koji predstavlja strukturu podataka koja se sastoji od x vrijednosti i
 * y vrijednosti grafa. Razred je nepromijenjiv. Ova struktura podataka koristi
 * se kao strukutura podataka za xy vrijednosti unutar razreda {@link BarChart}.
 * 
 * @see BarChart
 * 
 * @author Davor Češljaš
 */
public class XYValue {

	/** Članska varijabla koja predstavlja x vrijednost */
	private final int x;

	/** Članska varijabla koja predstavlja y vrijednost */
	private final int y;

	/**
	 * Konstruktor koji inicijalizira primjerak ovog razreda. Konstrukutor
	 * postavlja člansku varijablu {@link #x} na predanu vrijednost <b>x</b> i
	 * člansku varijablu {@link #y} na vrijednost <b>y</b>
	 *
	 * @param x
	 *            vrijednost x koordinate
	 * @param y
	 *            vrijednost y koordinate
	 */
	public XYValue(int x, int y) {
		this.x = x;
		this.y = y;
	}

	/**
	 * Metoda koja dohvaća vrijednost x koordinate
	 *
	 * @return vrijednost x koordinate
	 */
	public int getX() {
		return x;
	}

	/**
	 * Metoda koja dohvaća vrijednost y koordinate
	 *
	 * @return vrijednost y koordinate
	 */
	public int getY() {
		return y;
	}

	/**
	 * Statička metoda koja parsira novi primjerak razreda {@link XYValue} iz
	 * predanog niza znakova <b>input</b>. Format niza znakova mora biti:
	 * <b><i>"x,y"</b></i>
	 *
	 * @param input
	 *            niz znakova koji se parsira
	 * @return novi primjerak razreda {@link XYValue} parsiran iz <b>input</b>
	 */
	public static XYValue parse(String input) {
		input = input.trim();
		String[] coordinates = input.split(",");
		if (coordinates.length != 2) {
			throw new IllegalArgumentException(
					"Predali stve prevelik broj koordinata. Tražio sam 2 dobio sam" + coordinates.length);
		}
		return new XYValue(Integer.parseInt(coordinates[0].trim()), Integer.parseInt(coordinates[1].trim()));
	}

	/**
	 * Statička metoda koja parsira seriju vrijednosti u {@link List} primjeraka
	 * razreda {@link XYValue}. Vrijednosti moraju biti u formatu: <b><i>"x1,y1
	 * x2,y2 ,..., xn,yn"</b></i> (dakle odvojedni sa točno jednim razmakom)
	 *
	 * @param input
	 *            niz znakova koji se parsira
	 * @return {@link List} primjeraka razreda {@link XYValue} dobiven
	 *         parsiranjem <b>input</b>
	 */
	public static List<XYValue> parseValues(String input) {
		input = input.trim();
		String[] values = input.split(" ");

		List<XYValue> xyValues = new ArrayList<>(values.length);
		for (String value : values) {
			xyValues.add(parse(value));
		}

		return xyValues;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + x;
		result = prime * result + y;
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
		XYValue other = (XYValue) obj;
		if (x != other.x)
			return false;
		if (y != other.y)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "XYValue [x=" + x + ", y=" + y + "]";
	}
}

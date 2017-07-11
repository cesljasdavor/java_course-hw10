package hr.fer.zemris.java.gui.charts;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Objects;

/**
 * Razred koji predstavlja model unutar
 * <a href = "https://en.wikipedia.org/wiki/Model–view–controller">MVC(Model -
 * View -Controller)</a> oblikovnog obrasca. Ovaj model koristi komponenta
 * {@link BarChartComponent} te preko podataka spremljenih unutar primjerka ovog
 * razreda crta stupičasti dijagram.
 * 
 * @see BarChartComponent
 * 
 * @author Davor Češljaš
 */
public class BarChart {

	/**
	 * Konstanta koja se koristi prilikom parsiranja novog primjerka razreda
	 * {@link BarChart} iz datoteke koristeći metodu
	 * {@link #parseFromFile(Path)}. Konstanta predstavlja redak u kojem se
	 * nalazi opis uz x-os
	 */
	private static final int X_DESCRIPTION_INDEX = 0;

	/**
	 * Konstanta koja se koristi prilikom parsiranja novog primjerka razreda
	 * {@link BarChart} iz datoteke koristeći metodu
	 * {@link #parseFromFile(Path)}. Konstanta predstavlja redak u kojem se
	 * nalazi opis uz <-os
	 */
	private static final int Y_DESCRIPTION_INDEX = 1;

	/**
	 * Konstanta koja se koristi prilikom parsiranja novog primjerka razreda
	 * {@link BarChart} iz datoteke koristeći metodu
	 * {@link #parseFromFile(Path)}. Konstanta predstavlja redak u kojem se
	 * nalaze vrijednosti xy
	 */
	private static final int VALUES_INDEX = 2;

	/**
	 * Konstanta koja se koristi prilikom parsiranja novog primjerka razreda
	 * {@link BarChart} iz datoteke koristeći metodu
	 * {@link #parseFromFile(Path)}. Konstanta predstavlja redak u kojem se
	 * nalazi vrijednost minimalne y koordinate
	 */
	private static final int Y_MIN_INDEX = 3;

	/**
	 * Konstanta koja se koristi prilikom parsiranja novog primjerka razreda
	 * {@link BarChart} iz datoteke koristeći metodu
	 * {@link #parseFromFile(Path)}. Konstanta predstavlja redak u kojem se
	 * nalazi vrijednost maksimalne y koordinate
	 */
	private static final int Y_MAX_INDEX = 4;

	/**
	 * Konstanta koja se koristi prilikom parsiranja novog primjerka razreda
	 * {@link BarChart} iz datoteke koristeći metodu
	 * {@link #parseFromFile(Path)}. Konstanta predstavlja redak u kojem se
	 * nalazi vrijednost razmaka za koji se podiže y
	 */
	private static final int Y_STEP_INDEX = 5;

	/**
	 * Članska varijabla koja predstavlja {@link List} svih primjeraka razreda
	 * {@link XYValue} iz kojih se crta graf
	 */
	private final List<XYValue> xyValues;

	/** Članska varijabla koja predstavlja opis uz x-os */
	private final String xDescription;

	/** Članska varijabla koja predstavlja opis uz y-os */
	private final String yDescription;

	/** Članska varijabla koja predstavlja minimalnu y koordinatu */
	private final int yMin;

	/** Članska varijabla koja predstavlja maksimalnu y koordinatu */
	private int yMax;

	/**
	 * Članska varijabla koja predstavlja razmak za koji se podiže y koordinata
	 */
	private final int yStep;

	/**
	 * Konstruktor koji inicijalizira primjerak ovog razreda. Unutar
	 * konstruktora provjeravaju se i pohranjuju predane vrijednosti u pripadne
	 * članske varijable
	 *
	 * @param xyValues
	 *            {@link List} svih primjeraka razreda {@link XYValue} iz kojih
	 *            se crta graf
	 * @param xDescription
	 *            opis uz x-os
	 * @param yDescription
	 *            opis uz y-os
	 * @param yMin
	 *            minimalna y koordinata
	 * @param yMax
	 *            maksimalna y koordinata
	 * @param yStep
	 *            razmak za koji se povećava y koordinata
	 * 
	 * @throws NullPointerException
	 *             ukoliko je bilo koji od predanih parametara (a koji je
	 *             objekt) <code>null</code>
	 */
	public BarChart(List<XYValue> xyValues, String xDescription, String yDescription, int yMin, int yMax, int yStep) {
		this.xyValues = Objects.requireNonNull(xyValues, "Lista xy vrijednosti ne smije biti null");
		this.xDescription = Objects.requireNonNull(xDescription, "Opis uz x-os mora postajati");
		this.yDescription = Objects.requireNonNull(yDescription, "Opis uz y-os mora postojati");
		this.yMin = yMin;
		this.yMax = yMax;
		this.yStep = yStep;
	}

	/**
	 * Metoda koja dohvaća {@link List} svih primjeraka razreda {@link XYValue}
	 * iz kojih se crta graf
	 *
	 * @return {@link List} svih primjeraka razreda {@link XYValue} iz kojih se
	 *         crta graf
	 */
	public List<XYValue> getXyValues() {
		return xyValues;
	}

	/**
	 * Metoda koja dohvaća opis uz x-os
	 *
	 * @return opis uz x-os
	 */
	public String getxDescription() {
		return xDescription;
	}

	/**
	 * Metoda koja dohvaća opis uz y-os
	 *
	 * @return opis uz y-os
	 */
	public String getyDescription() {
		return yDescription;
	}

	/**
	 * Metoda koja dohvaća minimalnu y koordinatu
	 *
	 * @return minimalnu y koordinatu
	 */
	public int getyMin() {
		return yMin;
	}

	/**
	 * Metoda koja dohvaća maksimalnu y koordinatu
	 *
	 * @return maksimalnu y koordinatu
	 */
	public int getyMax() {
		return yMax;
	}

	/**
	 * Metoda koja postavlja maksimalnu y koordinatu na predanu vrijednost
	 * <b>yMax</b>
	 *
	 * @param yMax
	 *            vrijednost na koju se postavlja <b>yMax</b>
	 */
	public void setyMax(int yMax) {
		this.yMax = yMax;
	}

	/**
	 * Metoda koja dohvaća razmak za koji se povećava y koordinata
	 *
	 * @return razmak za koji se povećava y koordinata
	 */
	public int getyStep() {
		return yStep;
	}

	/**
	 * Statička metoda koja parsira datoteku predstavljenu primjerkom razreda
	 * koji implementira sučelje {@link Path} <b>filePath</b> te iz nje stvara
	 * novi primjerak razreda {@link BarChart}.
	 *
	 * @param filePath
	 *            putanja do datoteke iz koje se parsira novi primjerak razreda
	 *            {@link BarChart}
	 * @return novi primjerak razreda {@link BarChart} parsiran iz datoteke s
	 *         putanjom <b>filePath</b>
	 * @throws IOException
	 *             ukoliko nije moguće pročitati sadržaj datoteke (ili nemate
	 *             ovlasti ili ona ne postoji)
	 */
	public static BarChart parseFromFile(Path filePath) throws IOException {
		List<String> lines = Files.readAllLines(filePath);

		// moguć IllegalArgumentException
		List<XYValue> xyValues = XYValue.parseValues(lines.get(VALUES_INDEX));
		return new BarChart(xyValues, lines.get(X_DESCRIPTION_INDEX), lines.get(Y_DESCRIPTION_INDEX),
				Integer.parseInt(lines.get(Y_MIN_INDEX)), Integer.parseInt(lines.get(Y_MAX_INDEX)),
				Integer.parseInt(lines.get(Y_STEP_INDEX)));
	}
}

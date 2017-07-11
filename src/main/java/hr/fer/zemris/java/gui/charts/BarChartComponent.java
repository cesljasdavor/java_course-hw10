package hr.fer.zemris.java.gui.charts;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.geom.AffineTransform;
import java.util.Objects;

import javax.swing.BorderFactory;
import javax.swing.JComponent;

/**
 * Razred koji nasljeđuje razred {@link JComponent} i nadjačava njegovu metodu
 * {@link JComponent#paintComponent(Graphics)}. Kao model ovog razreda koristi
 * se {@link BarChart}. Komponenta se sastoji od samog dijagrama , y-osi, njoj
 * pripadnog opisa i pripadnih vrijednosti uz tu os, te od x- osi, njoj
 * pripadnog opisa i pripadnih vrijednosti uz tu os.
 * 
 * @see BarChart
 * @see JComponent
 * 
 * @author Davor Češljaš
 */
public class BarChartComponent extends JComponent {

	/** Defaultna konstanta za serijalizaciju */
	private static final long serialVersionUID = 1L;

	/** Konstanta koja predstavlja fiksni razmak */
	private static final int FIX_DISTANCE = 10;

	/**
	 * Konstanta koja predstavlja primjerak razreda {@link Color} za
	 * transparentno narančastu boju
	 */
	private static final Color TRANSPARENT_ORANGE = new Color(255, 119, 0, 50);

	/**
	 * Konstanta koja predstavlja primjerak razreda {@link Color} za narančastu
	 * boju
	 */
	private static final Color ORANGE = new Color(255, 119, 0);

	/**
	 * Članska varijabla koja predstavlja model podataka koji se koristi za
	 * iscrtavanje ove komponente
	 */
	private BarChart barChart;

	/**
	 * Članska varijabla koja predstavlja dimenzije samog stupičastog dijagrama
	 */
	private Dimension chartDimension;

	/** Članska varijabla koja predstavlja točku ishodišta dijagrama */
	private Point origin;

	/**
	 * Konstruktor koji inicijalizira primjerak ovog razreda. Unutar
	 * konstruktora ukoliko <b>barChart</b> parametar nije <code>null</code>
	 * pohranjuje se u pripadnu člansku varijablu. Pomoću <b>barChart</b> crta
	 * se dijagram pozivom metode {@link #paintComponent(Graphics)}
	 *
	 * @param barChart
	 *            model podataka koji se koristi za iscrtavanje ove komponente
	 */
	public BarChartComponent(BarChart barChart) {
		this.barChart = Objects.requireNonNull(barChart, "Model stupičastog dijagrama ne smije biti null!");
		setBorder(BorderFactory.createEmptyBorder(FIX_DISTANCE, FIX_DISTANCE, FIX_DISTANCE, FIX_DISTANCE));

		modifyYMax();
	}

	/**
	 * Pomoćna metoda koja modificira yMax od {@link #barChart} tako da ymax
	 * -yMin dijeli razmak (yStep)
	 */
	private void modifyYMax() {
		int yMin = this.barChart.getyMin();
		int yMax = this.barChart.getyMax();
		int yStep = this.barChart.getyStep();
		if ((yMax - yMin) % yStep == 0) {
			return;
		}

		barChart.setyMax(yMin + (int) Math.ceil(((double) yMax - yMin) / yStep) * yStep);
	}

	@Override
	protected void paintComponent(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		calculateOriginPoint(g2);
		calculateChartDimension(g2);

		AxisInfo xInfo = createXAxisData(g2);
		AxisInfo yInfo = createYAxisData(g2);

		drawDiagram(g2, xInfo, yInfo);
	}

	/**
	 * Pomoćna metoda koja crta x-os, opis uz x-os i pripadne vrijednosti uz
	 * x-os. Metoda vraća novi primjerak razreda {@link AxisInfo} sa izračunatim
	 * vrijednostima pomoću kojeg se u konačnici crta dijagram
	 *
	 * @param g2
	 *            primjerak razreda {@link Graphics2D} pomoću kojeg se crta po
	 *            površini komponente
	 * @return novi primjerak razreda {@link AxisInfo} sa izračunatim
	 *         vrijednostima pomoću kojeg se u konačnici crta dijagram
	 */
	private AxisInfo createXAxisData(Graphics2D g2) {
		Dimension dim = getSize();
		Insets insets = getInsets();
		FontMetrics fm = g2.getFontMetrics();

		// varijabla koja se pomiče kako crtamo, na kraju treba biti origin.y
		int currentY = dim.height - insets.bottom - fm.getDescent();
		g2.setColor(Color.BLACK);
		g2.drawString(barChart.getxDescription(),
				origin.x + chartDimension.width / 2 - fm.stringWidth(barChart.getxDescription()) / 2, currentY);

		currentY -= fm.getAscent() + FIX_DISTANCE;

		final int numberOfValues = barChart.getXyValues().size();
		// -FIX_DISTANCE jer je linije za pola s lijeva i pola s desna duža
		int stepXInPixels = (chartDimension.width - FIX_DISTANCE) / numberOfValues;
		final int difference = chartDimension.width - FIX_DISTANCE - stepXInPixels * numberOfValues;
		// sačuvaj pa vrati
		Color defaultColor = g2.getColor();
		int fontHeight = fm.getHeight();
		for (int x = 0, len = barChart.getXyValues().size(); x <= len; x++) {
			int xInPixels = origin.x + x * stepXInPixels + (x < difference ? x : difference);
			// crtanje donjih brojki
			int nextXInPixels = xInPixels + stepXInPixels;
			if (x != len) {
				String number = String.valueOf(barChart.getXyValues().get(x).getX());

				g2.setColor(Color.BLACK);
				g2.drawString(number, xInPixels + (nextXInPixels - xInPixels) / 2 - fm.stringWidth(number) / 2,
						currentY);
			}

			int yMin = currentY - fontHeight - FIX_DISTANCE / 2;

			if (x == 0) {
				// ostavi crnu boju
				g2.drawPolygon(createArrow(xInPixels, yMin - chartDimension.height, FIX_DISTANCE / 4, true));
			} else {
				// namjesti boju - narančasta skoro prozirna
				g2.setColor(TRANSPARENT_ORANGE);
			}
			g2.drawLine(xInPixels, yMin, xInPixels, yMin - chartDimension.height);
		}
		// resetiraj na početnu boju!
		g2.setColor(defaultColor);
		// vrati izračunate podatke
		return new AxisInfo(stepXInPixels, difference);
	}

	/**
	 * Pomoćna metoda koja crta y-os, opis uz y-os i pripadne vrijednosti uz
	 * y-os. Metoda vraća novi primjerak razreda {@link AxisInfo} sa izračunatim
	 * vrijednostima pomoću kojeg se u konačnici crta dijagram
	 *
	 * @param g2
	 *            primjerak razreda {@link Graphics2D} pomoću kojeg se crta po
	 *            površini komponente
	 * @return novi primjerak razreda {@link AxisInfo} sa izračunatim
	 *         vrijednostima pomoću kojeg se u konačnici crta dijagram
	 */
	private AxisInfo createYAxisData(Graphics2D g2) {
		Dimension dim = getSize();
		Insets insets = getInsets();
		FontMetrics fm = g2.getFontMetrics();

		AffineTransform defaultAt = g2.getTransform();
		// Afina transformacija za opis y osi
		AffineTransform at = new AffineTransform();
		at.rotate(-Math.PI / 2);

		g2.setTransform(at);

		int currentX = insets.left + fm.getAscent();
		int txtWidth = fm.stringWidth(barChart.getyDescription());
		// koordinate se obrću x = -y y = x + x ne radi s width nego s height
		g2.drawString(barChart.getyDescription(),
				-((dim.height - insets.top - insets.bottom) / 2 + insets.top + txtWidth / 2), currentX);

		g2.setTransform(defaultAt);

		// ascent + descent = hight
		currentX += FIX_DISTANCE + fm.getDescent() + findMaxNumberWidth(fm);

		int yMin = barChart.getyMin();
		int yStep = barChart.getyStep();
		final int numberOfSteps = (barChart.getyMax() - yMin) / yStep;

		int stepYInPixels = (chartDimension.height - FIX_DISTANCE) / numberOfSteps;
		final int difference = chartDimension.height - FIX_DISTANCE - stepYInPixels * numberOfSteps;

		// sačuvaj pa vrati
		Color defaultColor = g2.getColor();
		// crtanje gornjih brojki i linija
		for (int y = 0; y <= numberOfSteps; y++) {
			int yInPixels = origin.y - y * stepYInPixels - (y < difference ? y : difference);

			// brojke uz y-os
			String number = String.valueOf(yMin + y * yStep);
			g2.setColor(Color.BLACK);
			g2.drawString(number, currentX - fm.stringWidth(number), yInPixels + fm.getHeight() / 2 - fm.getDescent());

			int xMin = origin.x - FIX_DISTANCE / 2;
			if (y == 0) {
				// ostavi crnu boju
				g2.drawPolygon(createArrow(xMin + chartDimension.width, yInPixels, FIX_DISTANCE / 4, false));
			} else {
				// namjesti boju - narančasta skoro prozirna
				g2.setColor(TRANSPARENT_ORANGE);
			}
			g2.drawLine(xMin, yInPixels, xMin + chartDimension.width, yInPixels);
		}

		// resetiraj na početnu boju!
		g2.setColor(defaultColor);
		// vrati izračunate parametre
		return new AxisInfo(stepYInPixels, difference);
	}

	/**
	 * Pomoćna metoda koja pomoću predanog parametra <b>fm</b> računa najveću
	 * duljinu od svih mogućih brojki u pikselima i vraća ju kroz povratnu
	 * vrijednost
	 *
	 * @param fm
	 *            primjerak razreda {@link FontMetrics} pomoću kojeg se
	 *            izračunavaju vrijednosti duljina primjeraka razreda
	 *            {@link String} u pikselima
	 * @return najveću duljinu od svih mogućih brojki u pikselima
	 */
	private int findMaxNumberWidth(FontMetrics fm) {
		// najveće duljine je ili najmanji ili najveći broj
		return Math.max(fm.stringWidth(String.valueOf(barChart.getyMax())),
				fm.stringWidth(String.valueOf(barChart.getyMin())));
	}

	/**
	 * Pomoćna metoda koja crta strelicu sa vrhom u koordinatama (<b>x</b>,
	 * <b>y</b>) koje su predane kao parametar. Parametar <b>diff</b>
	 * predstavlja polovicu baze trokuta. Parametar <b>isRotated</b> određuje
	 * smjer. Ukoliko je <b>isRotated</b> <code>false</code> smjer je s lijeva
	 * na desno, a ukoliko je <code>true</code> smjer je odozdo prema gore.
	 * Metoda vraća primjerak razreda {@link Polygon} koji predstavlja strelicu
	 *
	 * @param x
	 *            x koordinata vrha trokuta
	 * @param y
	 *            y koordinata vrha trokuta
	 * @param diff
	 *            polovica baze trokuta
	 * @param isRotated
	 *            određuje smjer. Ukoliko je <b>isRotated</b> <code>false</code>
	 *            smjer je s lijeva na desno, a ukoliko je <code>true</code>
	 *            smjer je odozdo prema gore
	 * @return primjerak razreda {@link Polygon} izgrađen iz predanih parametara
	 */
	private Polygon createArrow(int x, int y, int diff, boolean isRotated) {
		int[] xCoordinates = null;
		int[] yCoordinates = null;
		if (isRotated) {
			xCoordinates = new int[] { x, x + diff, x - diff };
			yCoordinates = new int[] { y, y + diff, y + diff };
		} else {
			xCoordinates = new int[] { x, x - diff, x - diff };
			yCoordinates = new int[] { y, y + diff, y - diff };
		}
		return new Polygon(xCoordinates, yCoordinates, xCoordinates.length);
	}

	/**
	 * Pomoćna metoda koja računa visinu i širinu dijagrama te ju postavlja u
	 * člansku varijablu {@link #chartDimension}
	 *
	 * @param g2
	 *            primjerak razreda {@link Graphics2D} pomoću kojeg se crta po
	 *            površini komponente
	 */
	private void calculateChartDimension(Graphics2D g2) {
		Dimension dim = getSize();
		Insets insets = getInsets();
		chartDimension = new Dimension(dim.width - origin.x + FIX_DISTANCE / 2 - insets.right,
				origin.y + FIX_DISTANCE / 2 - insets.top);
	}

	/**
	 * Metoda koja računa ishodišnu točku grafa te modificira člansku varijablu
	 * {@link #origin}
	 *
	 * @param g2
	 *            primjerak razreda {@link Graphics2D} pomoću kojeg se crta po
	 *            površini komponente
	 */
	private void calculateOriginPoint(Graphics2D g2) {
		FontMetrics fm = g2.getFontMetrics();
		int fontHeight = fm.getHeight();
		Insets insets = getInsets();
		Dimension dim = getSize();

		origin = new Point(fontHeight + findMaxNumberWidth(fm) + 2 * FIX_DISTANCE + insets.left,
				dim.height - 2 * fontHeight - 2 * FIX_DISTANCE - insets.bottom);
	}

	/**
	 * Pomoćna metoda koja crta dijagram pomoću predanih parametara.
	 *
	 * @param g2
	 *            primjerak razreda {@link Graphics2D} pomoću kojeg se crta po
	 *            površini komponente
	 * @param xInfo
	 *            primjerak privatnog razreda {@link AxisInfo} koji sadrži
	 *            detalje o x-osi
	 * @param yInfo
	 *            primjerak privatnog razreda {@link AxisInfo} koji sadrži
	 *            detalje o y-osi
	 */
	private void drawDiagram(Graphics2D g2, AxisInfo xInfo, AxisInfo yInfo) {
		int yMin = barChart.getyMin();
		int yStep = barChart.getyStep();
		// x ne mora nužno ići od 1 do nekog broja, ovo je dijagram
		int x = 0;
		for (XYValue xyValue : barChart.getXyValues()) {
			int yDistance = xyValue.getY() - yMin;
			int y = (yDistance) / yStep;

			int xInPixels = origin.x + x * xInfo.stepInPixels + (x < xInfo.difference ? x : xInfo.difference);
			int nextXInPixels = xInPixels + xInfo.stepInPixels;
			x++;

			int yInPixels = origin.y - y * yInfo.stepInPixels - (y < yInfo.difference ? y : yInfo.difference);
			// modifikacija zbog toga što y - yMin ne dijeli yStep
			int diffInStep = yDistance % yStep;
			yInPixels -= diffInStep == 0 ? 0 : (yInfo.stepInPixels / yStep) * diffInStep;

			g2.setColor(ORANGE);
			g2.fillRect(xInPixels + 1, yInPixels, nextXInPixels - xInPixels, origin.y - yInPixels);
			g2.setColor(Color.WHITE);
			g2.drawLine(nextXInPixels, yInPixels, nextXInPixels, origin.y);

		}
	}

	/**
	 * Privatni statički razred koji predstavlju strukuturu podataka koja nudi
	 * određene informacije o osi grafa. Konkretno to su korak u pikselima i
	 * razlika nastala zbog cjelobrojnog dijeljenja osi na određeni broj
	 * dijelova. Primjerke ovog razreda generiraju metode
	 * {@link BarChartComponent#createXAxisData(Graphics2D)} i
	 * {@link BarChartComponent#createYAxisData(Graphics2D)}
	 * 
	 * @author Davor Češljaš
	 */
	private static class AxisInfo {

		/** Članska varijabla koja predstavlja korak u pikselima. */
		final int stepInPixels;

		/**
		 * Članska varijabla koja predstavlja razliku nastalu zbog cjelobrojnog
		 * dijeljenja osi na određeni broj dijelova
		 */
		final int difference;

		/**
		 * Konstruktor koji inicijalizira primjerak ovog razreda. Konstruktor
		 * predane parametre interno sprema u pripadne članske varijable
		 *
		 * @param stepInPixels
		 *            korak u pikselima.
		 * @param difference
		 *            razlika nastala zbog cjelobrojnog dijeljenja osi na
		 *            određeni broj dijelova
		 */
		public AxisInfo(int stepInPixels, int difference) {
			this.stepInPixels = stepInPixels;
			this.difference = difference;
		}
	}
}

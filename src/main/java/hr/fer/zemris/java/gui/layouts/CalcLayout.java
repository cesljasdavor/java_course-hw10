package hr.fer.zemris.java.gui.layouts;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.LayoutManager2;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * Razred koji implementira sučelje {@link LayoutManager2} i sve njegove metode.
 * Razred predstavlja upravljač razmještaja koji stvara mrežu komponenti. Razred
 * može sadržavati ukupno {@value #NUMBER_OF_COMPONENTS} komponenti i stvara
 * mrežu od {@value #NUMBER_OF_ROWS} redaka i {@value #NUMBER_OF_COLUMNS}
 * stupaca. Kao ograničenje za ovaj razred može se predati primjerak razreda
 * {@link RCPosition} ili primjerak razreda {@link String} propisan formatom
 * koji je naveden ovdje {@link RCPosition#parse(String)}. Legalni su svi redci
 * u rasponu (1-{@value #NUMBER_OF_ROWS}) i stupci u rasponu
 * (1-{@value #NUMBER_OF_COLUMNS}) uz iznimku prvog redka gdje su stupci (2-5)
 * zabranjeni. Komponenta sa položajem (1,1) razvlači se kroz navedene stupce.
 * 
 * @see LayoutManager2
 * @see RCPosition
 * @see RCPosition#parse(String)
 * 
 * @author Davor Češljaš
 */
public class CalcLayout implements LayoutManager2 {

	/**
	 * Konstanta koja predstavlja ukupan broj komponenti koji ovaj upravljač
	 * razmještaja može prikazati
	 */
	private static final int NUMBER_OF_COMPONENTS = 31;

	/** Konstanta koja predstavlja broj redaka ovog razmještaja */
	private static final int NUMBER_OF_ROWS = 5;

	/** Konstanta koja predstavlja broj stupaca ovog razmještaja */
	private static final int NUMBER_OF_COLUMNS = 7;

	/** Konstanta koja predstavlja početnu poziciju od koje kreće razmještaj */
	private static final int FIRST = 1;

	/**
	 * Konstanta koja predstavlja faktor s kojim se skalira širina komponente na
	 * poziciji (1,1)
	 */
	private static final int SCALE = 5;

	/**
	 * Konstanta koja predstavlja primjerak razreda {@link RCPosition} koji je
	 * specijalno raspoređen naspram ostalih komponenti
	 */
	private static final RCPosition SPECIAL_POSITION = new RCPosition(1, 1);

	/** Članska varijabla koja predstavlja razmak između svake komponente */
	private int gap;

	/**
	 * {@link Map} koji mapira pozicije oblikovane razredom {@link RCPosition}
	 * na pripadne komponente predstavljene razredom {@link Component}
	 */
	private Map<RCPosition, Component> positions;

	/**
	 * Konstruktor koji inicijalizira primjerak ovog razreda. Konstuktor poziva
	 * konstruktor {@link #CalcLayout(int)} pri čemu mu za <b>gap</b> predaje 0.
	 */
	public CalcLayout() {
		this(0);
	}

	/**
	 * Konstruktor koji inicijalizira primjerak ovog razreda. Konstrukor
	 * namješta pripadnoj članskoj varijabli koja određuje razmak predanu
	 * vrijednost <b>gap</b>.
	 *
	 * @param gap
	 *            željeni razmak između komponenata
	 * 
	 * @throws IllegalArgumentException
	 *             ukoliko se kao parametar <b>gap</b> preda vrijednost manja od
	 *             0
	 */
	public CalcLayout(int gap) {
		if (gap < 0) {
			throw new IllegalArgumentException(
					"Razmak između redaka i stupaca mora biti pozitivan cijeli broj. Vi ste predali: " + gap);
		}
		this.gap = gap;
		this.positions = new HashMap<>(NUMBER_OF_COMPONENTS);
	}

	@Override
	public void addLayoutComponent(String name, Component comp) {
		// predpostavka da je name = contraint u formatu "row, column"
		// iako ovo se nikada ne bi trebalo pozvati jer smo LayoutManager2
		addLayoutComponent(comp, name);
	}

	@Override
	public void removeLayoutComponent(Component comp) {
		RCPosition position = findPosition(comp);
		if (position != null) {
			positions.remove(position);
		}
	}

	/**
	 * Pomoćna metoda koja za predanu komponentu predstavljenu razredom
	 * {@link Component} <b>comp</b> traži pripadni joj ključ unutar
	 * {@link #positions}. Ključ je modeliran razredom {@link RCPosition}
	 *
	 * @param comp
	 *            primjerak razreda {@link Component} čiji se ključ traži unutar
	 *            {@link #positions}
	 * @return pronađeni ključ za predani parametar ili <code>null</code>
	 *         ukoliko taj ključ ne postoji
	 */
	private RCPosition findPosition(Component comp) {
		for (Map.Entry<RCPosition, Component> entry : positions.entrySet()) {
			if (entry.getValue().equals(comp)) {
				return entry.getKey();
			}
		}
		return null;
	}

	@Override
	public Dimension preferredLayoutSize(Container parent) {
		return calculateDimension(parent, Component::getPreferredSize);
	}

	@Override
	public Dimension minimumLayoutSize(Container parent) {
		return calculateDimension(parent, Component::getMinimumSize);
	}

	@Override
	public Dimension maximumLayoutSize(Container target) {
		return calculateDimension(target, Component::getMaximumSize);
	}

	/**
	 * Pomoćna metoda koja se koristi za izračun određene dimenzije ovog
	 * razmještaja (time i roditeljske komponente koja kojoj je primjerak ovog
	 * razreda postavljen kao upravljač razreda), ovisno o predanoj strategiji
	 * <b>dimensionGetter</b>. Metoda se poziva kroz metode
	 * {@link #preferredLayoutSize(Container)},
	 * {@link #minimumLayoutSize(Container)} i
	 * {@link #maximumLayoutSize(Container)}. kako se ne bi multiplicirao isti
	 * kod.
	 *
	 * @param parent
	 *            komponenta čija se dimenzija (koja ovisi o predanoj
	 *            strategiji) računa
	 * @param dimensionGetter
	 *            strategija koja računa neku dimenziju za svu djecu komponente
	 *            <b>parent</b>
	 * @return primjerak razreda {@link Dimension} koji predstavlja izračunatu
	 *         dimenziju
	 */
	private Dimension calculateDimension(Container parent, Function<Component, Dimension> dimensionGetter) {
		// pronalazak maksimalnih dimenzija
		Dimension dim = findMaximalDimension(parent, dimensionGetter);

		Insets parentInsets = parent.getInsets();

		dim.width = NUMBER_OF_COLUMNS * dim.width + (NUMBER_OF_COLUMNS - 1) * gap 
				+ parentInsets.left + parentInsets.right;

		dim.height = NUMBER_OF_ROWS * dim.height + (NUMBER_OF_ROWS - 1) * gap 
				+ parentInsets.top + parentInsets.bottom;

		return dim;
	}

	/**
	 * Pomoćna metoda koja računa najveću dimenziju koja se dobiva pozivom
	 * {@link Function#apply(Object)} za svako dijete ove komponente i vraća je
	 * kao povratnu vrijednost.
	 *
	 * @param parent
	 *            primjerak razreda {@link Container} čija se maksimalna širina
	 *            i visina djeteta računa
	 * @param dimensionGetter
	 *            strategija koja računa neku dimenziju za svu djecu komponente
	 *            <b>parent</b>
	 * @return primjerak razreda {@link Dimension} koji ima širinu najšireg
	 *         djeteta parametra <b>parent</b> i visinu najvišeg djeteta istog
	 *         parametra.
	 */
	private Dimension findMaximalDimension(Container parent, Function<Component, Dimension> dimensionGetter) {
		Dimension dim = new Dimension(0, 0);
		for (Map.Entry<RCPosition, Component> entry : positions.entrySet()) {
			if (entry.getKey().equals(SPECIAL_POSITION)) {
				continue;
			}

			Dimension compDim = dimensionGetter.apply(entry.getValue());

			if (compDim == null) {
				continue;
			}
			dim.width = Math.max(dim.width, compDim.width);
			dim.height = Math.max(dim.height, compDim.height);
		}

		return dim;
	}

	@Override
	public void layoutContainer(Container parent) {
		Dimension dimensionLeft = getDimensionLeft(parent);
		// nemam gdje crtati
		if (dimensionLeft.width == 0 || dimensionLeft.height == 0) {
			return;
		}

		Dimension cDimension = new Dimension(dimensionLeft.width / NUMBER_OF_COLUMNS,
				dimensionLeft.height / NUMBER_OF_ROWS);

		// razlika između podijeljenih vrijednosti i koliko prostora imam za
		// crtati
		final int widthDiff = dimensionLeft.width - cDimension.width * NUMBER_OF_COLUMNS;
		final int heightDiff = dimensionLeft.height - cDimension.height * NUMBER_OF_ROWS;

		Insets parentInsets = parent.getInsets();
		for (Map.Entry<RCPosition, Component> entry : positions.entrySet()) {
			RCPosition position = entry.getKey();
			int row = position.getRow();
			int column = position.getColumn();

			int x = (column - 1) * (cDimension.width + gap);
			// modifikacija x-a
			if (column != FIRST) {
				x += column < widthDiff ? column : widthDiff;
			}

			int y = (row - 1) * (cDimension.height + gap);
			// modifikacija y-a
			if (row != FIRST) {
				y += row < heightDiff ? row : heightDiff;
			}

			int cWidth = 0;
			if (entry.getKey().equals(SPECIAL_POSITION)) {
				cWidth = scaleWidth(cDimension.width, SCALE, widthDiff);
			} else {
				cWidth = cDimension.width + (column <= widthDiff ? 1 : 0);
			}
			entry.getValue().setBounds(x + parentInsets.left, y + parentInsets.top, cWidth,
					cDimension.height + (row <= heightDiff ? 1 : 0));
		}
	}

	/**
	 * Pomoćna metoda koja skalira vrijednost predanu kroz parametar
	 * <b>cWidth</b> sa parametrom <b>scale</b> i toj vrijednosti nadodaje
	 * razliku u širini komponente nastalu zbog cjelobrojnog dijeljenja.
	 *
	 * @param cWidth
	 *            the c width
	 * @param scale
	 *            faktor s kojim se skalira
	 * @param widthDiff
	 *            vrijednost razlike u širini komponente nastale zbog
	 *            cjelobrojnog dijeljenja.
	 * @return the int
	 */
	private int scaleWidth(int cWidth, int scale, int widthDiff) {
		int width = (scale - 1) * gap;
		for (int i = 1; i <= scale; i++) {
			width += cWidth + (i <= widthDiff ? 1 : 0);
		}
		return width;
	}

	/**
	 * Pomoćna metoda koja računa koliko je širine i visine preostalo za crtanje
	 * samih komponenata kada se oduzmu umetci sa svih strana i sve potrebne
	 * udaljenosti između komponenata. Vrijednosti nikada neće biti manje od 0
	 *
	 * @param parent
	 *            primjerak razreda {@link Container} čiji se ostatak širine i
	 *            visine računa
	 * @return primjerak razreda {@link Dimension} u koji su spremljene
	 *         izračunate vrijednosti
	 */
	private Dimension getDimensionLeft(Container parent) {
		Insets parentInsets = parent.getInsets();
		Dimension dimension = new Dimension(parent.getWidth(), parent.getHeight());

		dimension.width -= parentInsets.left + parentInsets.right + (NUMBER_OF_COLUMNS - 1) * gap;
		dimension.width = Math.max(0, dimension.width);

		dimension.height -= parentInsets.top + parentInsets.bottom + (NUMBER_OF_ROWS - 1) * gap;
		dimension.height = Math.max(0, dimension.height);

		return dimension;
	}

	@Override
	public void addLayoutComponent(Component comp, Object constraints) {
		if (constraints instanceof String) {
			// moguć NumberFormatException
			constraints = RCPosition.parse((String) constraints);
		}

		// moguć ClassCastException
		RCPosition position = (RCPosition) constraints;
		checkArguments(comp, position);

		positions.put(position, comp);
	}

	/**
	 * Pomoćna metoda koja provjerava ispravnost predanih parametara <b>comp</b>
	 * i <b>position</b>. Argumenti nisu ispravni ukoliko se <b>comp</b> već
	 * nalazi u {@link Map} {@link #positions} ili već postoji ključ
	 * <b>position</b> u istoj mapi ili je metoda
	 * {@link #isLegalPosition(RCPosition)} vratila <code>false</code>
	 *
	 * @param comp
	 *            primjerak razreda {@link Component} čija se ispravnost ocjenjuje
	 * @param position
	 *            primjerak razreda {@link RCPosition} čija se ispravnost ocjenjuje
	 * @throws IllegalArgumentException ukoliko se dogodio bilo koji od gornjih scenarija
	 */
	private void checkArguments(Component comp, RCPosition position) {
		if (positions.containsValue(comp)) {
			throw new IllegalArgumentException("Komponenta je već dodana.");
		}
		if (!isLegalPosition(position)) {
			throw new IllegalArgumentException(String.format("Ograničenje %s nije podržano", position.toString()));
		}
		if (positions.containsKey(position)) {
			throw new IllegalArgumentException("Već postoji komponenta s ograničenje" + position);
		}
	}

	/**
	 * Pomoćna metoda koja provjerava je li parametar <b>position</b>
	 * ispravno zadan. Ispravne pozicijie navedene su u dokumentaciji
	 * ovog razreda.
	 * 
	 * @see CalcLayout
	 *
	 * @param postition
	 *            primjerak razreda {@link RCPosition} čija se ispravnost ocjenjuje
	 * @return <code>true</code> ukoliko je predani parametar ispravan,
	 * 			<code>false</code> inače
	 */
	private boolean isLegalPosition(RCPosition postition) {
		int row = postition.getRow();
		int column = postition.getColumn();
		if (row < 1 || row > NUMBER_OF_ROWS || column < 1 || column > NUMBER_OF_COLUMNS) {
			return false;
		}
		if (row == 1 && (column >= 2 && column <= 5)) {
			return false;
		}
		return true;
	}

	@Override
	public float getLayoutAlignmentX(Container target) {
		return 0;
	}

	@Override
	public float getLayoutAlignmentY(Container target) {
		return 0;
	}

	@Override
	public void invalidateLayout(Container target) {
		// nemam cashiranih stvari
	}

}

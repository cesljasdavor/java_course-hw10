package hr.fer.zemris.java.gui.charts;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

/**
 * Razred koji predstavlja demonstracijski program koji demonstrira rad s swing
 * komponentom {@link BarChartComponent} i njezinim modelom koji je primjerak
 * razreda {@link BarChart}. na početku programa se iz naredbenog redka čita
 * putanja do datoteke te se kroz metodu {@link BarChart#parseFromFile(Path)}
 * parsira novi model {@link BarChart}. Potom se model iscrtava unutar prozora.
 * Razred ujedino nasljeđuje razred {@link JFrame} kako bi se komponenta mogla
 * prikazati na Vašem ekranu.
 * 
 * @author Davor Češljaš
 */
public class BarChartDemo extends JFrame {

	/** Defaultna konstanta za serijalizaciju */
	private static final long serialVersionUID = 1L;

	/**
	 * Konstrukotr koji inicijalizira primjerak ovog razreda. Unutra
	 * konstruktora inicijaliziraju se dimenzije prozora, namješta naslov i
	 * postavlja operacija zatvaranja prozora na
	 * {@link WindowConstants#DISPOSE_ON_CLOSE}. Parametar <b>barChart</b>
	 * koristi se kao model za primjerak razreda {@link BarChartComponent}, a
	 * parametar <b>filePath</b> kao tekst koji se upisuje u label modeliranu
	 * razredom {@link JLabel}
	 *
	 *
	 * @param barChart
	 *            model koji se koristi kao model za primjerak razreda
	 *            {@link BarChartComponent}
	 * @param filePath
	 *            primjerak razreda koji implementira sučelje {@link Path} koji
	 *            se koristi za ispis apsolutne putanje u primjerku razreda
	 *            {@link JLabel}
	 */
	public BarChartDemo(BarChart barChart, Path filePath) {
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		setTitle("Bar Chart Demo");
		setSize(650, 498);
		setLocationRelativeTo(null);

		initGUI(barChart, filePath);
	}

	/**
	 * Pomoćna metoda koja inicijalizira grafičko korisničko sučelje ovog
	 * prozora. Unutar ove metode stvara se novi primjerak razreda
	 * {@link BarChartComponent} sa parametrom <b>barChart</b> te se za tekst
	 * koji {@link JLabel} postavlja ono što se dobije pozivom
	 * {@link Path#toAbsolutePath()} nad <b>filePath</b>
	 *
	 * @param barChart
	 *            model koji se koristi kao model za primjerak razreda
	 *            {@link BarChartComponent}
	 * @param filePath
	 *            primjerak razreda koji implementira sučelje {@link Path} koji
	 *            se koristi za ispis apsolutne putanje u primjerku razreda
	 *            {@link JLabel}
	 */
	private void initGUI(BarChart barChart, Path filePath) {
		Container cp = getContentPane();
		cp.setLayout(new BorderLayout());

		JLabel pathLabel = new JLabel(filePath.toAbsolutePath().toString());
		pathLabel.setHorizontalAlignment(SwingConstants.CENTER);
		pathLabel.setBackground(Color.BLUE);
		pathLabel.setForeground(Color.WHITE);
		pathLabel.setOpaque(true);
		cp.add(pathLabel, BorderLayout.NORTH);

		// ako je barChart null -> NullPointerException
		BarChartComponent bcComp = new BarChartComponent(barChart);
		cp.add(bcComp, BorderLayout.CENTER);
	}

	/**
	 * Metoda od koje započinje izvođenje ovog programa.
	 *
	 * @param args
	 *            argumenti naredbenog redka. Za ovaj program mora postojati
	 *            točno jedan argument, koji predstavlja relativnu ili apsolutnu
	 *            putanju do datoteke koja se parsira metodom
	 *            {@link BarChart#parseFromFile(Path)}
	 */
	public static void main(String[] args) {
		if (args.length != 1) {
			throw new IllegalArgumentException(
					"Predali ste pogrešan broj argumenata. Tražio sam 1 dobio sam " + args.length);
		}

		Path filePath = Paths.get(args[0]);
		try {
			BarChart barChart = BarChart.parseFromFile(filePath);
			SwingUtilities.invokeLater(() -> new BarChartDemo(barChart, filePath).setVisible(true));
		} catch (IOException e) {
			throw new IllegalArgumentException("Ne mogu otvoriti datoteku: " + filePath);
		}
	}
}

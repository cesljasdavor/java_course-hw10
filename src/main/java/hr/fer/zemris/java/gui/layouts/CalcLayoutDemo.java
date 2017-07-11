package hr.fer.zemris.java.gui.layouts;

import java.awt.Container;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

/**
 * Razred koji predstavlja demonstracijski program koji demonstrira rad s
 * upravljačem razmještaja {@link CalcLayout}. Razred ujedino nasljeđuje razred
 * {@link JFrame} kako bi se program mogao prikazati na ekranu
 * 
 * @see CalcLayout
 * @see JFrame
 * 
 * @author Davor Češljaš
 */
public class CalcLayoutDemo extends JFrame {

	/** Defaultna konstanta za serijalizaciju */
	private static final long serialVersionUID = 1L;

	/**
	 * Konstrukotr koji inicijalizira primjerak ovog razreda. Unutra
	 * konstruktora inicijaliziraju se dimenzije prozora, namješta naslov i
	 * postavlja operacija zatvaranja prozora na
	 * {@link WindowConstants#DISPOSE_ON_CLOSE}
	 */
	public CalcLayoutDemo() {
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		setTitle("Calc Layout Demo");
		setSize(700, 500);
		setLocationRelativeTo(null);

		initGUI();
	}

	/**
	 * Pomoćna metoda koja inicijalizira grafičko korisničko sučelje ovog
	 * prozora. Unutar ove metode namješta se upravljač razmještaja
	 * {@link CalcLayout} i nekoliko demonstracijskih komponenti koje su
	 * modelirane razredom {@link JLabel}
	 */
	private void initGUI() {
		// primjer iz zadatka - JPanel zamijenjen sa Container
		Container cp = getContentPane();
		cp.setLayout(new CalcLayout(3));
		cp.add(new JLabel("x"), "1,1");
		cp.add(new JLabel("y"), "2,3");
		cp.add(new JLabel("z"), "2,7");
		cp.add(new JLabel("w"), "4,2");
		cp.add(new JLabel("a"), "4,5");
		cp.add(new JLabel("b"), "4,7");
	}

	/**
	 * Metoda od koje započinje izvođenje ovog programa.
	 *
	 * @param args
	 *            argumenti naredbenog redka. Ovdje se ne koriste.
	 */
	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> new CalcLayoutDemo().setVisible(true));
	}
}

package hr.fer.zemris.java.gui.prim;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.GridLayout;
import java.awt.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

/**
 * Razred koji predstavlja demonstracijski primjer koji demonstrira rad sa
 * primjerkom razreda {@link PrimListModel}. Pokretanjem ovog program stvaraju
 * se dva primjerka razreda {@link List} kojima se predaje isti model koji je
 * primjerak razreda {@link PrimListModel}. Klikom na gumb "Sljedeći" u listama
 * će se pojaviti novi prim-broj. Razred ujedino nasljeđuje {@link JFrame} kako
 * bi se rezultat mogao prikazati u prozoru
 * 
 * @see PrimListModel
 * @see List
 * @see JFrame
 * 
 * @author Davor Češljaš
 */
public class PrimDemo extends JFrame {

	/** Defaultna konstanta za serijalizaciju */
	private static final long serialVersionUID = 1L;

	/**
	 * Konstrukor koji inicijalizira primjerak ovog razreda. Unutra konstruktora
	 * inicijaliziraju se dimenzije prozora, namješta naslov i postavlja
	 * operacija zatvaranja prozora na {@link WindowConstants#DISPOSE_ON_CLOSE}
	 */
	public PrimDemo() {
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		setTitle("Prime demo");
		setSize(600, 400);
		setLocationRelativeTo(null);

		initGUI();
	}

	/**
	 * Pomoćna metoda koja inicijalizira grafičko korisničko sučelje ovog
	 * prozora. Unutar metode stvara se primjerak razreda {@link PrimListModel}
	 * koji se predaje dvama primjeraka razreda {@link List}. Također se unutar
	 * metode dodaje i jedan gumbe predtavljen razredom {@link JButton}
	 */
	private void initGUI() {
		Container cp = getContentPane();
		cp.setLayout(new BorderLayout());

		PrimListModel model = new PrimListModel();

		JPanel listPanel = new JPanel(new GridLayout(2, 1));
		cp.add(listPanel, BorderLayout.CENTER);

		JList<Integer> topList = new JList<>(model);
		listPanel.add(new JScrollPane(topList));

		JList<Integer> bottomList = new JList<>(model);
		listPanel.add(new JScrollPane(bottomList));

		JButton nextButton = new JButton("Sljedeći");
		nextButton.addActionListener(e -> model.next());
		cp.add(nextButton, BorderLayout.SOUTH);
	}

	/**
	 * Metoda od koje započinje izvođenje ovog programa.
	 *
	 * @param args
	 *            argumenti naredbenog redka. Ovdje se ne koriste.
	 */
	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> new PrimDemo().setVisible(true));
	}
}

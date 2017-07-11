package hr.fer.zemris.java.gui.calc;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.function.BinaryOperator;

import javax.swing.JCheckBox;

/**
 * Razred koji implementira sučelje {@link ActionListener}. Ovaj razred koristi
 * se za slušanje na pritisak gumba koji predstavlja binarni operator unutar
 * programa {@link Calculator}. Razred sadrži i privatni podrazred
 * {@link SecondArgumentListener} koji sluša na drugi argument. Drugi argument
 * prihvatit će se kada se pritisne na neki od gumba unutar
 * {@link Calculator#operators}. Razred sadrži dvije strategije
 * {@link BinaryOperator} koje se koriste ovisno o tome je li {@link JCheckBox}
 * {@link Calculator#inverse} označen ili nije. Ukoliko
 * {@link Calculator#inverse} ne invertira operaciju kao <b>inverseOperation</b>
 * treba predati <b>mainOperation</b>.
 * 
 * @see Calculator
 * 
 * @author Davor Češljaš
 */
public class BinaryOperatorListener implements ActionListener {

	/**
	 * Članska varijabla koja predstavlja strategiju koja se koristi ukoliko
	 * {@link Calculator#inverse} nije označen.
	 */
	private BinaryOperator<Double> mainOperation;

	/**
	 * Članska varijabla koja predstavlja strategiju koja se koristi ukoliko je
	 * {@link Calculator#inverse} označen.
	 */
	private BinaryOperator<Double> inverseOperation;

	/**
	 * Članska varijabla koja predstavlja referencu na primjerak razreda
	 * {@link Calculator} unutar kojeg rade gumbi koje ovaj primjerak razreda
	 * {@link BinaryOperatorListener} sluša
	 */
	private Calculator calculator;

	/**
	 * Konstruktor koji inicijalizira primjerak ovog razreda. U konstruktor se
	 * predaju dvije strategije. Strategija <b>mainOperation</b> koristi se za
	 * izračun rezultata ukoliko {@link Calculator#inverse} nije označen.
	 * Strategija <b>inverseOperation</b> koristi se ukoliko je
	 * {@link Calculator#inverse} označen. Ukoliko korisnik ne želi da se glavna
	 * operacija invertira, savjetuje se da korisnik preda referencu na isti
	 * primjerak razreda koji implementira sučelje {@link BinaryOperator} i za
	 * <b>mainOperation</b> i za <b>inverseOperation</b>
	 *
	 * @param mainOperation
	 *            glavna strategija, koja se koristi kada
	 *            {@link Calculator#inverse} nije označen
	 * @param inverseOperation
	 *            inverzna strategija, koja se koristi kada je
	 *            {@link Calculator#inverse} označen
	 * @param calculator
	 *            referencu na primjerak razreda {@link Calculator} unutar kojeg
	 *            rade gumbi koje ovaj primjerak razreda
	 *            {@link BinaryOperatorListener} sluša
	 */
	public BinaryOperatorListener(BinaryOperator<Double> mainOperation, BinaryOperator<Double> inverseOperation,
			Calculator calculator) {
		this.mainOperation = mainOperation;
		this.inverseOperation = inverseOperation;
		this.calculator = calculator;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (calculator.subResult == null) {
			try {
				calculator.subResult = Double.parseDouble(calculator.screen.getText());
			} catch (NumberFormatException nfe) {
				calculator.setNaN();
				return;
			}
		}
		calculator.subResultShowing = true;

		ActionListener l = new SecondArgumentListener();

		calculator.operators.forEach(operator -> operator.addActionListener(l));
	}

	/**
	 * Pomoćni razred koji se koristi za slušanje na drugi argument. Drugi
	 * argument prihvatit će se kada se pritisne na neki od gumba unutar
	 * {@link Calculator#operators}. Unutar metode
	 * {@link #actionPerformed(ActionEvent)} nakon primitka drugog argumenta
	 * primjerak ovog razreda će sam sebe odjaviti od svih gumbi unutar
	 * {@link Calculator#operators}
	 * 
	 * @see Calculator
	 * 
	 * @author Davor Češljaš
	 */
	private class SecondArgumentListener implements ActionListener {

		/** Kada se pozove, računa i ispisuje rezultat na ekran */
		@Override
		public void actionPerformed(ActionEvent e) {
			calculator.operators.forEach(operator -> operator.removeActionListener(this));
			// korisnik je možda stisnuo "res"
			if (calculator.subResult == null) {
				return;
			}

			calculator.subResult = calculator.inverse.isSelected()
					? inverseOperation.apply(calculator.subResult, Double.parseDouble(calculator.screen.getText()))
					: mainOperation.apply(calculator.subResult, Double.parseDouble(calculator.screen.getText()));

			calculator.screen.setText(calculator.subResult.toString());
		}

	}
}

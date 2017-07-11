package hr.fer.zemris.java.gui.calc;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.function.UnaryOperator;

import javax.swing.JCheckBox;


/**
 * Razred koji implementira sučelje {@link ActionListener}. Ovaj razred koristi
 * se za slušanje na pritisak gumba koji predstavlja unarni operator unutar
 * programa {@link Calculator}.Razred sadrži dvije strategije
 * {@link UnaryOperator} koje se koriste ovisno o tome je li {@link JCheckBox}
 * {@link Calculator#inverse} označen ili nije. Ukoliko
 * {@link Calculator#inverse} ne invertira operaciju kao <b>inverseOperation</b>
 * treba predati <b>mainOperation</b>.
 * 
 * @see Calculator
 * 
 * @author Davor Češljaš
 */
public class UnaryOperatorListener implements ActionListener{
	
	/**
	 * Članska varijabla koja predstavlja strategiju koja se koristi ukoliko
	 * {@link Calculator#inverse} nije označen.
	 */
	private UnaryOperator<Double> mainOperation;
	
	/**
	 * Članska varijabla koja predstavlja strategiju koja se koristi ukoliko je
	 * {@link Calculator#inverse} označen.
	 */
	private UnaryOperator<Double> inverseOperation;
	
	/**
	 * Članska varijabla koja predstavlja referencu na primjerak razreda
	 * {@link Calculator} unutar kojeg rade gumbi koje ovaj primjerak razreda
	 * {@link UnaryOperatorListener} sluša
	 */
	private Calculator calculator;
	
	
	
	/**
	 * Konstruktor koji inicijalizira primjerak ovog razreda. U konstruktor se
	 * predaju dvije strategije. Strategija <b>mainOperation</b> koristi se za
	 * izračun rezultata ukoliko {@link Calculator#inverse} nije označen.
	 * Strategija <b>inverseOperation</b> koristi se ukoliko je
	 * {@link Calculator#inverse} označen. Ukoliko korisnik ne želi da se glavna
	 * operacija invertira, savjetuje se da korisnik preda referencu na isti
	 * primjerak razreda koji implementira sučelje {@link UnaryOperator} i za
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
	 *            {@link UnaryOperatorListener} sluša
	 */
	public UnaryOperatorListener(UnaryOperator<Double> mainOperation, UnaryOperator<Double> inverseOperation,
			Calculator calculator) {
		this.mainOperation = mainOperation;
		this.inverseOperation = inverseOperation;
		this.calculator = calculator;
	}
	
	/** Kada se pozove, računa i ispisuje rezultat na ekran */
	@Override
	public void actionPerformed(ActionEvent e) {
		double number;
		try {
			number = Double.parseDouble(calculator.screen.getText());			
		}catch (NumberFormatException nfe) {
			calculator.setNaN();
			return;
		}
		
		Double result = calculator.inverse.isSelected() ? 
				inverseOperation.apply(number): 
				mainOperation.apply(number);
		
		calculator.subResultShowing = true;
		calculator.screen.setText(result.toString());
	}
	
	
}

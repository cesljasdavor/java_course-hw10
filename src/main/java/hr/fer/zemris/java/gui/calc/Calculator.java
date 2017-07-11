package hr.fer.zemris.java.gui.calc;

import java.awt.Color;
import java.awt.Container;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.EmptyStackException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Stack;
import java.util.function.BinaryOperator;
import java.util.function.UnaryOperator;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

import hr.fer.zemris.java.gui.layouts.CalcLayout;
import hr.fer.zemris.java.gui.layouts.RCPosition;

/**
 * Razred koji predstavlja program koji ima funkcionalnost kalkulatora. Razred
 * nasljeđuje razred {@link JFrame} kako bi se sam program mogao pokrenuti u
 * zasebnom prozoru na ekranu. Ovaj kalkulator nudi niz binarnih i unarnih
 * operacija nad brojevima. U ovaj kalkulator brojke se upisuju isključivo
 * pritiskom na gumbe koji predstavljaju brojke i znakove "+/-" i ".".
 * Kalkulator nudi i operacije spremanja brojeva na stog. Ukoliko je
 * {@link JCheckBox#isSelected()} <code>true</code> tada sin, cos, tan, ctg
 * prelaze u arkus funkcije, log u 10^, ln u e^, x^n u n-ti korijen iz x. Za invertiranje 
 * i postavljanje operacija koriste se strategije {@link BinaryOperatorListener} i 
 * {@link UnaryOperatorListener}
 * Prozoru je upravljač razmještaja postavljen na primjerak razreda
 * {@link CalcLayout}
 * <p>
 * Napomena: U daljnjoj dokumentaciji metoda, riječ "ekran" referirat će se na
 * primjerak razreda {@link JLabel} koji je prikazan u prozoru (narančasto
 * polje).
 * </p>
 * 
 * @see CalcLayout
 * @see JFrame
 * 
 * @author Davor Češljaš
 */
public class Calculator extends JFrame {

	/** Defaultna konstanta za serijalizaciju */
	private static final long serialVersionUID = 1L;

	/** Konstanta koja predstavlja prazan niz znakova */
	private static final String EMPTY = "";

	// članske varijable koje se vide u stratrgijama u istom paketu
	/**
	 * Članska varijabla koja je primjerak razreda {@link JLabel}, a koja unutar
	 * ovog programa predstavlja ekran na kojem se ispisuju brojevi.
	 */
	JLabel screen;

	/**
	 * Članska varijabla koja je primjerak razreda {@link JCheckBox}, a koja
	 * govori s kojim setom operacija radimo. Ukoliko je
	 * {@link JCheckBox#isSelected()} <code>true</code> tada sin, cos, tan, ctg
	 * prelaze u arkus funkcije, log u 10^, ln u e^, x^n u n-ti korijen iz x.
	 */
	JCheckBox inverse;

	/**
	 * Članska varijabla koja predstavlja {@link Set} svih primjeraka razreda
	 * {@link JButton} koji predstavljaju binarne operacije i operatora "="
	 */
	Set<JButton> operators;

	/**
	 * Članska varijabla koja predstavlja zastavicu koja signalizira prikazuje
	 * li se trenutno na ekranu međurezultat
	 */
	boolean subResultShowing;

	/**
	 * Članska varijabla koja predstavlja koja predstavlja trenutni međurezultat
	 */
	Double subResult;

	/**
	 * Članska varijabla koja predstavlja sav prostor ovog prozora unutar kojeg
	 * je moguće razmještati komponente
	 */
	private Container cp;

	/**
	 * Članska varijabla koja predstavlja stog na koji se sprema, odnosno s
	 * kojeg se vade brojke pritiskom na "push" , odnosno "pop"
	 */
	private Stack<Double> stack;

	/**
	 * Konstrukor koji inicijalizira primjerak ovog razreda. Unutra konstruktora
	 * inicijaliziraju se dimenzije prozora, namješta naslov i postavlja
	 * operacija zatvaranja prozora na {@link WindowConstants#DISPOSE_ON_CLOSE}
	 */
	public Calculator() {
		stack = new Stack<>();
		operators = new HashSet<>();

		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		setTitle("Kalkulator");
		setSize(600, 400);
		setLocationRelativeTo(null);

		initGUI();
	}

	/**
	 * Pomoćna metoda koja na ekran ispusuje {@link Double#NaN}
	 */
	void setNaN() {
		screen.setText(String.valueOf(Double.NaN));
	}

	/**
	 * Pomoćna metoda koja inicijalizira grafičko korisničko sučelje ovog
	 * prozora. Unutar ove metoda namještaju se komponente koje postoje unutar
	 * kalkulatora.
	 */
	private void initGUI() {
		cp = getContentPane();
		cp.setLayout(new CalcLayout(10));

		screen = new JLabel();
		cp.add(screen, "1,1");
		screen.setBackground(Color.ORANGE);
		screen.setOpaque(true);
		screen.setHorizontalAlignment(SwingConstants.TRAILING);
		screen.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 15));
		screen.setFont(screen.getFont().deriveFont(Font.BOLD, 34.0f));

		setUpNumbers();

		inverse = new JCheckBox("Inv");
		cp.add(inverse, "5,7");
		setUpOperators();
	}

	/**
	 * Pomoćna metoda koja namješta operatore ovog kalkulatora
	 */
	private void setUpOperators() {
		setUpUnaryOperators();
		setUpBinaryOperators();

		JButton equals = new JButton("=");
		cp.add(equals, "1,6");
		operators.add(equals);
		equals.addActionListener(e -> {
			subResultShowing = true;
			// makni sve što si prije spremio, ali ipak ispiši na ekran
			subResult = null;
		});

		JButton clr = new JButton("clr");
		cp.add(clr, "1,7");
		clr.addActionListener(e -> clear());

		JButton res = new JButton("res");
		cp.add(res, "2,7");
		res.addActionListener(e -> {
			reset();
		});
	}

	/**
	 * Pomoćna metoda koja namješta unarne operatore ovog kalkulatora.
	 */
	private void setUpUnaryOperators() {
		JButton powMinusOne = new JButton("1/x");
		cp.add(powMinusOne, "2,1");
		UnaryOperator<Double> powMinusOneOperator = arg -> 1 / arg;
		powMinusOne.addActionListener(new UnaryOperatorListener(powMinusOneOperator, 
				powMinusOneOperator, this));

		JButton log = new JButton("log");
		cp.add(log, "3,1");
		UnaryOperator<Double> logOperator = Math::log10;
		UnaryOperator<Double> power10 = arg -> Math.pow(10, arg);
		log.addActionListener(new UnaryOperatorListener(logOperator, power10, this));

		JButton ln = new JButton("ln");
		cp.add(ln, "4,1");
		UnaryOperator<Double> lnOperator = Math::log;
		UnaryOperator<Double> powEOperator = Math::exp;
		ln.addActionListener(new UnaryOperatorListener(lnOperator, powEOperator, this));

		setUpTrigonometricalFunctions();
		setUpStackOperators();
	}

	/**
	 * Pomoćna metoda koja namješta operatore ovog kalkulatora koji rade sa
	 * stogom
	 */
	private void setUpStackOperators() {
		JButton push = new JButton("push");
		cp.add(push, "3,7");
		push.addActionListener(e -> {
			try {
				stack.push(Double.parseDouble(screen.getText()));
			} catch (NumberFormatException nfe) {
				setNaN();
			}
		});

		JButton pop = new JButton("pop");
		cp.add(pop, "4,7");
		pop.addActionListener(e -> {
			try {
				screen.setText(stack.pop().toString());
			} catch (EmptyStackException empty) {
				reset();
				setNaN();
				subResultShowing = true;
			}
		});
	}

	/**
	 * Pomoćna metoda koja namješta trigonometrijske funckije ovog kalkulatora
	 */
	private void setUpTrigonometricalFunctions() {
		JButton sin = new JButton("sin");
		cp.add(sin, "2,2");
		UnaryOperator<Double> sinFunction = Math::sin;
		UnaryOperator<Double> asinFunction = Math::asin;
		sin.addActionListener(new UnaryOperatorListener(sinFunction, asinFunction, this));

		JButton cos = new JButton("cos");
		cp.add(cos, "3,2");
		UnaryOperator<Double> cosFunction = Math::cos;
		UnaryOperator<Double> acosFunction = Math::acos;
		cos.addActionListener(new UnaryOperatorListener(cosFunction, acosFunction, this));

		JButton tan = new JButton("tan");
		cp.add(tan, "4,2");
		UnaryOperator<Double> tanFunction = Math::tan;
		UnaryOperator<Double> atanFunction = Math::atan;
		tan.addActionListener(new UnaryOperatorListener(tanFunction, atanFunction, this));

		JButton ctg = new JButton("ctg");
		cp.add(ctg, "5,2");
		UnaryOperator<Double> ctgFunction = arg -> 1 / tanFunction.apply(arg);
		UnaryOperator<Double> actgFunction = arg -> 1 / ctgFunction.apply(arg);
		ctg.addActionListener(new UnaryOperatorListener(ctgFunction, actgFunction, this));
	}

	/**
	 * Pomoćna metoda koja namješta binarne operatore ovog kalkulatora
	 */
	private void setUpBinaryOperators() {
		JButton plus = new JButton("+");
		cp.add(plus, "5,6");
		operators.add(plus);
		BinaryOperator<Double> plusOperator = (arg1, arg2) -> arg1 + arg2;
		plus.addActionListener(new BinaryOperatorListener(plusOperator, plusOperator, this));

		JButton minus = new JButton("-");
		cp.add(minus, "4,6");
		operators.add(minus);
		BinaryOperator<Double> minusOperator = (arg1, arg2) -> arg1 - arg2;
		minus.addActionListener(new BinaryOperatorListener(minusOperator, minusOperator, this));

		JButton multiplication = new JButton("*");
		cp.add(multiplication, "3,6");
		operators.add(multiplication);
		BinaryOperator<Double> multiplicationOperator = (arg1, arg2) -> arg1 * arg2;
		multiplication
				.addActionListener(new BinaryOperatorListener(multiplicationOperator, 
						multiplicationOperator, this));

		JButton division = new JButton("/");
		cp.add(division, "2,6");
		operators.add(division);
		BinaryOperator<Double> divisionOperator = (arg1, arg2) -> arg1 / arg2;
		division.addActionListener(new BinaryOperatorListener(divisionOperator, divisionOperator, this));

		JButton xPowN = new JButton("x^n");
		cp.add(xPowN, "5,1");
		operators.add(xPowN);
		BinaryOperator<Double> xPowNOperator = Math::pow;
		BinaryOperator<Double> xRootNOperator = (arg1, arg2) -> Math.pow(arg1, 1 / arg2);
		xPowN.addActionListener(new BinaryOperatorListener(xPowNOperator, xRootNOperator, this));
	}

	/**
	 * Pomoćna metoda koja namješta brojke i gumbe "+/-" i "." ovom kalkulatoru
	 */
	private void setUpNumbers() {
		List<JButton> buttonList = new ArrayList<>();
		for (int i = 0; i < 10; i++) {
			buttonList.add(new JButton(String.valueOf(i)));
		}
		// dodaj ga da mu postaviš poziciju
		JButton plusMinus = new JButton("+/-");
		buttonList.add(1, plusMinus);
		JButton dot = new JButton(".");
		buttonList.add(2, dot);

		for (int y = 5, counter = 0; y >= 2; y--) {
			for (int x = 3; x <= 5; x++) {
				cp.add(buttonList.get(counter++), new RCPosition(y, x));
			}
		}
		// makni ga da mu ne dodaš krivog listenera
		buttonList.remove(1);

		ActionListener numbersAndDotListener = e -> {
			JButton pressed = (JButton) e.getSource();
			screen.setText((subResultShowing ? EMPTY : screen.getText()) + pressed.getText());
			subResultShowing = false;
		};
		buttonList.forEach(button -> button.addActionListener(numbersAndDotListener));

		plusMinus.addActionListener(new ActionListener() {
			private boolean minus = true;

			@Override
			public void actionPerformed(ActionEvent e) {
				if (minus) {
					screen.setText("-" + screen.getText());
				} else {
					screen.setText(screen.getText().substring(1));
				}
				minus = !minus;
			}
		});
	}

	/**
	 * Pomoćna metoda koja resetira primjerak ovog razreda u početno stanje. Svi
	 * međurezultati biti će odbačeni, te će se stog isprazniti
	 */
	private void reset() {
		clear();
		inverse.setSelected(false);
		subResult = null;
		stack.clear();
	}

	/**
	 * Pomoćna metoda koja briše trenutni sadržaj ekrana.
	 */
	private void clear() {
		screen.setText(EMPTY);
		subResultShowing = false;
	}

	/**
	 * Metoda od koje započinje izvođenje ovog programa.
	 *
	 * @param args
	 *            argumenti naredbenog redka. Ovdje se ne koriste
	 */
	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> new Calculator().setVisible(true));
	}
}

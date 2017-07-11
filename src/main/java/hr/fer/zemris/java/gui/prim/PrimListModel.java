package hr.fer.zemris.java.gui.prim;

import java.util.ArrayList;
import java.util.List;

import javax.swing.ListModel;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;

/**
 * Razred koji implementira sučelje {@link ListModel}. Razred predstavlja model
 * {@link List}e prim brojeva. Primjerci ovog razreda pozivom metode
 * {@link #next()} računaju i internoj spremaju sljedeći prim broj. Svaka
 * promjena dojavljuje se svim zainteresiranim promatračima dodanima korištenjem
 * metode {@link #addListDataListener(ListDataListener)}. Korisnik se upučuje na
 * teoriju oblikovnih obrazaca
 * <a href = "https://en.wikipedia.org/wiki/Observer_pattern">promatrač</a> i
 * <a href = "https://en.wikipedia.org/wiki/Model–view–controller">MVC(Model -
 * View -Controller)</a>
 * 
 * @see ListModel
 * 
 * @author Davor Češljaš
 */
public class PrimListModel implements ListModel<Integer> {

	/**
	 * Članska varijabla koja predstavlja {@link List} do sada izračunatih prim
	 * brojeva
	 */
	private List<Integer> primes;

	/**
	 * Članska varijabla koja predstavlja {@link List} svih zainteresiranih
	 * promatrača
	 */
	private List<ListDataListener> listeners;

	/**
	 * Konstanta koja predstavlja prvi prim broj koji se kroz konstruktor sprema
	 * u {@link #primes} i postavlja u {@link #currentPrime}
	 */
	private static final Integer FIRST_PRIME = 1;

	/**
	 * Predstavlja trenutni prim broj koji je zadnji ubačen u {@link #primes}.
	 */
	private Integer currentPrime;

	/**
	 * Konstruktor koji inicijalizira primjerak ovog razreda. Unutar ovog
	 * konstrukotra stvaraju se sve interne strukture za spremanje
	 * zainteresiranih promatrača i prim-brojeva. Kroz ovaj konstruktor
	 * postavlja se prvi prim broj na {@value #FIRST_PRIME}
	 */
	public PrimListModel() {
		primes = new ArrayList<>();
		listeners = new ArrayList<>();

		primes.add(FIRST_PRIME);
		currentPrime = FIRST_PRIME;
	}

	@Override
	public int getSize() {
		return primes.size();
	}

	@Override
	public Integer getElementAt(int index) {
		return primes.get(index);
	}

	@Override
	public void addListDataListener(ListDataListener l) {
		listeners = new ArrayList<>(listeners);
		listeners.add(l);

	}

	@Override
	public void removeListDataListener(ListDataListener l) {
		listeners = new ArrayList<>(listeners);
		listeners.remove(l);
	}

	/**
	 * Metoda koja računa i interno sprema sljedeći prim-broj. Po završetku
	 * izračuna metoda dojavljuje svim zainteresiranim promatračima da je došlo
	 * do promjene u ovom modelu
	 */
	public void next() {
		addNextPrime();

		// dojavi promjenu
		int position = listeners.size();
		ListDataEvent event = new ListDataEvent(this, ListDataEvent.INTERVAL_ADDED, position, position);

		for (ListDataListener l : listeners) {
			l.intervalAdded(event);
		}
	}

	/**
	 * Pomoćna metoda koja se koristi za izračun sljedećeg prim broja. Metoda
	 * traži prim brojeve veće od {@link #currentPrime} i postavlja novu
	 * vrijednost {@link #currentPrime} na sljedeći prim broj. Metoda također u
	 * {@link List} prim brojeva {@link #primes} nadodaje izračunati sljedeći
	 * prim broj
	 */
	private void addNextPrime() {
		int currentNumber = currentPrime + 1;
		while (true) {
			boolean isPrime = true;

			for (int i = 2, len = (int) Math.sqrt(currentNumber); i <= len; i++) {
				if (currentNumber % i == 0) {
					isPrime = false;
					break;
				}
			}

			if (isPrime) {
				primes.add(currentNumber);
				currentPrime = currentNumber;
				return;
			}

			currentNumber++;
		}
	}

}

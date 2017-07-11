package hr.fer.zemris.java.gui.prim;

import static org.junit.Assert.*;

import org.junit.Test;

public class PrimListModelTest {

	@Test
	public void testiranjeKojiJePrviBrojUListi() throws Exception {
		PrimListModel model = new PrimListModel();
		assertEquals(model.getElementAt(0), Integer.valueOf(1));
	}
	
	@Test
	public void testiranje6PrimBroj() throws Exception {
		PrimListModel model = new PrimListModel();
		for(int i = 0; i < 5; i++) {
			model.next();
		}
		assertEquals(model.getSize(), 6);
		assertEquals(model.getElementAt(model.getSize() - 1), Integer.valueOf(11));
	}
	
	@Test
	public void testiranje11PrimBroj() throws Exception {
		PrimListModel model = new PrimListModel();
		for(int i = 0; i < 10; i++) {
			model.next();
		}
		assertEquals(model.getSize(), 11);
		assertEquals(model.getElementAt(model.getSize() - 1), Integer.valueOf(29));
	}
	
	@Test
	public void testiranje16PrimBroj() throws Exception {
		PrimListModel model = new PrimListModel();
		for(int i = 0; i < 15; i++) {
			model.next();
		}
		assertEquals(model.getSize(), 16);
		assertEquals(model.getElementAt(model.getSize() - 1), Integer.valueOf(47));
	}
	
	@Test
	public void testiranje169PrimBroj() throws Exception {
		PrimListModel model = new PrimListModel();
		for(int i = 0; i < 168; i++) {
			model.next();
		}
		assertEquals(model.getSize(), 169);
		assertEquals(model.getElementAt(model.getSize() - 1), Integer.valueOf(997));
	}
		
}

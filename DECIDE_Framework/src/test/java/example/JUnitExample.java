package example;

import static org.junit.Assert.assertEquals;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class JUnitExample {

	public JUnitExample() {
	}
	
	
    @Before
    public void beforeEachTest() {
        System.out.println("This is executed before each Test");
    }
    
    
    @After
    public void afterEachTest() {
        System.out.println("This is exceuted after each Test");
    }

    
   @Test
   public void justAnExample() {
	 assertEquals(5.5, 5.4, 0.1);
   }	
	   
}
	 
 

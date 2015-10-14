package decide.qv;
/**
 * class that represents a data structure for storing an RQVResult 
 * <b>this is application specific</b>
 * @author sgerasimou
 *
 */

public class ResultQV{
	int 	sensor1;
	int		sensor2;
	int		sensor3;
	double 	speed;
	double 	req1Result; // expected number of accurate measurements
	double 	req2Result; // expected power consumption
	
	
	/**
	 * Class constructor: create a new ResultQV instance
	 * @param CSC
	 * @param speed
	 * @param req1Result
	 * @param req2Result
	 */
	public ResultQV (int CSC, double speed, double req1Result, double req2Result){
		this.sensor1 	= CSC%2;
		this.sensor2 	= CSC%4>1 ? 1 : 0;
		this.sensor3 	= CSC%8>3 ? 1 : 0;
		this.speed   	= speed;
		this.req1Result	= req1Result;
		this.req2Result	= req2Result;
	}
	
	
	/**
	 * Print the details of this ResultQV instance
	 */
	@Override
	public String toString() {
		StringBuilder str = new StringBuilder("[");
	    String NEW_LINE = System.getProperty("line.separator");
	    str.append(sensor1 + ",");
	    str.append(sensor2 + ",");
	    str.append(sensor3 + ",");
	    str.append(speed   + "]");
	    str.append("\t[" + req1Result);
	    str.append(","   + req2Result +"]");
		return str.toString();
	}
	
	
	/**
	 * Get sensor 1 value
	 * @return
	 */
	public int getSensor1(){
		return sensor1;
	}
	
	
	/**
	 * Get sensor 2 value
	 * @return
	 */
	public int getSensor2(){
		return sensor2;
	}
	
	
	/**
	 * Get sensor 3 value
	 * @return
	 */
	public int getSensor3(){
		return sensor3;
	}

	
	/**
	 * Get UUV speed
	 * @return
	 */
	public double getSpeed(){
		return speed;
	}
	
	
	/**
	 * Get result for requirement 1
	 * @return
	 */
	public double getReq1Result(){
		return req1Result;
	}
	
	
	/**
	 * Get result for requirement 2
	 * @return
	 */
	public double getReq2Result(){
		return req2Result;
	}



}
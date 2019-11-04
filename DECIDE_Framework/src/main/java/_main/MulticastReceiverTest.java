package _main;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import auxiliary.Utility;
import caseStudies.uuv.UUVCLAReceipt;
import caseStudies.uuv.UUVCapabilitySummaryCollection;
import decide.capabilitySummary.CapabilitySummaryCollection;
import decide.receipt.CLAReceipt;
import network.ComponentTypeDECIDE;
import network.MulticastReceiver;
import network.ReceiverDECIDE;

public class MulticastReceiverTest {


	public static void main(String[] args) {
		
		//setup configuration file, it can also be provided as an argument
		String configurationFile = "resources" + File.separator + "uuv" +File.separator +"config.properties";
		Utility.setConfigurationFile(configurationFile);

		System.setProperty("java.net.preferIPv4Stack", "true");

		//create capability summary collection
		CapabilitySummaryCollection capabilitySummaryCollection = new UUVCapabilitySummaryCollection();

		//create cla receipt
		CLAReceipt claReceipt		= new UUVCLAReceipt(capabilitySummaryCollection);

		//dummy multicast receiver
		String peerAddress 	= "224.224.224.221";
		int peerPort		= 8881;
		MulticastReceiver mrTest = new MulticastReceiver(peerAddress, peerPort, ComponentTypeDECIDE.PEER);

		//receiver from other DECIDE components
		List<ReceiverDECIDE> peersList = new ArrayList<ReceiverDECIDE>();
		peersList.add(mrTest);
		

		claReceipt.setReceiversFromOtherDECIDEs(peersList);		
		
//		Thread t = new Thread(mrTest, mrTest.toString());
//		t.start();

	}

}

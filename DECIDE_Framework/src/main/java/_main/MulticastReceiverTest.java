package _main;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import auxiliary.Utility;
import caseStudies.uuvNew.UUVCLAReceiptNew;
import caseStudies.uuvNew.UUVCapabilitySummaryCollectionNew;
import decide.capabilitySummary.CapabilitySummaryCollectionNew;
import decide.receipt.CLAReceiptNew;
import network.MulticastReceiverNew;
import network.ReceiverDECIDENew;

public class MulticastReceiverTest {


	public static void main(String[] args) {
		
		//setup configuration file, it can also be provided as an argument
		String configurationFile = "resources" + File.separator + "uuv" +File.separator +"config.properties";
		Utility.setConfigurationFile(configurationFile);

		System.setProperty("java.net.preferIPv4Stack", "true");

		//create capability summary collection
		CapabilitySummaryCollectionNew capabilitySummaryCollection = new UUVCapabilitySummaryCollectionNew();

		//create cla receipt
		CLAReceiptNew claReceipt		= new UUVCLAReceiptNew(capabilitySummaryCollection);

		//dummy multicast receiver
		String peerAddress 	= "224.224.224.221";
		int peerPort		= 8881;
		MulticastReceiverNew mrTest = new MulticastReceiverNew(peerAddress, peerPort);

		//receiver from other DECIDE components
		List<ReceiverDECIDENew> peersList = new ArrayList<ReceiverDECIDENew>();
		peersList.add(mrTest);
		

		claReceipt.setReceiverFromOtherDECIDE(peersList);		
		
//		Thread t = new Thread(mrTest, mrTest.toString());
//		t.start();

	}

}

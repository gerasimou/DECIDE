package caseStudies.activityBot;

import org.apache.log4j.Logger;

import decide.StatusRobot;
import decide.StatusComponent;
import decide.receipt.CLAReceipt;
import decide.receipt.CLAReceiptHandler;
import network.ReceiverDECIDE;
import network.ReceiverDECIDENew;

public class ActivityBotCLAReceipt extends CLAReceipt{
	
	/** Logging system events*/
    final static Logger logger = Logger.getLogger(ActivityBotCLAReceipt.class);
		
		/**
		 * Class constructor
		 */
		public ActivityBotCLAReceipt() {
			super();
		}
		
		
		/**
		 * Class <b>copy</b> constructor
		 */
		private ActivityBotCLAReceipt(ActivityBotCLAReceipt instance) {
			super();
		}


		/**
		 * Execute this CLAReceipt handler
		 */
		@Override
		public boolean execute(Object...args){
			return this.receivedNewCapabilitySummary;
		}
		
		/**
		 * Maintains heartbeat trace of peer components
		 */
		@Override
		public boolean executeListeningThread()
		{
			// 0 is no change has been recorded, 1 is major change
			boolean change = false;
			long timestamp = 0;
			int alivecounter = 0;
		
			
			receivedTimeStamp = System.currentTimeMillis();
			
			for (ReceiverDECIDENew server : serversList){
				timestamp = server.getTimeStamp();
				if(timestamp > 0)
				{ 
					if(logger.isDebugEnabled())
						logger.debug("[PEER:"+server.getServerAddress()+",Current TS:"+
								receivedTimeStamp+",PreviousTS:"+timestamp+",Latency:"+(receivedTimeStamp-timestamp)+"]");
					else
						logger.info("[PEER:"+server.getServerAddress()+",Latency:"+(receivedTimeStamp-timestamp)+"]");
					
					alivecounter++;
					
					if((receivedTimeStamp - timestamp) > (TIME_WINDOW+20000))
					{
						alivecounter--;
						server.setTimeStamp(0);
						server.setAtomicPeerStatus(StatusComponent.MISSING);
						// Remove missing peer capability summary
						removeCapabilitySummary(server.getServerAddress());
						change = true;
					}
				}
				
				switch(server.getAtomicPeerStatus()) {
			    case NEW_JOIN:
			    	// its a Major change
			    	change =true;
			    	server.setAtomicPeerStatus(StatusComponent.ALIVE);
			    	break;
			    	
			    case CHANGE:
			    	// its a minor change
			    	
			    	change = true;
			    	server.setAtomicPeerStatus(StatusComponent.ALIVE);
			    	break;
			    	
			    	default: break;
			    	
			}
			}
				
			// its a major change, kill the thread and reset received flag after safe time window. 
			// Also interrupt the main thread
				if(change) 
			    	atomicOperationReference.set(StatusRobot.MAJOR_PEER_CHANGE);

		
			    	
			
				if(alivecounter ==0)
				{
					receivedNewCapabilitySummary = false;
					return true;
				}
				
			return false;
		}

		
		/**
		 * Clone the CLAReceipt handler
		 */
		@Override
		public CLAReceipt deepClone(Object ... args) {
			CLAReceipt newHandler = new ActivityBotCLAReceipt(this);
			return newHandler;
		}	
	}


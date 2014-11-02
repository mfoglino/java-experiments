package jobs;

import static models.DejavuStreamPrintMessage.containsEnoughData;

import org.slf4j.Logger;

import models.DejavuStreamPrintMessage;

public class DejavuPrintJobDummy {

	private final Logger dejavuPrintsToMlChannelFileWriter;
	

	
	public DejavuPrintJobDummy(Logger dejavuPrintsToMlChannelFileWriter) {
		super();
		this.dejavuPrintsToMlChannelFileWriter = dejavuPrintsToMlChannelFileWriter;
	}



	public void run(String message)
	{
		
		try {
			if (containsEnoughData(message)) {
				
				DejavuStreamPrintMessage dejavuStreamPrintMessage = new DejavuStreamPrintMessage(message);
				dejavuPrintsToMlChannelFileWriter.info(dejavuStreamPrintMessage.toJson().toString());
			}			
		} catch (Exception e) {
				//play.Logger.error("Error processing message.", e);
		}
	}
}

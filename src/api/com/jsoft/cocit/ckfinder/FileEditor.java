package com.jsoft.cocit.ckfinder;

import com.ckfinder.connector.configuration.Events;
import com.ckfinder.connector.configuration.Events.EventTypes;
import com.ckfinder.connector.configuration.Plugin;
import com.jsoft.cocimpl.ckfinder.SaveFileCommand;

public class FileEditor extends Plugin {

	@Override
	public void registerEventHandlers(Events events) {
		events.addEventHandler(EventTypes.BeforeExecuteCommand, SaveFileCommand.class);

	}

}

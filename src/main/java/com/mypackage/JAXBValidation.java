package com.mypackage;

import java.io.PrintWriter;
import java.io.StringWriter;

import javax.xml.bind.ValidationEvent;
import javax.xml.bind.ValidationEventHandler;

public class JAXBValidation implements ValidationEventHandler {

    public boolean handleEvent(ValidationEvent event) {
        EncryptionLog4JLogger.logger.error("\nEVENT"
        +"SEVERITY:  " + event.getSeverity()
        +"MESSAGE:  " + event.getMessage()
        +"LINKED EXCEPTION:  " + event.getLinkedException()
        +"LOCATOR"
        +"    LINE NUMBER:  " + event.getLocator().getLineNumber()
        +"    COLUMN NUMBER:  " + event.getLocator().getColumnNumber()
        +"    OFFSET:  " + event.getLocator().getOffset()
        +"    OBJECT:  " + event.getLocator().getObject()
        +"    NODE:  " + event.getLocator().getNode()
        +"    URL:  " + event.getLocator().getURL());
        return false;
    }

}

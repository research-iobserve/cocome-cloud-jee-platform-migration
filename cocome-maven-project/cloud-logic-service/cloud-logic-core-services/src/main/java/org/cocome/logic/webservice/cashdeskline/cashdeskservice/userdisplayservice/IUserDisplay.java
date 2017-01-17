package org.cocome.logic.webservice.cashdeskline.cashdeskservice.userdisplayservice;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import javax.xml.bind.annotation.XmlElement;

import org.cocome.cloud.logic.webservice.exception.UnhandledException;
import org.cocome.tradingsystem.cashdeskline.cashdesk.userdisplay.MessageKind;


@WebService(targetNamespace = "http://userdisplay.cashdesk.cashdeskline.webservice.logic.cocome.org/")
public interface IUserDisplay {
	@WebMethod
	public String getMessage(@XmlElement(required = true) @WebParam(name = "cashDeskName") String cashDeskName,
			@XmlElement(required = true) @WebParam(name = "storeID") long storeID) throws UnhandledException;
	
	@WebMethod
	public MessageKind getMessageKind(@XmlElement(required = true) @WebParam(name = "cashDeskName") String cashDeskName,
			@XmlElement(required = true) @WebParam(name = "storeID") long storeID) throws UnhandledException;
	
}

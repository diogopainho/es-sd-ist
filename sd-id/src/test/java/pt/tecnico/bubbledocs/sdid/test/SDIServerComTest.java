package pt.tecnico.bubbledocs.sdid.test;

import javax.xml.registry.JAXRException;

import mockit.Expectations;
import mockit.Mocked;

import org.junit.Test;

import pt.tecnico.bubbledocs.sdid.ws.uddi.UDDINaming;

public class SDIServerComTest {

	@Test(expected = JAXRException.class)
	public void testMockUDDIRebindException(@Mocked final UDDINaming uddiNaming)
			throws Exception {

		new Expectations() {
			{
				new UDDINaming(anyString);
				uddiNaming.rebind(anyString, anyString);
				result = new JAXRException();
			}
		};

		UDDINaming uddi = new UDDINaming("http://localhost:8081");
		uddi.rebind("SD-ID", "http://localhost:8080/sdid-ws/endpoint");
	}

	@Test(expected = JAXRException.class)
	public void testMockUDDIUnbindException(@Mocked final UDDINaming uddiNaming)
			throws Exception {

		new Expectations() {
			{
				new UDDINaming(anyString);
				uddiNaming.unbind(anyString);
				result = new JAXRException();
			}
		};

		UDDINaming uddi = new UDDINaming("http://localhost:8081");
		uddi.unbind("SD-ID");
	}

}

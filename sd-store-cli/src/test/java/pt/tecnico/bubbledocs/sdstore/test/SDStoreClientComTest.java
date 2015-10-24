package pt.tecnico.bubbledocs.sdstore.test;

import javax.xml.registry.JAXRException;

import mockit.Expectations;
import mockit.Mocked;

import org.junit.Test;

import pt.tecnico.bubbledocs.sdstore.ws.uddi.UDDINaming;

public class SDStoreClientComTest {

	@Test(expected = JAXRException.class)
	public void testMockUDDIException(@Mocked final UDDINaming uddiNaming)
			throws Exception {

		new Expectations() {
			{
				new UDDINaming(anyString);
				uddiNaming.lookup(anyString);
				result = new JAXRException();
			}
		};

		UDDINaming uddi = new UDDINaming("http://localhost:8081");
		uddi.lookup("SD-STORE");
	}
}

//package pt.tecnico.bubbledocs.sdstore.test;
//
//import static org.junit.Assert.assertEquals;
//import static org.junit.Assert.fail;
//
//import javax.xml.ws.BindingProvider;
//import javax.xml.ws.WebServiceException;
//
//import mockit.Expectations;
//import mockit.Mocked;
//
//import org.junit.Test;
//
//import pt.tecnico.bubbledocs.sdstore.cli.SDStoreClient;
//import pt.ulisboa.tecnico.sdis.store.ws.CapacityExceeded;
//import pt.ulisboa.tecnico.sdis.store.ws.CapacityExceeded_Exception;
//import pt.ulisboa.tecnico.sdis.store.ws.DocAlreadyExists;
//import pt.ulisboa.tecnico.sdis.store.ws.DocAlreadyExists_Exception;
//import pt.ulisboa.tecnico.sdis.store.ws.DocDoesNotExist;
//import pt.ulisboa.tecnico.sdis.store.ws.DocDoesNotExist_Exception;
//import pt.ulisboa.tecnico.sdis.store.ws.DocUserPair;
//import pt.ulisboa.tecnico.sdis.store.ws.SDStore;
//import pt.ulisboa.tecnico.sdis.store.ws.UserDoesNotExist;
//import pt.ulisboa.tecnico.sdis.store.ws.UserDoesNotExist_Exception;
//
//// classes generated by wsimport from WSDL
//
///**
// * Test suite
// */
//public class SDStoreClientTest<SDStore_Service extends SDStore & BindingProvider> {
//
//	@Mocked
//	pt.ulisboa.tecnico.sdis.store.ws.SDStore_Service service;
//
//	/**
//	 * In this test the server is mocked to simulate a communication exception.
//	 */
//
//	@Test(expected = WebServiceException.class)
//	public void testMockServerException() throws Exception {
//
//		// One or more invocations to mocked types, causing expectations to be
//		// recorded.
//		new Expectations() {
//			{
//				service = new pt.ulisboa.tecnico.sdis.store.ws.SDStore_Service();
//				pt.ulisboa.tecnico.sdis.store.ws.SDStore port = service
//						.getSDStoreImplPort();
//				port.createDoc((DocUserPair) any);
//				result = new WebServiceException("fabricated");
//			}
//		};
//
//		SDStoreClient client = new SDStoreClient();
//		DocUserPair pair = new DocUserPair();
//		pair.setDocumentId("receitas");
//		pair.setUserId("alice");
//
//		// call to mocked server
//		client.createDoc(pair);
//	}
//
//	/**
//	 * In this test the server is mocked to simulate a communication exception
//	 * on a second call.
//	 */
//	@Test
//	public void testMockServerExceptionOnSecondCall() throws Exception {
//
//		// One or more invocations to mocked types, causing expectations to be
//		// recorded.
//		new Expectations() {
//			{
//				pt.ulisboa.tecnico.sdis.store.ws.SDStore port = service
//						.getSDStoreImplPort();
//				port.listDocs(anyString);
//				result = new WebServiceException("fabricated");
//
//			}
//		};
//
//		// Unit under test is exercised.
//		SDStoreClient client = new SDStoreClient();
//
//		// first call to mocked server
//		try {
//			DocUserPair pair = new DocUserPair();
//			pair.setDocumentId("batata");
//			pair.setUserId("alice");
//			client.createDoc(pair);
//		} catch (WebServiceException e) {
//			// exception is not expected
//			fail();
//		}
//
//		// second call to mocked server
//		try {
//			client.listDocs("alice");
//			fail();
//		} catch (WebServiceException e) {
//			// exception is expected
//			assertEquals("fabricated", e.getMessage());
//		}
//	}
//
//	/**
//	 * In this test the server is mocked to test the document already exists
//	 * exception propagation.
//	 */
//	@Test
//	public void testMockServerDocAlreadyExists() throws Exception {
//
//		new Expectations() {
//			{
//				service = new pt.ulisboa.tecnico.sdis.store.ws.SDStore_Service();
//				pt.ulisboa.tecnico.sdis.store.ws.SDStore port = service
//						.getSDStoreImplPort();
//				port.createDoc((DocUserPair) any);
//				result = new DocAlreadyExists_Exception("fabricated",
//						new DocAlreadyExists());
//			}
//		};
//
//		// Unit under test is exercised.
//		SDStoreClient client = new SDStoreClient();
//		DocUserPair pair = new DocUserPair();
//		pair.setUserId("alice");
//		pair.setDocumentId("cebola");
//
//		try {
//			client.createDoc(pair);
//			fail();
//		} catch (DocAlreadyExists_Exception e) {
//			// exception is expected
//			assertEquals("fabricated", e.getMessage());
//		}
//
//	}
//
//	/**
//	 * In this test the server is mocked to test the user does not exist
//	 * exception propagation.
//	 */
//	@Test
//	public void testMockServerUserDoesNotExist() throws Exception {
//
//		new Expectations() {
//			{
//				service = new pt.ulisboa.tecnico.sdis.store.ws.SDStore_Service();
//				pt.ulisboa.tecnico.sdis.store.ws.SDStore port = service
//						.getSDStoreImplPort();
//				port.listDocs(anyString);
//				result = new UserDoesNotExist_Exception("fabricated",
//						new UserDoesNotExist());
//			}
//		};
//
//		// Unit under test is exercised.
//		SDStoreClient client = new SDStoreClient();
//
//		try {
//			client.listDocs("alice");
//			fail();
//		} catch (UserDoesNotExist_Exception e) {
//			// exception is expected
//			assertEquals("fabricated", e.getMessage());
//		}
//
//	}
//
//	/**
//	 * In this test the server is mocked to test the capacity exceeded exception
//	 * propagation.
//	 */
//	@Test
//	public void testMockServerCapExceeded() throws Exception {
//
//		new Expectations() {
//			{
//				service = new pt.ulisboa.tecnico.sdis.store.ws.SDStore_Service();
//				pt.ulisboa.tecnico.sdis.store.ws.SDStore port = service
//						.getSDStoreImplPort();
//				port.store((DocUserPair) any, (byte[]) any);
//				result = new CapacityExceeded_Exception("fabricated",
//						new CapacityExceeded());
//			}
//		};
//
//		// Unit under test is exercised.
//		SDStoreClient client = new SDStoreClient();
//
//		byte[] batata = new byte[1024];
//		DocUserPair pair = new DocUserPair();
//
//		pair.setUserId("carla");
//		pair.setDocumentId("batata");
//
//		try {
//			client.store(pair, batata);
//			fail();
//		} catch (CapacityExceeded_Exception e) {
//			// exception is expected
//			assertEquals("fabricated", e.getMessage());
//		}
//
//	}
//
//	/**
//	 * In this test the server is mocked to test the document does not exist
//	 * exception propagation.
//	 */
//	@Test
//	public void testMockServerDocDoesNotExist() throws Exception {
//
//		new Expectations() {
//			{
//				service = new pt.ulisboa.tecnico.sdis.store.ws.SDStore_Service();
//				pt.ulisboa.tecnico.sdis.store.ws.SDStore port = service
//						.getSDStoreImplPort();
//				port.load((DocUserPair) any);
//				result = new DocDoesNotExist_Exception("fabricated",
//						new DocDoesNotExist());
//			}
//		};
//
//		// Unit under test is exercised.
//		SDStoreClient client = new SDStoreClient();
//
//		DocUserPair pair = new DocUserPair();
//		pair.setUserId("carla");
//		pair.setDocumentId("batata");
//
//		try {
//			client.load(pair);
//			fail();
//		} catch (DocDoesNotExist_Exception e) {
//			// exception is expected
//			assertEquals("fabricated", e.getMessage());
//		}
//
//	}
//
// }

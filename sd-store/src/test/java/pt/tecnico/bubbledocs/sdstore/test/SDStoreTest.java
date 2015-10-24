//package pt.tecnico.bubbledocs.sdstore.test;
//
//import static org.junit.Assert.assertEquals;
//import static org.junit.Assert.assertNotNull;
//import static org.junit.Assert.assertTrue;
//
//import java.util.Arrays;
//import java.util.List;
//
//import org.junit.AfterClass;
//import org.junit.BeforeClass;
//import org.junit.Test;
//
//import pt.ulisboa.tecnico.sdis.store.ws.CapacityExceeded_Exception;
//import pt.ulisboa.tecnico.sdis.store.ws.DocAlreadyExists_Exception;
//import pt.ulisboa.tecnico.sdis.store.ws.DocDoesNotExist_Exception;
//import pt.ulisboa.tecnico.sdis.store.ws.DocUserPair;
//import pt.ulisboa.tecnico.sdis.store.ws.UserDoesNotExist_Exception;
//import pt.tecnico.bubbledocs.sdstore.app.SDStoreApp;
//import pt.tecnico.bubbledocs.sdstore.domain.Document;
//import pt.tecnico.bubbledocs.sdstore.ws.SDStoreImpl;
//
///*
// alice
// bruno
// carla
// duarte
// eduardo
// */
//
///**
// * Test suite
// */
//public class SDStoreTest {
//
//	private static SDStoreImpl sdStoreImpl;
//	private static SDStoreApp sdStoreApp = SDStoreApp.getInstance();
//
//	@BeforeClass
//	public static void oneTimeSetup() throws DocAlreadyExists_Exception {
//		sdStoreImpl = new SDStoreImpl();
//
//		DocUserPair CarlaPair = new DocUserPair();
//		DocUserPair AlicePair = new DocUserPair();
//		DocUserPair DuartePair = new DocUserPair();
//
//		AlicePair.setUserId("alice");
//		AlicePair.setDocumentId("cebola");
//		sdStoreImpl.createDoc(AlicePair);
//
//		CarlaPair.setUserId("carla");
//		CarlaPair.setDocumentId("batata");
//		sdStoreImpl.createDoc(CarlaPair);
//		CarlaPair.setDocumentId("tomate");
//		sdStoreImpl.createDoc(CarlaPair);
//		CarlaPair.setDocumentId("alface");
//		sdStoreImpl.createDoc(CarlaPair);
//
//		DuartePair.setUserId("duarte");
//		DuartePair.setDocumentId("cenoura");
//		sdStoreImpl.createDoc(DuartePair);
//
//	}
//
//	@AfterClass
//	public static void oneTimeTearDown() {
//		sdStoreImpl = null;
//		sdStoreApp = null;
//	}
//
//	// Testes CreateDoc
//
//    @Test
//    public void testCreateDoc() throws Exception {
//        final DocUserPair docUser = new DocUserPair();
//        docUser.setDocumentId("a1");
//        docUser.setUserId("alice");
//
//        sdStoreImpl.createDoc(docUser);
//    }
//
//    @Test
//    public void testCreateDocNullUser() throws Exception {
//        final DocUserPair docUser = new DocUserPair();
//        docUser.setDocumentId("a1");
//        docUser.setUserId(null);
//
//        sdStoreImpl.createDoc(docUser);
//    }
//
//    @Test
//    public void testCreateDocEmptyUser() throws Exception {
//        final DocUserPair docUser = new DocUserPair();
//        docUser.setDocumentId("a1");
//        docUser.setUserId("");
//
//        sdStoreImpl.createDoc(docUser);
//    }
//
//    @Test
//    public void testCreateNullDoc() throws Exception {
//        final DocUserPair docUser = new DocUserPair();
//        docUser.setDocumentId(null);
//        docUser.setUserId("alice");
//
//        sdStoreImpl.createDoc(docUser);
//    }
//
//    @Test
//    public void testCreateEmptyDoc() throws Exception {
//        final DocUserPair docUser = new DocUserPair();
//        docUser.setDocumentId("");
//        docUser.setUserId("alice");
//
//        sdStoreImpl.createDoc(docUser);
//    }
//
//    @Test(expected = DocAlreadyExists_Exception.class)
//    public void tesCreateDocTwice() throws Exception {
//        final DocUserPair docUser = new DocUserPair();
//        docUser.setDocumentId("a2");
//        docUser.setUserId("alice");
//
//        sdStoreImpl.createDoc(docUser);
//        // repeat
//        sdStoreImpl.createDoc(docUser);
//    }
//	
//	@Test(expected = DocAlreadyExists_Exception.class)
//	public void testDocAlreadyExists() throws DocAlreadyExists_Exception {
//		DocUserPair pair = new DocUserPair();
//		pair.setUserId("alice");
//		pair.setDocumentId("cebola");
//		sdStoreImpl.createDoc(pair);
//	}
//
//	@Test
//	public void sucessCreateDoc() throws DocAlreadyExists_Exception,
//			DocDoesNotExist_Exception {
//		DocUserPair pair = new DocUserPair();
//		pair.setUserId("alice");
//		pair.setDocumentId("tomate");
//
//		sdStoreImpl.createDoc(pair);
//		Document doc = sdStoreApp.getRepoFromUser("alice")
//				.getDocument("tomate");
//
//		boolean docCreated = doc.getDocID().equals("tomate");
//
//		assertTrue("Documento nao foi criado", docCreated);
//	}
//
//		
//	@Test(expected = UserDoesNotExist_Exception.class)
//    public void testListDocsNoUser() throws Exception {
//        final String user = "userthatdoesnotexist";
//        sdStoreImpl.listDocs(user);
//    }
//
//    @Test(expected = UserDoesNotExist_Exception.class)
//    public void testListDocsNullUser() throws Exception {
//        sdStoreImpl.listDocs(null);
//    }
//
//    @Test(expected = UserDoesNotExist_Exception.class)
//    public void testListDocsEmptyUser() throws Exception {
//        final String user = "";
//        sdStoreImpl.listDocs(user);
//    }
//
//    @Test
//    public void testEmptyListDocs() throws Exception {
//        final String user = "bruno";
//
//        List<String> list = sdStoreImpl.listDocs(user);
//        assertNotNull(list);
//        assertEquals(0, list.size());
//    }
//
//    @Test
//    public void testListDocs() throws Exception {
//        final String user = "bruno";
//
//        {
//            final DocUserPair docUser = new DocUserPair();
//            docUser.setDocumentId("b1");
//            docUser.setUserId(user);
//
//            sdStoreImpl.createDoc(docUser);
//        }
//        {
//            final DocUserPair docUser = new DocUserPair();
//            docUser.setDocumentId("b2");
//            docUser.setUserId(user);
//
//            sdStoreImpl.createDoc(docUser);
//        }
//        {
//            final DocUserPair docUser = new DocUserPair();
//            docUser.setDocumentId("b3");
//            docUser.setUserId(user);
//
//            sdStoreImpl.createDoc(docUser);
//        }
//
//        List<String> list = sdStoreImpl.listDocs(user);
//        assertNotNull(list);
//        assertEquals(3, list.size());
//        assertTrue(list.contains("b1"));
//        assertTrue(list.contains("b2"));
//        assertTrue(list.contains("b3"));
//    }
//    
//    
//    @Test
//    public void testLoad() throws Exception {
//        final DocUserPair docUser = new DocUserPair();
//        docUser.setDocumentId("d1");
//        docUser.setUserId("duarte");
//
//        // create document
//        sdStoreImpl.createDoc(docUser);
//        // store document
//        sdStoreImpl.store(docUser, "d1contents".getBytes());
//
//        // load document
//        byte[] contents = sdStoreImpl.load(docUser);
//
//        assertNotNull(contents);
//        assertTrue(Arrays.equals("d1contents".getBytes(), contents));
//    }
//
//    @Test(expected = DocDoesNotExist_Exception.class)
//    public void testDocDoesNotExist() throws Exception {
//        final DocUserPair docUser = new DocUserPair();
//        docUser.setDocumentId("d2");
//        docUser.setUserId("duarte");
//
//        // load document
//        sdStoreImpl.load(docUser);
//    }
//
//    @Test(expected = DocDoesNotExist_Exception.class)
//    public void testLoadNullDoc() throws Exception {
//        final DocUserPair docUser = new DocUserPair();
//        docUser.setDocumentId(null);
//        docUser.setUserId("duarte");
//
//        // load document
//        sdStoreImpl.load(docUser);
//    }
//
//    @Test(expected = DocDoesNotExist_Exception.class)
//    public void testLoadEmptyDoc() throws Exception {
//        final DocUserPair docUser = new DocUserPair();
//        docUser.setDocumentId("");
//        docUser.setUserId("duarte");
//
//        // load document
//        sdStoreImpl.load(docUser);
//    }
//
//    @Test(expected = UserDoesNotExist_Exception.class)
//    public void testUserDoesNotExist() throws Exception {
//        final DocUserPair docUser = new DocUserPair();
//        docUser.setDocumentId("d3");
//        docUser.setUserId("userthatdoesnotexist");
//
//        // load document
//        sdStoreImpl.load(docUser);
//    }
//
//    @Test(expected = UserDoesNotExist_Exception.class)
//    public void testLoadNullUser() throws Exception {
//        final DocUserPair docUser = new DocUserPair();
//        docUser.setDocumentId("d3");
//        docUser.setUserId(null);
//
//        // load document
//        sdStoreImpl.load(docUser);
//    }
//
//    @Test(expected = UserDoesNotExist_Exception.class)
//    public void testLoadEmptyUser() throws Exception {
//        final DocUserPair docUser = new DocUserPair();
//        docUser.setDocumentId("d3");
//        docUser.setUserId("");
//
//        // load document
//        sdStoreImpl.load(docUser);
//    }
//    
//    @Test
//    public void testStore() throws Exception {
//        final DocUserPair docUser = new DocUserPair();
//        docUser.setDocumentId("c1");
//        docUser.setUserId("carla");
//
//        sdStoreImpl.createDoc(docUser);
//
//        sdStoreImpl.store(docUser, "c1contents".getBytes());
//        byte[] a = "".getBytes();
//    }
//
//    @Test
//    public void testStoreNullContent() throws Exception {
//        final DocUserPair docUser = new DocUserPair();
//        docUser.setDocumentId("j1");
//        docUser.setUserId("Joana");
//
//        sdStoreImpl.createDoc(docUser);
//
//        sdStoreImpl.store(docUser, null);
//    }
//
//
//    @Test(expected = DocDoesNotExist_Exception.class)
//    public void testStoreNullDoc() throws Exception {
//        final DocUserPair docUser = new DocUserPair();
//        docUser.setDocumentId(null);
//        docUser.setUserId("carla");
//
//        sdStoreImpl.store(docUser, "c2contents".getBytes());
//    }
//
//    @Test(expected = DocDoesNotExist_Exception.class)
//    public void testStoreEmptyDoc() throws Exception {
//        final DocUserPair docUser = new DocUserPair();
//        docUser.setDocumentId("");
//        docUser.setUserId("carla");
//
//        sdStoreImpl.store(docUser, "c2contents".getBytes());
//    }
//
//    @Test(expected = UserDoesNotExist_Exception.class)
//    public void testStoreNullUser() throws Exception {
//        final DocUserPair docUser = new DocUserPair();
//        docUser.setDocumentId("c3");
//        docUser.setUserId(null);
//
//        sdStoreImpl.store(docUser, "c3contents".getBytes());
//    }
//
//    @Test(expected = UserDoesNotExist_Exception.class)
//    public void testStoreEmptyUser() throws Exception {
//        final DocUserPair docUser = new DocUserPair();
//        docUser.setDocumentId("c3");
//        docUser.setUserId("");
//
//        sdStoreImpl.store(docUser, "c3contents".getBytes());
//    }
//
//
//
//    
//    
//	
// }
//package pt.tecnico.bubbledocs.integration.system;
//
//import static org.junit.Assert.*;
//
//import javax.xml.registry.JAXRException;
//
//import org.hamcrest.core.IsEqual;
//import org.junit.*;
//
//import mockit.*;
//import pt.ist.fenixframework.Atomic;
//import pt.tecnico.bubbledocs.domain.ADD;
//import pt.tecnico.bubbledocs.domain.BubbleDocs;
//import pt.tecnico.bubbledocs.domain.Content;
//import pt.tecnico.bubbledocs.domain.DIV;
//import pt.tecnico.bubbledocs.domain.Literal;
//import pt.tecnico.bubbledocs.domain.NonFunction;
//import pt.tecnico.bubbledocs.domain.Reference;
//import pt.tecnico.bubbledocs.domain.SpreadSheet;
//import pt.tecnico.bubbledocs.domain.User;
//import pt.tecnico.bubbledocs.integration.CreateSpreadSheetIntegrator;
//import pt.tecnico.bubbledocs.integration.CreateUserIntegrator;
//import pt.tecnico.bubbledocs.integration.LoginUserIntegrator;
//import pt.tecnico.bubbledocs.integration.component.BubbleDocsIT;
//import pt.tecnico.bubbledocs.service.local.AssignLiteralCell;
//import pt.tecnico.bubbledocs.service.local.AssignReferenceCell;
//import pt.tecnico.bubbledocs.service.local.CreateSpreadSheet;
//import pt.tecnico.bubbledocs.service.local.CreateUser;
//import pt.tecnico.bubbledocs.service.local.GetUserInfoService;
//import pt.tecnico.bubbledocs.service.remote.IDRemoteServices;
//import pt.tecnico.bubbledocs.service.remote.StoreRemoteServices;
//
//public class LocalSystemTest extends BubbleDocsIT{
//	
//	private static BubbleDocs bd;
//    
//    private static String ROOT_NAME = "root";
//    private static String ROOT_PASS = "rootroot";
//    private static String rootToken;
//    private static String userToken;
//   
//    private static String USERNAME_1 = "dig";
//    private static String USERNAME_2 = "mik";
//    
//    private static String PASS_1 = "Aaa1";
//   
//    private static String EMAIL_1 = "email1@bubble.com";
//    private static String EMAIL_2 = "email2@bubble.com";
//   
//    private static String NAME_1 = "diogo";
//    private static String NAME_2 = "mikael";
//   
//    private static String FILE_1 = "sheet1";
//    
//    private static final String SERVICE = "SD-STORE;2";
//    
//    private GetUserInfoService user1, user2;
//    
//    @Before
//    public void oneTimeSetup() {
//            LoginUserIntegrator loginUser = new LoginUserIntegrator(ROOT_NAME, ROOT_PASS, SERVICE.getBytes());
//            loginUser.execute();
//            rootToken = addUserToSession(ROOT_NAME);
//            userToken = addUserToSession("dig");
//    }
//   
//    @After
//    public void tearDown() {
//            deleteAllDomain();
//    }
//	
//    @Atomic
//    static void deleteAllDomain()
//    {
////            BubbleDocs db = BubbleDocs.getInstance();
////
////            for(User u: db.getUserSet()){
////                    u.delete();
////            }
////            
////            for(SpreadSheet s: db.getSpreadSheetSet()){
////                    s.delete();
////            }
////            
////            for(Session ss : db.getSessionSet()){
////                    ss.delete();
////            }
////            
////            System.out.println("All domain has been deleted with success");
//    }
//    
//    @Test
//    public void sucess(@Mocked final IDRemoteServices idremote,
//                    @Mocked final StoreRemoteServices storeremote) throws JAXRException {
//           
//            // CREATE THE USER
//            new Expectations() {{
//                    //new IDRemoteServices();
//                    idremote.createUser(USERNAME_1, EMAIL_1);
//                    idremote.createUser(USERNAME_2, EMAIL_2);
//            }};
//           
//            new CreateUserIntegrator(rootToken, USERNAME_1, EMAIL_1, NAME_1).execute();
//            new CreateUserIntegrator(rootToken, USERNAME_2, EMAIL_2, NAME_2).execute();
//                           
//            user1 = new GetUserInfoService(USERNAME_1);
//            user1.execute();
//           
//            assertEquals(USERNAME_1, user1.getUserName());
//           
//            user2 = new GetUserInfoService(USERNAME_2);
//            user2.execute();
//           
//            assertEquals(USERNAME_2, user2.getUserName());
//           
//           
//            // USER LOGIN
//            new Expectations() {{
//                    idremote.loginUser(user1.getUserName(),SERVICE.getBytes());
//                    //idremote.loginUser(user2.getUsername(), user2.getPassword());
//                    //result = any;
//            }};
//           
//            LoginUserIntegrator lg1 = new LoginUserIntegrator(USERNAME_1, PASS_1, SERVICE.getBytes());
//            lg1.execute();
//           
//            assertNotNull(lg1.getData());
//           
//           
//            // USER CREATES THE SHEET
//            CreateSpreadSheetIntegrator sheet1 = new CreateSpreadSheetIntegrator( userToken , FILE_1, 5, 5);
//            sheet1.execute();
//            System.out.println("the ID: " + sheet1.getSheetId());
//            assertNotNull(sheet1.getSheetId());
//           
//            // USER ASSIGNS SOME VALUES
//            new AssignLiteralCellIntegrator(new Integer(4), "5;4", sheet1.getSheetId(), lg1.getToken()).execute();
//            new AssignLiteralCellIntegrator(new Integer(3), "5;3", sheet1.getSheetId(), lg1.getToken()).execute();
//            new AssignLiteralCellIntegrator(new Integer(2), "5;2", sheet1.getSheetId(), lg1.getToken()).execute();
//           
//           
//            // THE OTHER USER IS REMOVED
//            new Expectations() {{
//                    idremote.removeUser(USERNAME_2);
//                    result = any;
//            }};
//           
//            new RemoveUserIntegrator(rootToken, USERNAME_2).execute(); //must test permissions
//           
//            ObtainSpreadSheetContentIntegrator content_1 = new ObtainSpreadSheetContentIntegrator(sheet1.getSheetId(), lg1.getToken());
//            content_1.execute();
//            String[][] spreadSheet_1 = content_1.getMatrix();
//           
//            for(int row = 0; row < sheet1.getRows(); row++) {
//                    for(int col = 0; col < sheet1.getColumns(); col++) {
//                            if(spreadSheet_1[col][row].equals(""))
//                                    System.out.println("#VALUE");
//                            else
//                                    System.out.println(spreadSheet_1[col][row]);
//                    }
//            }
//           
//            for(int row = 0; row < sheet1.getRows(); row++) {
//                    for(int col = 0; col < sheet1.getColumns(); col++) {
//                            String cell = row + ";" + col;
//                            new AssignLiteralCellIntegrator(new Integer(row), cell, sheet1.getSheetId(), lg1.getToken()).execute();
//                    }
//            }
//           
//            ObtainSpreadSheetContentIntegrator content_2 = new ObtainSpreadSheetContentIntegrator(sheet1.getSheetId(), lg1.getToken());
//            content_2.execute();
//            String[][] spreadSheet_2 = content_2.getMatrix();
//           
//            for(int row = 0; row < sheet1.getRows(); row++) {
//                    for(int col = 0; col < sheet1.getColumns(); col++) {
//                            if(spreadSheet_2[col][row].equals(""))
//                                    System.out.println("#VALUE");
//                            else
//                                    System.out.println(spreadSheet_2[col][row]);
//                    }
//            }
//           
//            assertNotSame(spreadSheet_1, spreadSheet_2);
//           
//            new Expectations() {{
//                    storeremote.storeDocument(USERNAME_1, FILE_1, (byte[]) any, 1);
//                    result = any;
//            }};
//           
//            ExportDocumentIntegrator export = new ExportDocumentIntegrator(lg1.getToken(), sheet1.getSheetId());
//            export.execute();
//            String xml = new String(export.getXML());
//            System.out.println(xml);
//           
//            //String tokenUser, long docId, String cellId,String reference
//            new AssignReferenceCellIntegrator(lg1.getToken(), sheet1.getSheetId(), "1;1", "5;5").execute();
//            new AssignReferenceCellIntegrator(lg1.getToken(), sheet1.getSheetId(), "1;2", "5;6").execute();
//            new AssignReferenceCellIntegrator(lg1.getToken(), sheet1.getSheetId(), "1;3", "5;7").execute();
//            new AssignReferenceCellIntegrator(lg1.getToken(), sheet1.getSheetId(), "1;4", "5;8").execute();
//            new AssignReferenceCellIntegrator(lg1.getToken(), sheet1.getSheetId(), "1;5", "5;9").execute();
//           
//           
//            ObtainSpreadSheetContentIntegrator content_3 = new ObtainSpreadSheetContentIntegrator(sheet1.getSheetId(), lg1.getToken());
//            content_3.execute();
//            String[][] spreadSheet_3 = content_3.getMatrix();
//           
//            for(int row = 0; row < sheet1.getRows(); row++) {
//                    System.out.println("**********");
//                    for(int col = 0; col < sheet1.getColumns(); col++) {
//                            if(spreadSheet_3[col][row].equals(""))
//                                    System.out.println("#VALUE");
//                            else
//                                    System.out.println(spreadSheet_3[col][row]);
//                    }
//            }
//           
//            assertNotSame(spreadSheet_2, spreadSheet_3);
//           
//    }
//
//}

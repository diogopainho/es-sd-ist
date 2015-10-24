package pt.tecnico.bubbledocs;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;
import pt.ist.fenixframework.TransactionManager;
import pt.tecnico.bubbledocs.domain.ADD;
//Importar as classes do .domain
import pt.tecnico.bubbledocs.domain.BubbleDocs;
import pt.tecnico.bubbledocs.domain.Content;
import pt.tecnico.bubbledocs.domain.DIV;
import pt.tecnico.bubbledocs.domain.Literal;
import pt.tecnico.bubbledocs.domain.NonFunction;
import pt.tecnico.bubbledocs.domain.Reference;
import pt.tecnico.bubbledocs.domain.SpreadSheet;
import pt.tecnico.bubbledocs.integration.ExportDocumentIntegrator;
import pt.tecnico.bubbledocs.integration.LoginUserIntegrator;
import pt.tecnico.bubbledocs.service.local.AssignLiteralCell;
import pt.tecnico.bubbledocs.service.local.AssignReferenceCell;
import pt.tecnico.bubbledocs.service.local.CreateSpreadSheet;
import pt.tecnico.bubbledocs.service.local.CreateUser;

public class BubbleDocsApplication {
	public static void main(String[] args) {
		System.out.println("Welcome to the Bubble Docs application!");

		TransactionManager tm = FenixFramework.getTransactionManager();
		BubbleDocs bd = null;
		try {
			tm.begin();
			bd = BubbleDocs.getInstance();
			if (bd.getUserSet().size() <= 1) {
				populateDomainWithUsers(bd);
				populateDomainLogin(bd);
			}
			tm.commit();
		} catch (Exception e) {
			System.out.println(e);
		}

		String service = "SD-STORE;200";
		LoginUserIntegrator login = new LoginUserIntegrator("alice", "Aaa1",
				service.getBytes());
		login.execute();

		try {
			tm.begin();
			String token = bd.getUserByUsername("alice").getToken();
			System.out.println("TOKEN: " + token);

			String pfToken = bd.getUserByUsername("alice").getToken();
			CreateSpreadSheet ss = new CreateSpreadSheet(pfToken, "Notas ES",
					300, 20);
			ss.execute();

			ExportDocumentIntegrator export = new ExportDocumentIntegrator(
					token, ss.getSheetId());

			// export.execute();
			tm.commit();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// byte[] data = login.getData();

		// TransactionManager tm = FenixFramework.getTransactionManager();
		// boolean committed = false;
		//
		// try { tm.begin();
		//
		// BubbleDocs bd = BubbleDocs.getInstance(); SerializeToXML serial = new
		// SerializeToXML();
		//
		// if (bd.getUserSet().size() <= 1) {
		//
		// populateDomainWithUsers(bd); populateDomainLogin(bd); int
		// populatedSheet = populateDomainWithServices(bd);
		// populateDomainWithoutServices(bd, populatedSheet); }
		//
		// // Escrever a informacao sobre todos os utilizadores registados na //
		// aplicacao bd.printAllUsers();
		//
		// // Escrever o nomes de todas as folhas de calculo dos utilizadores //
		// pf e ra
		//
		// bd.printSpreadsheetsFromAuthor("pff");
		// bd.printSpreadsheetsFromAuthor("raa");
		//
		// String pfToken = bd.getUserByUsername("pff").getToken();
		// org.jdom2.Document doc = null;
		//
		// for (SpreadSheet s : bd.getSpreadsheetsFromAuthor("pff")) { int
		// sheetId = s.getId();
		//
		// ExportDocument export = new ExportDocument(pfToken, sheetId);
		// export.execute();
		//
		// // Exporta folhas de calculo do user pf para XML doc =
		// export.getDocument();
		//
		// // Imprime folhas exportadas no ecran serial.printDomainInXML(doc); }
		//
		// for (SpreadSheet s : bd.getSpreadsheetsFromAuthor("pff")) {
		//
		// // Apaga folha de calculo do user pf s.delete(); }
		//
		// // Escrever os nomes e identificadores de todas as folhas de calculo
		// // do utilizador pf
		//
		// bd.getSpreadsheetsFromAuthor("pff");
		//
		// // Importar folha SpreadSheet sBackup = bd.recoverFromBackup(doc);
		//
		// bd.addSpreadSheet(sBackup);
		//
		// // Escrever os nomes e identificadores de todas as folhas de calculo
		// // do utilizador pf bd.printSpreadsheetsFromAuthor("pff");
		//
		// for (SpreadSheet s : bd.getSpreadsheetsFromAuthor("pff")) { //
		// Exporta folhas de calculo do user pf para XML doc =
		// bd.convertToXML(s);
		//
		// // Imprime folhas exportadas no ecran serial.printDomainInXML(doc); }
		//
		// tm.commit(); committed = true; } catch (SystemException |
		// NotSupportedException | RollbackException | HeuristicMixedException |
		// HeuristicRollbackException ex) {
		// System.err.println("Error in execution of transaction: " + ex); }
		// finally { if (!committed) try { tm.rollback(); } catch
		// (SystemException ex) {
		// System.err.println("Error in roll back of transaction: " + ex); } }

	}

	@Atomic
	static void populateDomainWithUsers(BubbleDocs bd) {

		String rootToken = bd.addUserToSession("root");

		CreateUser pf = new CreateUser(rootToken, "pff", "pff@mail.com",
				"Paul Door");
		pf.execute();

		CreateUser ra = new CreateUser(rootToken, "raa", "raa@mail.com",
				"Step Rabbit");

		ra.execute();

		CreateUser al = new CreateUser(rootToken, "alice", "alice@mail.com",
				"Alice");

		al.execute();

	}

	@Atomic
	static void populateDomainLogin(BubbleDocs bd) {

		bd.addUserToSession("pff");

		bd.addUserToSession("raa");

		bd.addUserToSession("alice");
	}

	@Atomic
	static int populateDomainWithServices(BubbleDocs bd) {

		String pfToken = bd.getUserByUsername("alice").getToken();
		CreateSpreadSheet ss = new CreateSpreadSheet(pfToken, "Notas ES", 300,
				20);
		ss.execute();

		int sheetId = ss.getSheetId();
		AssignLiteralCell literal1 = new AssignLiteralCell(pfToken, sheetId,
				"3;4", "5");
		literal1.execute();

		AssignReferenceCell reference1 = new AssignReferenceCell(pfToken,
				sheetId, "1;1", "5;6");
		reference1.execute();
		return sheetId;
	}

	@Atomic
	static void populateDomainWithoutServices(BubbleDocs bd, int sheetId) {
		SpreadSheet s1 = bd.getSpreadSheetById(sheetId);

		NonFunction literal2 = new Literal(2);
		NonFunction reference2 = new Reference(s1.getCellByCoordinates(3, 4));
		Content add = new ADD(literal2, reference2);
		s1.addContentToCell(5, 6, add);

		NonFunction reference3 = new Reference(s1.getCellByCoordinates(1, 1));
		NonFunction reference4 = new Reference(s1.getCellByCoordinates(3, 4));
		Content div = new DIV(reference3, reference4);
		s1.addContentToCell(2, 2, div);

	}

}

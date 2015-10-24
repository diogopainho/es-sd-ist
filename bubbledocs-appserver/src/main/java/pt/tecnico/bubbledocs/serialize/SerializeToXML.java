package pt.tecnico.bubbledocs.serialize;

import org.jdom2.Element;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;
import org.joda.time.DateTime;

import pt.tecnico.bubbledocs.domain.ADD;
import pt.tecnico.bubbledocs.domain.BinaryFunction;
import pt.tecnico.bubbledocs.domain.BubbleDocs;
import pt.tecnico.bubbledocs.domain.Cell;
import pt.tecnico.bubbledocs.domain.Content;
import pt.tecnico.bubbledocs.domain.DIV;
import pt.tecnico.bubbledocs.domain.IntervalFunction;
import pt.tecnico.bubbledocs.domain.Literal;
import pt.tecnico.bubbledocs.domain.MUL;
import pt.tecnico.bubbledocs.domain.NonFunction;
import pt.tecnico.bubbledocs.domain.Permissions;
import pt.tecnico.bubbledocs.domain.Reference;
import pt.tecnico.bubbledocs.domain.SUB;
import pt.tecnico.bubbledocs.domain.SpreadSheet;

public class SerializeToXML {

	public SpreadSheet recoverFromBackup(org.jdom2.Document jdomDoc) {

		SpreadSheet ss = new SpreadSheet();
		ssImportFromXML(jdomDoc.getRootElement(), ss);

		return ss;
	}

	public void ssImportFromXML(Element ssElement, SpreadSheet ss) {
		String author = ssElement.getAttribute("author").getValue();
		ss.setName(ssElement.getAttribute("name").getValue());
		ss.setNumlines(Integer.parseInt(ssElement.getAttribute("numlines")
				.getValue()));
		ss.setNumcolumns(Integer.parseInt(ssElement.getAttribute("numcolumns")
				.getValue()));
		ss.setAuthor(author);
		ss.setDate(new DateTime(Long.parseLong(ssElement.getAttribute("date")
				.getValue())));
		BubbleDocs.getInstance().addPermissions(
				new Permissions(true, true, ss.getId(), author));

		Element cells = ssElement.getChild("cells");
		Element backupCells = cells.clone();

		for (Element cell : cells.getChildren("cell")) {

			Cell c = new Cell(Integer.parseInt(cell.getAttribute("line")
					.getValue()), Integer.parseInt(cell.getAttribute("column")
					.getValue()), Boolean.parseBoolean(cell
					.getAttribute("lock").getValue()));

			ss.addCells(c);
		}

		for (Element cellElement : backupCells.getChildren("cell")) {
			cellImportFromXML(cellElement, ss);
		}

	}

	public void cellImportFromXML(Element cellElement, SpreadSheet ss) {
		int lin = Integer.parseInt(cellElement.getAttribute("line").getValue());
		int col = Integer.parseInt(cellElement.getAttribute("column")
				.getValue());

		Cell cell = ss.getCellByCoordinates(lin, col);

		Element content = cellElement.getChild("content");
		Element valueElement = content.clone();
		for (Element value : content.getChildren()) {

			if (value.toString().contains("ADD")) {
				Element addElement = valueElement.getChild("ADD");
				ADD add = new ADD();
				bfImportFromXML(add, addElement, ss);
				cell.setContent(add);

			} else if (value.toString().contains("MUL")) {
				Element mulElement = valueElement.getChild("MUL");
				MUL mul = new MUL();
				bfImportFromXML(mul, mulElement, ss);
				cell.setContent(mul);

			} else if (value.toString().contains("SUB")) {
				Element subElement = valueElement.getChild("SUB");
				SUB sub = new SUB();
				bfImportFromXML(sub, subElement, ss);
				cell.setContent(sub);

			} else if (value.toString().contains("DIV")) {
				Element divElement = valueElement.getChild("DIV");
				DIV div = new DIV();
				bfImportFromXML(div, divElement, ss);
				cell.setContent(div);

			} else if (value.toString().contains("Reference")) {
				Element refElement = valueElement.getChild("Reference");
				Reference ref = new Reference();
				refImportFromXML(ref, refElement, ss);
				cell.setContent(ref);
			} else if (value.toString().contains("Literal")) {
				Element litElement = valueElement.getChild("Literal");
				Literal lit = new Literal();
				litImportFromXML(lit, litElement);
				cell.setContent(lit);
			} else if (value.toString().contains("AVG")) {
				// TODO
			} else if (value.toString().contains("PRD")) {
				// TODO
			}
		}
	}

	public void bfImportFromXML(BinaryFunction bin, Element valElement,
			SpreadSheet ss_to_ref) {
		Element leftElement = valElement.getChild("leftNonFunction");
		Element rightElement = valElement.getChild("rightNonFunction");
		Element lnfElement = leftElement.clone();
		Element rnfElement = rightElement.clone();

		for (Element leftAuxValue : leftElement.getChildren()) {

			if (leftAuxValue.toString().contains("Reference")) {
				Element lEl = lnfElement.getChild("Reference");
				Reference ref = new Reference();
				refImportFromXML(ref, lEl, ss_to_ref);
				bin.setLeftNonFunction(ref);

			} else if (leftAuxValue.toString().contains("Literal")) {
				Element lEl = lnfElement.getChild("Literal");
				Literal lit = new Literal();
				litImportFromXML(lit, lEl);
				bin.setLeftNonFunction(lit);
			}
		}
		for (Element rightAuxValue : rightElement.getChildren()) {
			if (rightAuxValue.toString().contains("Reference")) {
				Element rEl = rnfElement.getChild("Reference");
				Reference ref = new Reference();
				refImportFromXML(ref, rEl, ss_to_ref);
				bin.setRightNonFunction(ref);

			} else if (rightAuxValue.toString().contains("Literal")) {
				Element rEl = rnfElement.getChild("Literal");
				Literal lit = new Literal();
				litImportFromXML(lit, rEl);
				bin.setRightNonFunction(lit);
			}
		}
	}

	public void litImportFromXML(Literal lit, Element leftElement) {
		lit.setValue(Integer.parseInt(leftElement.getAttribute("value")
				.getValue()));
	}

	public void refImportFromXML(Reference ref, Element leftElement,
			SpreadSheet ss) {
		int lin = Integer.parseInt(leftElement.getAttribute("line").getValue());
		int col = Integer.parseInt(leftElement.getAttribute("column")
				.getValue());
		ref.setReferencedCell(ss.getCellByCoordinates(lin, col));
	}

	public org.jdom2.Document convertToXML(SpreadSheet ss) {

		org.jdom2.Document jdomDoc = new org.jdom2.Document();

		jdomDoc.setRootElement(ssExportToXML(ss));

		return jdomDoc;
	}

	public Element ssExportToXML(SpreadSheet ss) {
		Element element = new Element("spreadsheet");

		element.setAttribute("name", ss.getName());
		element.setAttribute("author", ss.getAuthor());
		element.setAttribute("numlines", Integer.toString(ss.getNumlines()));
		element.setAttribute("numcolumns", Integer.toString(ss.getNumcolumns()));
		element.setAttribute("date", String.valueOf(ss.getDate().getMillis()));
		Element cellElement = new Element("cells");
		element.addContent(cellElement);

		for (Cell celula : ss.getCellsSet()) {
			cellElement.addContent(cellExportToXML(celula));
		}

		return element;
	}

	public Element cellExportToXML(Cell celula) {
		Element element = new Element("cell");

		element.setAttribute("line", Integer.toString(celula.getLine()));
		element.setAttribute("column", Integer.toString(celula.getColumn()));
		element.setAttribute("lock", Boolean.toString(celula.getLock()));

		Element contentElement = new Element("content");
		element.addContent(contentElement);

		Content cont = celula.getContent();
		contentElement.addContent(contentExportToXML(cont));

		return element;
	}

	public Element contentExportToXML(Content cont) {
		Element element = null;

		if (cont instanceof BinaryFunction) {
			if (cont instanceof ADD) {
				element = new Element("ADD");
			} else if (cont instanceof DIV) {
				element = new Element("DIV");
			} else if (cont instanceof SUB) {
				element = new Element("SUB");
			} else if (cont instanceof MUL) {
				element = new Element("MUL");
			}
			Element leftElement = new Element("leftNonFunction");
			element.addContent(leftElement);
			Element rightElement = new Element("rightNonFunction");
			element.addContent(rightElement);

			NonFunction lnf = ((BinaryFunction) cont).getLeftNonFunction();
			leftElement.addContent(nfExportToXML(lnf));
			NonFunction rnf = ((BinaryFunction) cont).getRightNonFunction();
			rightElement.addContent(nfExportToXML(rnf));

		}
		if (cont instanceof IntervalFunction) {
			// if(cont instanceof AVG){
			// //TODO
			// }
			// else if(cont instanceof PRD){
			// //TODO
			// }
		} else if (cont instanceof Reference) {
			element = new Element("Reference");
			element.setAttribute("line", Integer.toString(((Reference) cont)
					.getReferencedCell().getLine()));
			element.setAttribute("column", Integer.toString(((Reference) cont)
					.getReferencedCell().getColumn()));
		} else if (cont instanceof Literal) {
			element = new Element("Literal");
			element.setAttribute("value",
					Integer.toString(((Literal) cont).getValue()));
		}

		return element;
	}

	public Element nfExportToXML(NonFunction nf) {
		Element element = null;

		if (nf instanceof Reference) {
			element = new Element("Reference");
			element.setAttribute("line", Integer.toString(((Reference) nf)
					.getReferencedCell().getLine()));
			element.setAttribute("column", Integer.toString(((Reference) nf)
					.getReferencedCell().getColumn()));
		} else if (nf instanceof Literal) {
			element = new Element("Literal");
			element.setAttribute("value",
					Integer.toString(((Literal) nf).getValue()));
		}

		return element;
	}

	public void printDomainInXML(org.jdom2.Document jdomDoc) {
		XMLOutputter xml = new XMLOutputter();
		xml.setFormat(Format.getPrettyFormat());
		System.out.println(xml.outputString(jdomDoc));
	}

}

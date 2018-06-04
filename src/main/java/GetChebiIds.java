import uk.ac.ebi.chebi.webapps.chebiWS.client.ChebiWebServiceClient;
import uk.ac.ebi.chebi.webapps.chebiWS.model.*;

import javax.xml.namespace.QName;
import java.io.*;
import java.net.URL;

/**
 * @author venkat
 * @date 16/11/2017
 */
public class GetChebiIds {

	public static int matched = 0;
	public static int total = 0;
	public static int notExact = 0;
	public static int notmatched = 0;
	public static int error = 0;

	public static final String INCHIKEY = "INCHIKEY";
	public static final String DBACC = "DBACC";
	public static final String NAMES = "NAME";


	public static void getMatchedChEBIId(String query, String type, Writer writer) throws Exception {
		try {
			// Create client
			ChebiWebServiceClient client = new ChebiWebServiceClient(new URL("http://www.ebi.ac.uk/webservices/chebi/2.0/webservice?wsdl"),new QName("https://www.ebi.ac.uk/webservices/chebi", "ChebiWebServiceService"));
			LiteEntityList entities = null;

			if (type.equalsIgnoreCase(INCHIKEY)) {
				entities = client.getLiteEntity(query,
						SearchCategory.INCHI_INCHI_KEY,
						50,
						StarsCategory.ALL);

			} else if (type.equalsIgnoreCase(DBACC)) {
				entities = client.getLiteEntity(query,
						SearchCategory.DATABASE_LINK_REGISTRY_NUMBER_CITATION,
						100,
						StarsCategory.ALL);
			} else if (type.equalsIgnoreCase(NAMES)) {
				entities = client.getLiteEntity(query,
						SearchCategory.ALL_NAMES,
						100,
						StarsCategory.ALL);
			}

			// somekind of error
			if (entities == null) {
				System.out.println("Please check the webservices");
				return;
			} else if (entities.getListElement().size() == 1) {
				matched++;
				writer.write(query + "\t" + entities.getListElement().get(0).getChebiId() + "\tEXACT MATCH ");

			} else if (entities.getListElement().size() == 0) {
				notmatched++;
				writer.write(query + "\t-\tNOT EXIST IN CHEBI");
			} else {
				notExact++;
				writer.write(query + "\t");

				for (int i = 0; i < entities.getListElement().size(); i++) {
					writer.write(entities.getListElement().get(i).getChebiId());
					if (i < entities.getListElement().size() - 1)
						writer.write(" | ");
				}

				writer.write("\t" + entities.getListElement().size() + " matches.");
			}

			writer.write("\n");

		} catch (Exception e) {
			error++;
			e.printStackTrace();
			writer.write(query + "\t-\tNOT EXIST IN CHEBI");
		}

	}

	public static void main(String[] args) throws Exception {
		if (args.length == 0) {
			System.out.println("PLEASE see the following example for arguments \n" +
					"java -jar QueryChebi.jar [inputfilename] [type:'DBACC', 'INCHIKEY', 'NAME'] [outputfilename] " +
					"[notificationCount]\n"+
					"ex: java -jar QueryChebi.jar hmdbIds.txt DBACC output.tsv ");
			System.exit(0);
		}

		String inputFileName = args[0];
		String type = args[1];
		String outputFileName = "Results.tsv";
		int count = 5;

		if(args.length >= 3)
			outputFileName = args[2];

		if(args.length >= 4)
			count = Integer.parseInt(args[3]);

		validateArgs(inputFileName, type);

		BufferedReader reader = new BufferedReader(new FileReader(inputFileName));
		BufferedWriter writer = new BufferedWriter(new FileWriter(outputFileName));

		String line;

		while ((line = reader.readLine()) != null) {
			total++;
			getMatchedChEBIId(line, type, writer);
			if(total%count == 0) System.out.println("Run "+total +" so far");
		}

		writer.write("\n" + "Total:" + total + ", notExact: " + notExact + ", matched: " + matched + ", notmatched: " + notmatched + ", error: " + error);
		writer.close();
	}

	private static void validateArgs(String inputFileName, String type){
		if (inputFileName.isEmpty() || type.isEmpty()) {
			System.out.println("Please check the arguments \n" +
					"See the following example arguments \n" +
					"java -jar QueryChebi.jar hmdbIds.txt DBACC output.tsv 100");
			System.exit(0);
		}

		if(! new File(inputFileName).exists()){
			System.out.println("Please check, input file does not exist");
			System.exit(0);
		}

		if (!type.equalsIgnoreCase(NAMES)
				&&!type.equalsIgnoreCase(DBACC)
				&& !type.equalsIgnoreCase(INCHIKEY)){
			System.out.println("Please check, Type does not exist \n [type:'DBACC', 'INCHIKEY', 'NAME'] ");
			System.exit(0);
		}
	}
}

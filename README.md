# QueryChEBI

QueryChEBI is a wrapper for Webservices to query a list of terms. 
All the terms are expected to be the same property.
When you run the jar please specify the type of the property that you are about to use, 
it could be either InChIkey, Name or Database Accession.

Tools Required
---------------
    . Java (6+)

How to use it 
---------------
Please run the following command to execute the jar

```
java -jar QueryChebi.jar [inputfilename] [type:'DBACC', 'INCHIKEY', 'NAME'] [outputfilename] [notificationCount]
java -jar QueryChebi.jar hmdbIds.txt DBACC output.tsv 
```
 
Output file name and Notification count are optional.
Default output file name is "Results.tsv" & Notification count is 5.

Sample files
----------- 
Input file should have only one term per line as below
```
HMDB0013131
HMDB0011313
HMDB0011329
HMDB0011296
HMDB0011280
HMDB0011264
HMDB0013456
HMDB0013455
HMDB0013453
HMDB0000098
```

Expected output is below 
```
HMDB0013131	CHEBI:88772	EXACT MATCH 
HMDB0011313	CHEBI:89460	EXACT MATCH 
HMDB0011329	CHEBI:89471	EXACT MATCH 
HMDB0011296	CHEBI:89908	EXACT MATCH 
HMDB0011280	CHEBI:89931	EXACT MATCH 
HMDB0011264	CHEBI:89964	EXACT MATCH 
HMDB0013456	CHEBI:89995	EXACT MATCH 
HMDB0013455	CHEBI:89996	EXACT MATCH 
HMDB0013453	CHEBI:89998	EXACT MATCH 
HMDB0000098	CHEBI:65327 | CHEBI:53455	2 matches.

Total:10, notExact: 1, matched: 9, notmatched: 0, error: 0

```



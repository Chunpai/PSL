package movie;

import edu.umd.cs.psl.application.inference.LazyMPEInference;
import edu.umd.cs.psl.application.learning.weight.maxlikelihood.LazyMaxLikelihoodMPE;
import edu.umd.cs.psl.config.*
import edu.umd.cs.psl.database.DataStore
import edu.umd.cs.psl.database.Database;
import edu.umd.cs.psl.database.Partition;
import edu.umd.cs.psl.database.ReadOnlyDatabase;
import edu.umd.cs.psl.database.rdbms.RDBMSDataStore
import edu.umd.cs.psl.database.rdbms.driver.H2DatabaseDriver
import edu.umd.cs.psl.database.rdbms.driver.H2DatabaseDriver.Type
import edu.umd.cs.psl.groovy.PSLModel;
import edu.umd.cs.psl.groovy.PredicateConstraint;
import edu.umd.cs.psl.groovy.SetComparison;
import edu.umd.cs.psl.model.argument.ArgumentType;
import edu.umd.cs.psl.model.argument.GroundTerm;
import edu.umd.cs.psl.model.atom.GroundAtom;
import edu.umd.cs.psl.model.function.ExternalFunction;
import edu.umd.cs.psl.ui.functions.textsimilarity.*
import edu.umd.cs.psl.ui.loading.InserterUtils;
import edu.umd.cs.psl.util.database.Queries;

ConfigManager cm = ConfigManager.getManager()
ConfigBundle config = cm.getBundle("test")

def defaultPath = System.getProperty("java.io.tmpdir")
String dbpath = config.getString("dbpath", defaultPath + File.separator + "test1PSL")
DataStore data = new RDBMSDataStore(new H2DatabaseDriver(Type.Disk, dbpath, true), config)


/* Create a PSL Model */
PSLModel model = new PSLModel(this,data);

/* Predicate Declaration */
model.add predicate: "watch"     , types: [ArgumentType.String, ArgumentType.String]
model.add predicate: "similar"   , types: [ArgumentType.String, ArgumentType.String]

/* Rule */
model.add rule: ( watch(A,X) & watch(A,Y) ) >> similar(X,Y), weight : 2, squared : false
//model.add rule: ( watch(A,X) & ~watch(A,Y) ) >> ~similar(X,Y), weight : 1, squared : false
model.add rule: ( similar(X,Y) & similar(Y,Z) ) >> similar(X,Z) , weight : 2, squared : false

model.add rule: ( ~similar(X,Y) ), weight : 1, squared : false

/* Print Model */
println model;

/* Import Data */
def partition = new Partition(0);

// insert watch
insert = data.getInserter(watch,partition);

insert.insert("A","Captain");
insert.insert("A","Nonstop");
insert.insert("B","Nonstop");
insert.insert("B","300");
insert.insert("C","Thor");
//insert.insert("C","300");

// insert similar
insert = data.getInserter(similar,partition);
insert.insert("Captain","Captain");
insert.insert("Nonstop","Nonstop");
insert.insert("300","300");
insert.insert("Thor","Thor")

Database db = data.getDatabase(partition, [watch] as Set);
LazyMPEInference inferenceApp = new LazyMPEInference(model, db, config);
inferenceApp.mpeInference();
inferenceApp.close();

/*
 * Let's see the results
 */
println "Inference results with hand-defined weights:"
for (GroundAtom atom : Queries.getAllAtoms(db, similar))
        println atom.toString() + "\t" + atom.getValue();

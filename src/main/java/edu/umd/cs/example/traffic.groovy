package traffic;

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
//ConfigBundle config = cm.getBundle("test")
ConfigBundle config = cm.getBundle("traffic")

def defaultPath = System.getProperty("java.io.tmpdir")
String dbpath = config.getString("dbpath", defaultPath + File.separator + "trafficPSL")
DataStore data = new RDBMSDataStore(new H2DatabaseDriver(Type.Disk, dbpath, true), config)


/* Create a PSL Model */
PSLModel model = new PSLModel(this,data);

/* Predicate Declaration */
model.add predicate: "adjacent"     , types: [ArgumentType.String, ArgumentType.String]
model.add predicate: "conjestion"   , types: [ArgumentType.String]

/* Rule */
model.add rule: ( adjacent(X,Y) & conjestion(X) ) >> conjestion(Y), weight : 1, squared : false
model.add rule: ( adjacent(X,Y) & conjestion(Y) ) >> conjestion(X), weight : 1, squared : false
//model.add rule:  adjacent(X,Y)  >> adjacent(Y,X), weight : 1, squared : false
//model.add rule: ( watch(A,X) & ~watch(A,Y) ) >> ~similar(X,Y), weight : 1, squared : false
//model.add rule: ( similar(X,Y) & similar(Y,Z) ) >> similar(X,Z) , weight : 1, squared : false
//model.add rule: ( ~similar(X,Y) ), weight : 1, squared : false

/* Print Model */
println model;

/* Import Data */
//def evidencePartition = new Partition(0);

// insert adjacent
/*
insert = data.getInserter(adjacent,evidencePartition);
insert.insert("1_2","2_3");
insert.insert("2_3","3_4");
insert.insert("3_4","4_5");
insert.insert("6_7","7_8");
insert.insert("7_8","8_9");
insert.insert("8_9","9_10");
insert.insert("11_12","12_13");
insert.insert("12_13","13_14");
insert.insert("13_14","14_15");
insert.insert("16_17","17_18");
insert.insert("17_18","18_19");
insert.insert("18_19","19_20");
insert.insert("21_22","22_23");
insert.insert("22_23","23_24");
insert.insert("23_24","24_25");
insert.insert("1_2","1_6");
insert.insert("6_7","1_6");
insert.insert("6_7","6_11");
insert.insert("11_12","6_11");
insert.insert("1_6","6_11");
insert.insert("11_12","11_16");
insert.insert("16_17","11_16");
insert.insert("6_11","11_16");
insert.insert("16_17","16_21");
insert.insert("21_22","16_21");
insert.insert("11_16","16_21");
insert.insert("1_2","2_7");
insert.insert("2_3","2_7");
insert.insert("6_7","2_7");
insert.insert("7_8","2_7");
insert.insert("6_7","7_12");
insert.insert("7_8","7_12");
insert.insert("11_12","7_12");
insert.insert("12_13","7_12");
insert.insert("2_7","7_12");
insert.insert("11_12","12_17");
insert.insert("12_13","12_17");
insert.insert("16_17","12_17");
insert.insert("17_18","12_17");
insert.insert("7_12","12_17");
insert.insert("16_17","17_22");
insert.insert("17_18","17_22");
insert.insert("21_22","17_22");
insert.insert("22_23","17_22");
insert.insert("12_17","17_22");
insert.insert("2_3","3_8");
insert.insert("3_4","3_8");
insert.insert("7_8","3_8");
insert.insert("8_9","3_8");
insert.insert("7_8","8_13");
insert.insert("8_9","8_13");
insert.insert("12_13","8_13");
insert.insert("13_14","8_13");
insert.insert("3_8","8_13");
insert.insert("12_13","13_18");
insert.insert("13_14","13_18");
insert.insert("17_18","13_18");
insert.insert("18_19","13_18");
insert.insert("8_13","13_18");
insert.insert("17_18","18_23");
insert.insert("18_19","18_23");
insert.insert("22_23","18_23");
insert.insert("23_24","18_23");
insert.insert("13_18","18_23");
insert.insert("3_4","4_9");
insert.insert("4_5","4_9");
insert.insert("8_9","4_9");
insert.insert("9_10","4_9");
insert.insert("8_9","9_14");
insert.insert("9_10","9_14");
insert.insert("13_14","9_14");
insert.insert("14_15","9_14");
insert.insert("4_9","9_14");
insert.insert("13_14","14_19");
insert.insert("14_15","14_19");
insert.insert("18_19","14_19");
insert.insert("19_20","14_19");
insert.insert("9_14","14_19");
insert.insert("18_19","19_24");
insert.insert("19_20","19_24");
insert.insert("23_24","19_24");
insert.insert("24_25","19_24");
insert.insert("14_19","19_24");
insert.insert("4_5","5_10");
insert.insert("9_10","5_10");
insert.insert("9_10","10_15");
insert.insert("14_15","10_15");
insert.insert("5_10","10_15");
insert.insert("14_15","15_20");
insert.insert("19_20","15_20");
insert.insert("10_15","15_20");
insert.insert("19_20","20_25");
insert.insert("24_25","20_25");
insert.insert("15_20","20_25");
// finish insert adjacent 

// insert conjestion
insert = data.getInserter(conjestion,evidencePartition);
insert.insert("1_2");
insert.insert("2_3");
insert.insert("3_4");
insert.insert("7_8");
insert.insert("8_9");
insert.insert("12_13");
insert.insert("13_14");
insert.insert("14_15");
insert.insert("18_19");
insert.insert("21_22");
insert.insert("24_25");
insert.insert("2_7");
insert.insert("17_22");
insert.insert("3_8");
insert.insert("8_13");
insert.insert("19_24");
// finish insert conjestion
*/

/*
 * Of course, we can also load data directly from tab delimited data files.
 */
 
def dir = 'data'+java.io.File.separator+'traffic'+java.io.File.separator;



for (i=0;i<289;i++) {
    /* Import Data */
    def evidencePartition = new Partition(i);

    insert = data.getInserter(adjacent, evidencePartition)
    InserterUtils.loadDelimitedData(insert, dir+"adjacent.txt");

    insert = data.getInserter(conjestion, evidencePartition)
    InserterUtils.loadDelimitedData(insert, dir+"conjestion"+i.toString()+".txt");


    Database db = data.getDatabase(evidencePartition, [adjacent] as Set);
    LazyMPEInference inferenceApp = new LazyMPEInference(model, db, config);
    inferenceApp.mpeInference();
    inferenceApp.close();

    /*
     * Let's see the results
     */
    //def number = i
    def dir2 = 'result'+java.io.File.separator;
    //def filename = 'result'+number.toString()+'.txt';
    def filename = 'traffic'+java.io.File.separator+'result'+i.toString()+'.txt';
    def file_i = new File(dir2+filename);

    println "Inference results with hand-defined weights:"
    for (GroundAtom atom : Queries.getAllAtoms(db, conjestion))
            //println atom.toString() + "\t" + atom.getValue()
            //file_i << atom.getArguments() + "\t" + atom.getValue()+"\n";
            file_i << atom.toString() + "\t" + atom.getValue()+"\n";
}

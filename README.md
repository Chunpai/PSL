###Procedures:  
1. data preprocessing, and store evidences in data/  
2. run psl to read the predicate and evidence, and do the inference  
3. store the results in result/

###installation:  
1. git clone https://github.com/linqs/psl  
2. follow instructions https://github.com/linqs/psl/wiki/Getting-Started-with-Groovy  

###Run:  
1. python extract.py
2. mvn compile  
3. mvn dependency:build-classpath -Dmdep.outputFile=classpath.out   
4. java -cp ./target/classes:`cat classpath.out` traffic.traffic

To run the program first compile the java file by using: 
	javac Apriori.java 

Then run the program with the following command: 
	java Apriori filename min.Support min.Confidence 
where, 
	   filename = path to the location of the dataset
	   min.Support = value for the minimum support threshold in float 
	   min.Confidence = value of minimum confidence threshold in float

Please make sure that you enter the min support and confidence values between 0 and 1.
And the output would generated in the same directory with the name output.txt
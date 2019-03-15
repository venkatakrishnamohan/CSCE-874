To run the program first compile the java file by using: 
	javac Kmeans.java 

Then run the program with the following command: 
	java Kmeans 

Now the program asks for different inputs from the user:

1. Path: Full path to the file (input data set).
	E.g.: /home/grad/vsunkara/iris.arff
2. Enter the number of Clusters: k
3. Enter the epsilon value which is used for stopping criteria: (Give values between 0-1, As the data is normalized it's better to give values as low and as close to 0)
4. Enter the maximum number of iterations allowed: (try to give values in 4-10 as we can observe differences in the clusters formed)

Dataset:

iris.arff is the dataset used for testing our program.
# SZZFramework
This Framework is based on the publication "A Framework for Evaluating the Results of the SZZ Approach for Identifying Bug-Introducing Changes", giving criterion to evaluate the results of SZZ.
This Framework is an open implementation of this Framework, along with scripts to adapt data from SZZ Unleashed results.

How to use :
Import the project in eclipse
Add 3 arguements : -repo "path to the git repository on your local drive"
                   -results "path to the json file containing the results from SZZ" 
                   -truth "path to the ground truth JSON"
The format are detailled in the script folder.
                   
The console will then display differents values, corresponding to different criterions. 

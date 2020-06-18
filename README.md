# SZZFramework
This Framework is based on the publication "A Framework for Evaluating the Results of the SZZ Approach for Identifying Bug-Introducing Changes", giving criterion to evaluate the results of SZZ.</br>
This Framework is an open implementation of this Framework, along with scripts to adapt data from SZZ Unleashed results.

How to use :</br>
Import the project in eclipse</br>
Add 3 arguements : </br>
                   ```-repo "path to the git repository on your local drive"```</br>
                   ```-results "path to the json file containing the results from SZZ" ```</br>
                   ```-truth "path to the ground truth JSON"```</br>
The format are detailled in the script folder.</br>
</br>
The console will then display differents values, corresponding to different criterions. 

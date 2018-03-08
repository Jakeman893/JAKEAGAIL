import os
import sys
import subprocess

save_dir = "logs/"

names = ['Abalone', 'ContinuousPeaks', 'CountOnes', 'CrossValidation',
         'FlipFlop', 'FourPeaks', 'Knapsack', 'MaxKColoring', 'NQueens',
         'TravelingSalesman', 'TwoColors', 'XOR', 'Wine', 'Student']

if len(sys.argv) > 1:
    names=sys.argv[1:]

for name in names:
    print("===========" + name + "=========")
    subprocess.call("java -cp ABAGAIL.jar opt.test." + name + "Test > logs/" + name + ".log", shell=True)

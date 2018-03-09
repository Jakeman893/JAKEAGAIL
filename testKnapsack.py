import os
import sys
import subprocess
import pandas as pd
import time

algs = ['RHC', 'SA', 'GA', 'MIMIC']

args = {}
maxN = 10

if len(sys.argv) > 1:
    for i in xrange(1, len(sys.argv)):
        if '-maxN' in sys.argv[i]:
            maxN = int(sys.argv[i+1])
        elif '-maxVal' in sys.argv[i]:
            max_val = int(sys.argv[i+1])
        elif '-maxW' in sys.argv[i]:
            max_weight = int(sys.argv[i+1])

else:
    exit()

data = {'a': [], 'n': [], 'c': [], 'max_val': [], 'max_weight': [], 'score': []}

for a in range(0, 4):
    for n in range(10, maxN+1, 10):
        for c in range(0, 5):
            # for max_val in xrange(0,50, 5):
            #     for max_weight in xrange(0,50,5):
            print(str(a) + '\t' + str(n) + '\t' + str(c) + '\t' + str(max_val) + '\t' + str(max_weight))
            data['a'].append(algs[a])
            data['n'].append(n)
            data['c'].append(c)
            data['max_val'].append(max_val)
            data['max_weight'].append(max_weight)
            time1 = time.time()
            proc = subprocess.Popen("java -cp ABAGAIL.jar opt.test.KnapsackTest -a " + str(a) + 
                                    " -c " + str(c) + " -n " + str(n) + " -max_val " + str(max_val) + 
                                    " -max_weight " + str(max_weight),
                                    stdout=subprocess.PIPE, stderr=subprocess.PIPE, shell=True)
            out, err = proc.communicate()
            time2 = time.time()
            data['time'].append((time2-time1) * 1000.0)
            data['score'].append(float(out.strip()))

df = pd.DataFrame(data)

print(df.head())

df.to_csv('logs/KnapsackExhaustive.log', index=False)

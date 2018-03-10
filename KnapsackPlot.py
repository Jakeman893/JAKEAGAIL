import os
import sys
import subprocess
import pandas as pd
import matplotlib.pyplot as plt
import math
import numpy as np
import itertools

# Number of items
N = 0
# Number of possible copies
C = 4
# Max Value
MV = 10
# Max Weight
MW = 10
# Max Knapsack Weight 40% of avaiable
MKW = 0

def main(argv):
    global N
    global C
    global MV
    global MW
    global MKW

    if len(argv) > 1:
        for i in range(1, len(argv)):
            if '-n' in argv[i]:
                N = int(argv[i+1])
            elif '-c' in argv[i]:
                C = int(argv[i+1])
            elif '-mv' in argv[i]:
                MV = int(argv[i+1])
            elif '-mw' in argv[i]:
                MW = int(argv[i+1])
    else:
        exit()

    if N == 0:
        exit()

    MKW = MW * N * C * 0.4

    items = []
    # Generate items
    for i in range(0, N):
        items.append((np.random.uniform(0.0, MV), np.random.uniform(0.0, MW)))

    combos = []
    for i in range(N-3, N):
        combos += [item for item in itertools.combinations(items,i) 
                   if sum(n for _, n in item) < MKW]

    yy = [sum(n for n, _ in combo) for combo in combos]
    # xx = range(0, len(yy))
    xx = [sum(n for _, n in combo) for combo in combos]

    fig = plt.figure()
    ax = fig.add_subplot(111)
    plt.xlabel('Combination weights')
    plt.ylabel('Fitness')
    ax.plot(yy)
    ax.set_xticklabels(xx)
    plt.show()


if __name__ == "__main__":
    main(sys.argv)
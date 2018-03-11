import os
import sys
import subprocess
import pandas as pd
import matplotlib.pyplot as plt
import math
import numpy as np

T = 0
N = 0

"""
Pass in a bitstring
"""
def fourpeaks_step(X, T):
    # if X is not str:
    #     X = format(X, '0200b')
    #     print X
    tail = trailingzeros(X)
    head = leadingones(X)
    # print tail
    # print head
    return max(tail, head) + R(tail, head, T)

def R(tail, head, T):
    global N
    if tail > T and head > T:
        return N
    else:
        return 0

def trailingzeros(X):
    return len(X) - len(X.rstrip('0'))

def leadingones(X):
    return len(X) - len(X.lstrip('1'))

def plot_simple(T, N):
    X = "".join('1' for i in range(0,N))
    yy = []
    xx = range(0, N)
    for _ in xx:
        X = (X + '0')[1:]
        # print X
        yy.append(fourpeaks_step(X, T))
    
    plt.xlabel('Bitstrings from 00...0 to 11...1')
    plt.ylabel('Fitness')

    plt.plot(xx, yy)

def plot_extended(T, N):
    yy = []
    xx = range(0, int(math.pow(N,4)))
    for X in xx:
        # print X
        yy.append(fourpeaks_step(X, T))
    
    plt.ylabel('Fitness')

    plt.plot(xx, yy)


def main(argv):
    global T
    global N
    if len(argv) > 1:
        for i in range(1, len(argv)):
            if '-t' in argv[i]:
                T = int(argv[i+1])
            elif '-N' in argv[i]:
                N = int(argv[i+1])
    else:
        exit()

    if T == 0:
        T = N/5
    
    plot_simple(T, N)

    plt.show()

if __name__ == "__main__":
    main(sys.argv)
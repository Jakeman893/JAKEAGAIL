import pandas as pd
import numpy as np
import matplotlib.pyplot as plt
from FourPeaksPlot import fourpeaks_step

df = pd.read_csv('logs/FourPeaksIterations.log')
df_KS = pd.read_csv('logs/KnapsackIterations.log')
df_NQ = pd.read_csv('logs/NQueensIterations.log')
df_NN = pd.read_csv('logs/WineIterations.log')

algs = ['FourPeaks', 'Knapsack', 'NQueens']

data = pd.DataFrame(columns=['problem', 'a', 'score', 'time', 'iters'])

for alg in algs:
    df = pd.read_csv('logs/' + alg + 'Iterations.log')
    df['problem'] = alg
    max_rhc = np.argmax(df.loc[df['a'] == 'RHC']['score'])
    max_sa = np.argmax(df.loc[df['a'] == 'SA']['score'])
    max_ga = np.argmax(df.loc[df['a'] == 'GA']['score'])
    max_mimic = np.argmax(df.loc[df['a'] == 'MIMIC']['score'])
    
    data = data.append(df.loc[max_rhc])
    data = data.append(df.loc[max_sa])
    data = data.append(df.loc[max_ga])
    data = data.append(df.loc[max_mimic])
    # print(max_rhc)

print(data)

# for alg in algs:
#     data.loc[data['problem'] == alg].plot(subplots=True, kind='bar', x='a', title=alg)

#     plt.show()


data = pd.DataFrame(columns=['problem', 'a', 'test_err', 'train_err', 'time', 'iters'])

alg = 'Wine'
df = pd.read_csv('logs/' + alg + 'Iterations.log')
df = df.replace({'MIMIC': 'BKPRP'}, regex=True)
df['problem'] = alg
max_rhc = np.argmin(df.loc[df['a'] == 'RHC']['train_err'])
max_sa = np.argmin(df.loc[df['a'] == 'SA']['train_err'])
max_ga = np.argmin(df.loc[df['a'] == 'GA']['train_err'])
max_mimic = np.argmin(df.loc[df['a'] == 'BKPRP']['train_err'])
data = data.append(df.loc[max_rhc])
data = data.append(df.loc[max_sa])
data = data.append(df.loc[max_ga])
data = data.append(df.loc[max_mimic])
# print(max_rhc)

print(data)

data.loc[data['problem'] == alg].plot(subplots=True, kind='bar', x='a', title="Neural Net")

plt.show()
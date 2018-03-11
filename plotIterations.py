import pandas as pd
import matplotlib.pyplot as plt
from FourPeaksPlot import fourpeaks_step

df = pd.read_csv('logs/FourPeaksIterations.log')

df.head()

df_RHC = df.loc[df['a'] == 'RHC']
df_SA = df.loc[df['a'] == 'SA']
df_GA = df.loc[df['a'] == 'GA']
df_MIMIC = df.loc[df['a'] == 'MIMIC']

df_RHC.plot(x='iters', subplots=True)
plt.title("RHC")
plt.show()
df_SA.plot(x='iters', subplots=True)
plt.title("SA")
plt.show()
df_GA.plot(x='iters', subplots=True)
plt.title("GA")
plt.show()
df_MIMIC.plot(x='iters', subplots=True)
plt.title("MIMIC")
plt.show()

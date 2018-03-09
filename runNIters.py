import os
import sys
import subprocess

save_dir = "logs/"

name = ""
iters = 0

if len(sys.argv) == 3:
    name=sys.argv[1]
    iters=int(sys.argv[2])
else:
    exit()

orig_stdout = sys.stdout

f = open('logs/' + name + 'Iters.log', 'w')
sys.stdout = f

for i in xrange(0,iters):
    print("===========" + name + ": " + str(i) + "=========")
    a = subprocess.Popen("java -cp ABAGAIL.jar opt.test." + name + "Test", stdout=subprocess.PIPE, stderr=subprocess.PIPE, shell=True)
    out, err = a.communicate()
    print(out)
    
sys.stdout = orig_stdout
f.close()
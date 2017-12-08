
from os import walk, system
from pprint import pprint


c2 = input("What to look for ? : ")
l = []
for a, b, c in walk("src/"):
    for c1 in c:
        if c1.endswith(".java"):
            with open(a + "/" + c1, "r") as f:
                for line in f.readlines():
                    if line.count(c2) > 0:
                        l += [c1, line.replace("\t", "").replace("\n", "")]

pprint(l)
system("pause")

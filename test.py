Python 3.6.3 (v3.6.3:2c5fed8, Oct  3 2017, 17:26:49) [MSC v.1900 32 bit (Intel)] on win32
Type "copyright", "credits" or "license()" for more information.
>>> import os
>>> list = []
>>> l = []
>>> for file in os.lisdir("/"):
	if file.endswith(".java"):
		with open(file, "r") as f:
			for line in f.readlines():
				if line.count("\"res/\"") > 0:
					l += [f]

					
Traceback (most recent call last):
  File "<pyshell#10>", line 1, in <module>
    for file in os.lisdir("/"):
AttributeError: module 'os' has no attribute 'lisdir'
>>> for file in os.listdir("/"):
	if file.endswith(".java"):
		with open(file, "r") as f:
			for line in f.readlines():
				if line.count("\"res/\"") > 0:
					l += [f]

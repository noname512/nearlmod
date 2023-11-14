def work(data):
    if type(data) == list:
        for dat in data:
            work(dat)
    elif type(data) == dict:
        for key, value in data.items():
            if key == "attachment" or key == "rotate" or key == "translate" or key == "shear" or key == "scale":
                addTime(value)
            if key == "rotate":
                addAngle(value)
        for key, value in data.items():
            work(value)

def addTime(data):
    if type(data) != list:
        return
    for dat in data:
        if type(dat) != dict:
            continue
        if "time" not in dat.keys():
            dat["time"] = 0

def addAngle(data):
    for dat in data:
        if "angle" not in dat.keys():
            dat["angle"] = 0

import json
with open('char_1014_nearl2_epoque#17.json') as file:
    data = json.load(file)
    work(data)
    with open("char_1014_nearl2_epoque#17.json", "w") as outp:
        json.dump(data, outp)
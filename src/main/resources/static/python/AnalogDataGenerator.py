import os

path = os.path
rootPath = path.abspath(path.dirname(os.getcwd()))
lines = list()



def randGenerateTag():
    with open(rootPath + "/data/RGBData.txt","r") as fi:
        lines = fi.readlines()
    for i in range(len(lines)):
        strLine = lines[i].strip()
        arr = list(strLine[1:-1].split(","))
        arr_num = [float(s) for s in arr]
        gray =arr_num[0] * 0.299 + arr_num[1] * 0.587 + arr_num[1] * 0.114
        tag = round(gray / 255,6)
        lines[i] = strLine + " tag="+ str(tag) + "\n"
    with open(rootPath + "/data/RGBTrainingData.txt","w+") as fo:
        fo.writelines(lines)

def main():
    randGenerateTag()

if __name__ == '__main__':
    main()
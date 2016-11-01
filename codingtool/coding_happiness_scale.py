from tkinter import *
from tkinter import ttk
from glob import glob
from PIL import ImageTk, Image
import csv
import os
import collections

root = Tk()

class CodingWindow(Frame):


    def __init__(self, master):
   
        Frame.__init__(self, master)
        self.images = glob("./picture/*.jpg")
        self.cur = 0
        self.data = collections.OrderedDict()
        self.setPicture()
        self.grid()
        self.master = master        
        self.master.title("Smile Coding")
           
        oneButton = Button(self, text='1',command=lambda: self.ratePicture("1"), width=4)
        oneButton.grid(row = 6, column = 0)
        twoButton = Button(self, text='2',command=lambda: self.ratePicture("2"), width=4)
        twoButton.grid(row = 6, column = 1)
        threeButton = Button(self, text='3',command=lambda: self.ratePicture("3"), width=4)
        threeButton.grid(row = 6, column = 2)
        fourButton = Button(self, text='4',command=lambda: self.ratePicture("4"), width=4)
        fourButton.grid(row = 6, column = 3)
        fiveButton = Button(self, text='5',command=lambda: self.ratePicture("5"), width=4)
        fiveButton.grid(row = 6, column = 4)
        noneButton = Button(self, text='Not rateable',command=lambda: self.ratePicture("-1"))
        noneButton.grid(row = 6, column = 5)
        self.textField = Entry(self, width=15)
        self.textField.grid(row = 0, column = 5)
        self.textField.insert(10,"Rater name")
        resumeButton = Button(self, text='resume',command=lambda: self.resume())
        resumeButton.grid(row = 1, column = 5)
        ratinglabel = Label(self, text="Freude (1=zeigt keine Freude, 5=zeigt sehr viel Freude)")
        ratinglabel.grid(row=5, column=0, columnspan = 6,sticky=W)
        backButton = Button(self, text='back',command=lambda: self.back())
        backButton.grid(row = 2, column = 5) 
        quitButton = Button(self, text='Quit',command=lambda: self.printData())
        quitButton.grid(row = 3, column = 5)  

    def ratePicture(self, value):
        imageName = self.images[self.cur][11:]
        self.data[imageName]=value
        if self.cur+1==len(self.images):
            self.printData()
        else:
            self.cur += 1
        self.setPicture()  
 
    def resume(self):
        filename = "./"+self.textField.get() + ".csv"
        if os.path.isfile(filename):
            with open(filename, newline='') as csvfile:
                reader = csv.reader(csvfile)
                for row in reader:
                    self.data[row[0]]=row[1]
                    self.cur += 1
            self.setPicture()

    def printData(self):
        filename = "./"+self.textField.get() + ".csv"
        if len(self.data)>0:
            with open(filename, 'w', newline='') as csvfile:
                datawriter = csv.writer(csvfile)
                for picture in self.data:
                    smilevalue = self.data[picture]
                    datawriter.writerow([picture,smilevalue])
        self.quit()

    def quit(self):
        root.quit()

    def back(self):
        if self.cur > 0:
            self.cur-=1
            self.setPicture()

    def setPicture(self):
        img = Image.open(self.images[self.cur])
        wpercent = (200 / float(img.size[0]))
        hsize = int((float(img.size[1]) * float(wpercent)))
        img = img.resize((200, hsize), Image.ANTIALIAS)
        self.image = ImageTk.PhotoImage(img)
        imagelabel = Label(self, image=self.image)
        imagelabel.grid(row=0, column=0, columnspan=5, rowspan=4)
    #    playertext = 'Player: '+str(self.cur+1)
    #    playerNumber = Label(self, text=playertext )
    #    playerNumber.grid(row = 0, column = 6)

    def onExit(self):
        self.printData()
 
def main():
    ex = CodingWindow(root)
    root.protocol("WM_DELETE_WINDOW", ex.onExit)      
    root.mainloop()  


if __name__ == '__main__':
    main()  

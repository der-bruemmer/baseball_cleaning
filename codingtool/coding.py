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
        
        self.textField = Entry(self, width=15)
        self.textField.grid(row = 5, column = 0)
        self.textField.insert(10,"Rater name")
        resumeButton = Button(self, text='resume',command=lambda: self.resume(),width=5)
        resumeButton.grid(row = 5, column = 1)
        trueButton = Button(self, text='True Smile',command=lambda: self.ratePicture("true"))
        trueButton.grid(row = 1, column = 2, columnspan=2)
        fakeButton = Button(self, text='Fake Smile',command=lambda: self.ratePicture("fake"))
        fakeButton.grid(row = 2, column = 2, columnspan=2)
        falseButton = Button(self, text='No Smile',command=lambda: self.ratePicture("false"))
        falseButton.grid(row = 3, column = 2, columnspan=2)
        noneButton = Button(self, text='Not rateable',command=lambda: self.ratePicture("none"))
        noneButton.grid(row = 4, column = 2, columnspan=2)
        backButton = Button(self, text='back',command=lambda: self.back())
        backButton.grid(row = 5, column = 2) 
        quitButton = Button(self, text='Quit',command=lambda: self.printData())
        quitButton.grid(row = 5, column = 3)  

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
        imagelabel.grid(row=0, column=0, rowspan=5, columnspan=2)
        playertext = 'Player: '+str(self.cur+1)
        playerNumber = Label(self, text=playertext )
        playerNumber.grid(row = 0, column = 2, columnspan=2)

    def onExit(self):
        self.printData()
 
def main():
    ex = CodingWindow(root)
    root.protocol("WM_DELETE_WINDOW", ex.onExit)      
    root.mainloop()  


if __name__ == '__main__':
    main()  

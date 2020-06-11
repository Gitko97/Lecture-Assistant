<img width="601" alt="LA logo2" src="https://user-images.githubusercontent.com/47098720/83949882-533ca980-a861-11ea-9f2a-7137893aaa6c.png">


# About Lecture Assistant
Lecture Assistant is a tool to support students who take online classes. It provides not only voice dictation and handwriting dictation but also mapping those two. Students can relive their burden on taking online classes and easily review what they've learned.


# Install
## Google API(speech-to-text)
We are using Google api so you need to download it for using our Lecture Assistant.

This is [a link to Google speech-to-text api.](https://cloud.google.com/speech-to-text)

## Libraries
* **Apache PDFBox**
This is [a link to Apache PDFBox.](https://pdfbox.apache.org/)

* **Recommand to use JDK 13.0.2**
This is [a link to download jdk13.0.2](https://www.oracle.com/java/technologies/javase/jdk13-archive-downloads.html)

* **JRE 1.8 for GUI**
We are using JRE 1.8 for GUI.

## Stereo Mix
Please enable **stereo mix** on your computer.
[This link let you know how to enble stereo mix.](https://www.howtogeek.com/howto/39532/how-to-enable-stereo-mix-in-windows-7-to-record-audio/)
If you still have a problem, [this link will help you out.](https://answers.microsoft.com/en-us/windows/forum/all/how-to-enable-stereo-mix-in-windows-10/4c51b5da-d56c-42c1-bb9d-bc9fccf8fa48)

## Step1
Select the range of screen size of your video lecture. This range will make screenshot files which will be used to compare with the original pdf file. You can easily adjust the size of the green square using dragging your mouse.

<img width="1474" alt="스크린샷 2020-06-07 오후 5 49 02" src="https://user-images.githubusercontent.com/47098720/83964364-7a3dbe80-a8e7-11ea-9f46-e81f7430c8af.png">

## Step2
Select .pdf file on your computer.

<img width="313" alt="스크린샷 2020-06-07 오후 5 49 23" src="https://user-images.githubusercontent.com/47098720/83964433-1ec00080-a8e8-11ea-9f85-b26dbde5f7be.png">
<img width="307" alt="스크린샷 2020-06-07 오후 9 51 58" src="https://user-images.githubusercontent.com/47098720/83969851-3e1d5480-a90d-11ea-8fba-0861a6018926.png">



## Step3
Select .json file on your computer. It is a Google Api speech-to-text **Key File**.

<img width="306" alt="스크린샷 2020-06-07 오후 5 57 13" src="https://user-images.githubusercontent.com/47098720/83964478-6050ab80-a8e8-11ea-9a3b-f81be3445010.png">
<img width="310" alt="스크린샷 2020-06-07 오후 9 53 48" src="https://user-images.githubusercontent.com/47098720/83969892-80469600-a90d-11ea-8079-d0324b581a0a.png">

You should select what a lecture's language is.

![image](https://user-images.githubusercontent.com/47098720/84114471-5e612680-aa67-11ea-940c-830f31d70ba2.png)


## Step4
Press **Start** button.

<img width="306" alt="스크린샷 2020-06-07 오후 5 57 13" src="https://user-images.githubusercontent.com/47098720/83964496-78282f80-a8e8-11ea-81e1-fabcac406e5d.png">
<img width="1125" alt="스크린샷 2020-06-07 오후 9 55 45" src="https://user-images.githubusercontent.com/47098720/83969925-aec47100-a90d-11ea-8387-a839b5db4350.png">



## Step5
Press **EIXT** button. Now you get a completed lecture pdf note!

<img width="307" alt="2" src="https://user-images.githubusercontent.com/47098720/83970073-96088b00-a90e-11ea-8e9b-a7968152bb09.png">

<img width="261" alt="스크린샷 2020-06-07 오후 10 33 37" src="https://user-images.githubusercontent.com/47098720/83970141-ff889980-a90e-11ea-90e8-81565d0a9f98.png">


# How Lecture Assistant makes Notes

## This is the simple algorithm how we **compare images**.

<img width="602" alt="comparepdf2" src="https://user-images.githubusercontent.com/47098720/83944111-f24eaa80-a83b-11ea-8818-b865959099e4.png">





## This is the simple algorithm  how we **compare notes**. Each number represents a single screenshot.

* *case 1*

<img width="711" alt="addnote" src="https://user-images.githubusercontent.com/47098720/83944270-1ced3300-a83d-11ea-9ca8-a95425c939c0.png">



* *case 2*

<img width="711" alt="addnote2" src="https://user-images.githubusercontent.com/47098720/83944355-e6fc7e80-a83d-11ea-8772-c9c86b6e3502.png">



* *case 3*

<img width="853" alt="addnote4" src="https://user-images.githubusercontent.com/47098720/83944695-6f7c1e80-a840-11ea-9781-847a39f15367.png">






# Relation between Classes
## Relation between the Compare class and others.
![83271875-674f2e00-a205-11ea-8b12-2f3a5df74119](https://user-images.githubusercontent.com/47098720/83944863-a6066900-a841-11ea-901b-6e0d024e7984.png)

# Contribution Guidelines
**This project adheres to Lecture-Assistant's [code of conduct.](https://github.com/Gitko97/Lecture-Assistant/blob/master/CODE_OF_CONDUCT.md)
We use [GitHub issues](https://github.com/Gitko97/Lecture-Assistant/issues) for tracking requests and bugs.**
You can see the specific README.md in each brach which helps you understand the details.

# LICENSE

[Apache License 2.0](https://github.com/Gitko97/Lecture-Assistant/blob/master/LICENSE)

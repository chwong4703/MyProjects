import webbrowser
import string
import os
import speech_recognition as sr
import time
from googletrans import Translator

# obtain audio 
rec = sr.Recognizer()

# keep looping the following function until user says No or exception is triggered 
while True:
    with sr.Microphone() as source:
        print("Hello, what are you looking for?[Direction, Translation, Searching, Open]")
        audio = rec.listen(source)
        try: 
            action = rec.recognize_google(audio).lower()
            print("I am think you are look for " + action)
        except sr.UnknownValueError:
            print("Google Speech Recognition could not understand audio")
        except sr.RequestError as e:
            print("Could not request results from Google Speech Recognition service; {0}".format(e))
        

    # find a driving direction using goolge map 
    if "direction" in action:
        # obtain audio from the microphone
        # google map prefix path
        url = 'https://www.google.com/maps/dir/'
        with sr.Microphone() as source:
            try:
                # recognize the starting point
                print("Where is your current location? ")
                audio = rec.listen(source)
                sp = rec.recognize_google(audio)
                print("Starting point is " + sp)
                # recognize the destination
                print("Where do you want to go? ")
                audio = rec.listen(source)   
                dest = rec.recognize_google(audio)
                print("Starting point is " + dest)
                print("Google Speech Recognition thinks you said " + sp +' to '+ dest)
                webbrowser.open_new(url + sp + '/' + dest)
            except sr.UnknownValueError:
                print("Google Speech Recognition could not understand audio")
            except sr.RequestError as e:
                   print("Could not request results from Google Speech Recognition service; {0}".format(e))


    # translate from Cantonese to English
    if "translation" in action: 
        # Speech recognition using Google Speech Recognition
        translator = Translator()
        with sr.Microphone() as source:
            print("Say something!(In Cantonese)")
            audio = rec.listen(source)
            try:
                speak = rec.recognize_google(audio, language='yue-Hant-HK')
                print("You said: " + speak)
            except sr.UnknownValueError:
                print("Google Speech Recognition could not understand audio")
            except sr.RequestError as e:
                print("Could not request results from Google Speech Recognition service; {0}".format(e))

        translate = translator.translate(speak, dest ='en').text
        print("In English: " + translate)

    # google search
    if "searching" in action:
        # google query prefix path
        googleQuery = 'http://www.google.com/search?q='
        with sr.Microphone() as source:
            print("What do you want to search?")
            audio = rec.listen(source)
            try:
                speak = rec.recognize_google(audio)
                print("Googling the information about " + speak)
                webbrowser.open_new(googleQuery + speak)
            except sr.UnknownValueError:
                print("Google Speech Recognition could not understand audio")
            except sr.RequestError as e:
                print("Could not request results from Google Speech Recognition service; {0}".format(e))

    # open a text file through voice control
    if "open" in action:
        # open the assigned file location path 
        os.system('C:\Users\chris\Downloads\hello.txt')


    # delay 5 seconds to wait to for restart respond
    time.sleep(5)
    print("Restart again? (YES/NO)")
    with sr.Microphone() as source:
        audio = rec.listen(source)
        try:
            restart = rec.recognize_google(audio).lower()
            print("You said " + restart)
            if (restart != "yes"): 
                break
        except sr.UnknownValueError:
            print("Google Speech Recognition could not understand audio")
            break
        except sr.RequestError as e:
            print("Could not request results from Google Speech Recognition service; {0}".format(e))
            break
    print

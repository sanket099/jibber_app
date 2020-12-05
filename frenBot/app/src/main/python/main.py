# This is a sample Python script.

# Press Shift+F10 to execute it or replace it with your code.
# Press Double Shift to search everywhere for classes, files, tool windows, actions, and settings.

import nltk
nltk.download('punkt')
from nltk.stem.porter import PorterStemmer


stemmer = PorterStemmer()
# things we need for Tensorflow
import numpy as np

#from tensorflow.keras.models import Sequential
#from tensorflow.keras.layers import Dense, Activation, Dropout
#from tensorflow.keras.optimizers import SGD
import pandas as pd
import random
import json

from tensorflow.keras.models import load_model
from os.path import dirname, join

from nltk.tokenize import word_tokenize



class Model:
    def clean_up_sentence(self, sentence):
        # tokenize the pattern - split words into array
        sentence_words = word_tokenize(sentence)
        #print(sentence_words)
        # stem each word - create short form for word
        sentence_words = [stemmer.stem(word.lower()) for word in sentence_words]
        return sentence_words

    # return bag of words array: 0 or 1 for each word in the bag that exists in the sentence
    def bow(self, sentence, words, show_details=True):
        # tokenize the pattern
        sentence_words = self.clean_up_sentence(sentence)
        # bag of words - matrix of N words, vocabulary matrix
        bag = [0] * len(words)
        for s in sentence_words:
            for i, w in enumerate(words):
                if w == s:
                    # assign 1 if current word is in the vocabulary position
                    bag[i] = 1
                    if show_details:
                       pass
        #print(np.array(bag))
        return (np.array(bag))

    def classify_local(self, sentence, intents):
        ERROR_THRESHOLD = 0.25
        filename = join(dirname(__file__), "mod.keras")
       # filename2 = join(dirname(__file__), "intents.json")
        model = load_model(filename, compile=True)



        words = []
        classes = []
        documents = []
        responses = []
        ignore_words = ['?', ',', '.']
        # loop through each sentence in our intents patterns
        for intent in intents['intents']:
            for pattern in intent['patterns']:
                # tokenize each word in the sentence
                w = nltk.word_tokenize(pattern)
                # add to our words list
                words.extend(w)
                # add to documents in our corpus
                documents.append((w, intent['tag']))
                # add to our classes list
                if intent['tag'] not in classes:
                    classes.append(intent['tag'])
        # stem and lower each word and remove duplicates
        words = [stemmer.stem(w.lower()) for w in words if w not in ignore_words]
        words = sorted(list(set(words)))
        # sort classes
        classes = sorted(list(set(classes)))

        # generate probabilities from the model
        input_data = pd.DataFrame([self.bow(sentence, words)], dtype=float, index=['input'])
        #print(input_data)
        results = model.predict([input_data])[0]
        # filter out predictions below a threshold, and provide intent index
        results = [[i, r] for i, r in enumerate(results) if r > ERROR_THRESHOLD]
        # sort by strength of probability
        results.sort(key=lambda x: x[1], reverse=True)
        return_list = []
        for r in results:
            return_list.append((classes[r[0]], str(r[1])))
        # return tuple of intent and probability

        return return_list


class FrenBotApp:

    def show_data(self, sentence):
        #print(self.username.text)
        global responses
        mod = Model()

        filename = join(dirname(__file__), "intents.json")

        with open(filename) as file:

            intents = json.load(file)

        tag = mod.classify_local(sentence,intents)[0][0]

        for tg in intents["intents"]:
            if tg['tag'] == tag:
                responses = tg['responses']

        # return mod
        return random.choice(responses)


def getResults(sente):

    fren = FrenBotApp()

    return fren.show_data(sente)

#getResults("hi there")

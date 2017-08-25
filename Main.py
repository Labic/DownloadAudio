#!/usr/bin/env python
# -*- coding: utf-8 -*-
#
#  Autor Andrei Bastos
#  You can contact me by email (andreibastos@outlook.com) or write to:
#  Via Labic/Ufes - Vitória/ES - Brazil

"""
Objetivo: receber e gerar uma lista de links para serem baixados pelo programa AudioDownload.Main.
"""

#################### Importações #################
from environs import Env
import json, datetime, time 
import requests, urllib, urllib3

from os import listdir
from os.path import isfile, join

import subprocess

#################### Variaveis ###################
env = Env()

URL_API_ARTICLES = "http://localhost/radio/article.json"
FILENAME_LIST = "ListLinksRadios.txt"
FILENAME_DOWNLOADEDS = "Downloadeds.txt"
PATH_DIR_AUDIOS = "/var/www/html/radios/"
LIST_DOMAINS_RADIOS = []
JAVA_PROGRAM = 'AudioDownload.Main'

# URL_API_ARTICLES = env('URL_API_ARTICLES')
# FILENAME_LIST = env('FILENAME_LIST')
# PATH_DIR_AUDIOS = env('PATH_DIR_AUDIOS')
# LIST_DOMAINS_RADIOS = env.list('LIST_DOMAINS_RADIOS')


################### Funções ######################
"""
pega os artigos através de uma URL_ENDPOINT
"""
def getArticles():
	params = {'dominios':LIST_DOMAINS_RADIOS}
	headers = {'user-agent': 'app-download-audio', 'content-type': 'application/json'}
	try:			
		r = requests.get(URL_API_ARTICLES, params=params, headers = headers)		
		r.raise_for_status()
		articles = r.json()		
		
		return articles
	except Exception as e:
		print e
		return {}
	pass


"""
Salva os links em um arquivo e retorna True ou False se conseguiu salvar.
"""
def saveListLinksFile(articles):
	links_str = getLinesToWrite(articles);

	# links_str = "\n".join(x for x in links);
	return saveInFile(FILENAME_LIST, links_str);

"""
Gera os links a partir dos artigos e retorna uma lista dos links
"""
def getLinesToWrite(articles):
	lines = ""	
	for article in articles:
		lines += article['url'] + "," + article["_id"] + "\n";
		pass
	return lines
	pass
	
"""
Salva uma string em um arquivo
"""
def saveInFile(filename, str_lines):
	saved = False;
	try:
		f = open(filename,'w');
		f.write(str_lines);
		f.close()		
		saved = True;
	except Exception as e:
		print e
		saved = False;
	finally:
		return saved		

"""
Baixa os Áudios usando o código em java AudioDownload.Main
"""
def downloadAudios(articles):
	global PATH_DIR_AUDIOS	
	PATH_DIR_AUDIOS += datetime.datetime.now().strftime("%Y-%m-%d") + "/"
	print subprocess.call(['mkdir', '-p', PATH_DIR_AUDIOS])
	print PATH_DIR_AUDIOS
	try:
				
		subprocess.call(['java', JAVA_PROGRAM,FILENAME_LIST, PATH_DIR_AUDIOS]) 
		downloadeds = readAudiosDownloadeds();
		
		for article in articles:
			paths = downloadeds[article["_id"]] 
			audio = {}
			audio["contentUrl"] = paths[0]
			audio["filename"] = paths[1]
			article["audio"] = audio
			pass		

		return articles

	except Exception as e:
		raise e
	pass

"""
Ler o arquivo gerado pelo java para saber os audios que foram salvos
"""
def readAudiosDownloadeds():
	downloadeds = {}
	f = open(FILENAME_DOWNLOADEDS, 'r')
	for line in f.readlines():
		line  = line.replace("\n","")
		_id = line.split(",")[0]
		downloadeds[_id] = [line.split(",")[1],line.split(",")[2]]		
	return downloadeds
	

"""
Atualiza os artigos no endpoints URL_API_ARTICLES 
"""
def updateArticles(articles):
	pass

def sendArticle(article):
	data=json.dumps(data)
	try:
		headers = {'user-agent': 'app-download-audio', 'content-type': 'application/json'}		

		r = requests.post(URL_API_DATABASE, data=data, headers=headers)
		r.raise_for_status()		
		output = {'ok':1, 'result':'sucess', 'msg':'Salvo com sucesso'}			
	except (requests.exceptions.HTTPError, Exception) as error:
		output = {'ok':1,'result':'error', 'msg':error}			
	finally:
		return output

def main():
	# Procura os ultimos links das rádios 
	articles = getArticles(); 	

	# Gera a lista e salva no arquivo FILENAME_LIST
	saved = saveListLinksFile(articles);

	try:
		# Baixa os áudios na pasta correta e Adiciona um campo 'audio'
		if (saved):
			articles = downloadAudios(articles);   #[{"id":"10", "url":"http://",  "audio":{"contentUrl":"". "filename":"radio_id", "url":""}}]
			# print json.dumps(articles, indent=4)

		# Atualiza no banco usando o endpoint article['audio'] =  { contentUrl: 'http...' } 
		updateArticles(articles);
		
	except Exception as e:
		print e
	



if __name__ == '__main__':
	main()
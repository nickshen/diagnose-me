__author__ = 'nick'

import requests  #request  info from web (make sure you install this)
from bs4 import BeautifulSoup   # web sort though data (and this)
from mechanize import Browser
import mechanize
import time

def crawl_list(max_pages):
    text_file = open("output.txt", "w")
    b = Browser()
    place = 65
    count = 0
    while place <= max_pages:
        url = "http://www.nhs.uk/Conditions/Pages/BodyMap.aspx?Index=" + chr(place)
        print("\n" + chr(place) + "\n__________________________________________________________")
        text_file.write( ("\n" + chr(place) + "\n__________________________________________________________").encode('utf-8'))
        page = b.open(url)

        if page.code < 400:
            html = page.read()
            soup = BeautifulSoup(html)

            for sector in soup.findAll('div', {'class' : 'index-section'}):
                for link in sector.findAll('a'):
                    href = link.get('href')
                    count += 1
                    print("+++++ " + link.string)
                    text_file.write(("\n+++++ " + link.string).encode('utf-8') )
                    if "http://www.nhs.uk/" not in href:
                        #go into link and do shit
                        page_read("http://www.nhs.uk/" + href, text_file)
                    else:
                        page_read(href, text_file)



        place += 1
    text_file.close()

def page_read(given_url, text):
    b = Browser()
    try:
        page = b.open(given_url)

        if page.code < 400:
            html2 = page.read()
            soup2 = BeautifulSoup(html2)

            for content2 in soup2.findAll('div', {'class' : 'healthaz-content'}):
                for listelem in content2.findAll('li'):
                    if listelem.string is not None:
                        print("* " + listelem.string)
                        text.write( ("\n* " + listelem.string).encode('utf-8') )


            ## finds description
            for content in soup2.findAll('div', {'class' : 'healthaz-content'}):
                if content.contents[3].strong is not None and content.contents[3].strong.string is not None:
                    print("-- " + content.contents[3].strong.string)
                    text.write( ("\n-- " + content.contents[3].strong.string).encode('utf-8') )
    except (mechanize.HTTPError, mechanize.URLError) as e:
        if isinstance(e,mechanize.HTTPError):
            print e.code
        else:
            print e.reason.args



crawl_list(25 + 65)
import os
import json
import re
import argparse


def edit_bugfix(bugfix, bugintro):

    issue_list = []
    issue_count = 0
    current_issue = Bugfix()
    
    with open(bugfix, encoding="utf8") as f:
        for line in f:
            id, idRepo, hash, idIntro, date = line.split(',')
            current_issue.hash = hash
            current_issue.id = id
            current_issue.date = date[:-4] + " +0000"
            if(idRepo == str(327)): #A changer pour l'ID du projet que l'on souhaite
                intro = hashIntroGetter(idIntro, bugintro)
                if(intro != None):
                    current_issue.hashIntro = intro
                    issue_list.append(current_issue)
            current_issue = Bugfix()
    return issue_list


def issue_list_toJSONBuglist(issue_list):

    json_list = []
    
    for issue in issue_list:
        json_list.append(issue.toJSONBug() + ", ")

    
    return "{" + ''.join(json_list)[:-2] + "}"

def issue_list_toJSONTruth(issue_list):

    json_list = []
    
    for issue in issue_list:
        entry = issue.toJSONTruth()
        if(entry != None):
            json_list.append(entry + ", ")

    
    return "{" + ''.join(json_list)[:-2] + "}"

def hashIntroGetter(idIntro, bugintro):
    
    with open(bugintro, encoding="utf8") as f:
        for line in f:
            id, hash = line.split(',')
            if(id==idIntro):
                return hash[:-1]
        return None

class Bugfix:
    hash = None
    id = None
    date = None
    hashIntro = None
    
    def toJSONBug(self):
        if self.hash == None or self.id == None or self.date == None:
            print("Missing value in Bugfix")
            return None
        
        json = '"ISSUE-' + str(self.id) + '": {"creationdate": "' + self.date +'", "resolutiondate": "' + self.date + '", "hash": "' + self.hash + '", "commitdate": "' + self.date + '"}'
        return json
    
    def toJSONTruth(self):
        if self.id == None or self.hashIntro == None or self.hash == None:
            print("Missing value in Bugfix")
            return None
        
        json = '"ISSUE-' + self.id + '": {"fix": "' + self.hash +'", "introducer": "' + self.hashIntro + '"}'
        return json

    
def main():
    """ Main method """
    parser = argparse.ArgumentParser(description="""Transform list of bugfix given into a standard issue_list""")
    
    parser.add_argument('--bugfix', type=str,
                        help='Path to the list of fix commits')
    parser.add_argument('--bugintro', type=str,
                        help='Path to the list of bug intro id/hash')
    args = parser.parse_args()
    

    issue_list = edit_bugfix(args.bugfix, args.bugintro)
    
    with open('issue_list.json', 'w') as f:
        f.write(issue_list_toJSONBuglist(issue_list))
    with open('truth.json', 'w') as f:
        f.write(issue_list_toJSONTruth(issue_list))

if __name__ == '__main__':
    main()

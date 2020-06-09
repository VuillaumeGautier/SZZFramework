import os
import json
import re
import argparse
import re

def edit_bugfix(pairs):

    issue_list = []
    issue_count = 0
    
    with open(pairs, encoding="utf8") as f:
        filetext = f.read()
        matches = re.findall('"[\d|a-f]*","[\d|a-f]*"', filetext)

    issue = IntroList()
    issue.hashIntroducers = []
    for match in matches:
        
        values = match.split('","')
        fix = values[0][1:]
        introducer = values[1]
        
        if issue.fixHash == None:
            issue.fixHash = fix
            issue.id = issue_count
            issue_count = issue_count + 1
            
        if issue.fixHash == fix:
            issue.hashIntroducers.append(introducer[:-1])
            
        else:
            issue_list.append(issue)
            issue = IntroList()
            issue.hashIntroducers = []

            
    return issue_list_toJSON(issue_list)

def issue_list_toJSON(issue_list):

    json_list = []
    
    for issue in issue_list:
        json_list.append(issue.toJSON() + ", ")

    
    return "{" + ''.join(json_list)[:-2] + "}"


class IntroList:
    id = None
    fixHash = None
    hashIntroducers = None
    
    def toJSON(self):
        if self.fixHash == None or self.hashIntroducers == None or self.id == None:
            print("Missing value in Bugfix")
            print(self.fixHash)
            print(self.hashIntroducers)
            return None
        
        json_head = '"' + str(self.id) + '" : {"fix": "' + self.fixHash +'" , "introducers" : { '
        json_foot = '}}'
        hash_list = []
        count = 0
        hashIntroducers = list(set(self.hashIntroducers))
        for hashIntroducer in hashIntroducers:
            hash_list.append('"intro-' + str(count) + '" : "' + hashIntroducer + '",')
            count = count + 1
        
        return json_head + ''.join(hash_list)[:-1] + json_foot

def main():
    """ Main method """
    parser = argparse.ArgumentParser(description="""Transform dump of bugfix given into a txt list of issue to use in merge or json builder""")
    
    parser.add_argument('--pairs', type=str,
                        help='Path to the file to transform into an bugfix_list.txt')
    args = parser.parse_args()

    issue_list_dump = edit_bugfix(args.pairs)
    with open('introducers.json', 'w') as f:
        f.write(issue_list_dump)

if __name__ == '__main__':
    main()

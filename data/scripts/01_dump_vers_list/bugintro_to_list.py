import os
import json
import re
import argparse
import re

def edit_bugfix(bugfix):

    issue_list = []
    issue_count = 0
    current_issue = Bugfix()
    
    with open(bugfix, encoding="utf8") as f:
        filetext = f.read()
        matches = re.findall("\d+,\d{3},\d*,blamed_commit,[\d | a-f]+,,[^,]+,[^,]+,[^,]+", filetext)

    issue = Bugfix()
    for match in matches:
        
        values = match.split(',')
        
        issue.hash = values[4]
        issue.id = values[0]
        issue_list.append(issue)
        
        issue = Bugfix()
    
    return issue_list_dump(issue_list)

def issue_list_dump(issue_list):
    dump = ""
    for issue in issue_list:
        dump += issue.toDump()
    return dump

class Bugfix:
    hash = None
    id = None
    
    def toDump(self):
        if self.hash == None or self.id == None:
            print("Missing value in Bugfix")
            return None
        
        dump = self.id + ',' + self.hash + '\n'

        return dump

def main():
    """ Main method """
    parser = argparse.ArgumentParser(description="""Transform dump of bugintroducer given into a txt list of issue to use in merge""")
    
    parser.add_argument('--bugfix', type=str,
                        help='Path to the file to transform into an bugfix_list.txt')
    args = parser.parse_args()

    issue_list_dump = edit_bugfix(args.bugfix)
    with open('bugintro_list.txt', 'w') as f:
        f.write(issue_list_dump)

if __name__ == '__main__':
    main()

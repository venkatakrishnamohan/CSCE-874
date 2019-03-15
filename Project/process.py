import numpy as np
import pandas as pd
import glob
import random
import tensorflow as tf
from difflib import SequenceMatcher
def main(argv):
    mod_dist = pd.read_csv('to_be_modified.csv')    
    fin = pd.read_csv('total_final.csv')
    fin['State_Name'] = fin['State Name'].str.strip().str.lower()
    fin['District_Name'] = fin['District Name'].str.strip().str.lower()
    fin['Tehsil_name'] = fin['Tehsil name'].str.strip().str.lower()
    fin['Name_x'] = fin['Name_x'].str.strip().str.lower()
    for idx,row in mod_dist.iterrows():
        a = fin[(fin['State_Name'] == row['STATE']) & (fin['District_Name']== row['DISTRICT'])]
        max_sim =0
        sub_dist = ''
        for idx2,row2 in a.iterrows():
            sim=SequenceMatcher(a=str(row['SUB_DIST']),b=str(row2['Tehsil_name'])).ratio()
            if sim>max_sim:
                max_sim = sim
                sub_dist = row2['Tehsil_name']
        b = a[a['Tehsil_name'] == sub_dist]
        max_sim2=0
        name_f = ''
        for idx2,row2 in b.iterrows():
            if str(row['NAME']) in str(row2['Name_x']):
                name_f = row2['Name_x']
                break
            sim=SequenceMatcher(a=str(row['NAME']),b=str(row2['Name_x'])).ratio()
            if sim>max_sim2:
                max_sim2 = sim
                name_f = row2['Name_x']
        c = b[b['Name_x'] == name_f]
        if row['TYPE'] == 'Village':
            mod_dist[idx,'Name_x'] = c.iloc[0]['Name_x']
        else:
            d = fin[fin['code'] == c.iloc[0]['code']]
            n = random.randint(0,d.shape[0]-1)
            mod_dist[idx,'Name_x'] = d.iloc[n]['Name_x']
    mod_dist.to_csv('new.csv')

if __name__ == "__main__":
    tf.app.run()

if __name__ == '__main__':
    infile = open('Oneday.txt','r')
    lines = infile.readlines()
    infile.close()
    count = 0
    obs = []
    for idx,line in enumerate(lines):
        fields = line.strip().split(' ')
        if fields[0] == 'SECTION3':
            #print idx
            idx += 2
            while fields[0] != 'END':
                obs.append(lines[idx])
                #print idx
                idx += 1
                fields = lines[idx].strip().split(' ')
            break
    #print obs 
                
    edges = []
    outfile = open('data/traffic/adjacent.txt','w')
    
    for line in obs:
        fields = line.strip().split(' ')
        length_obs = len(fields)
        print length_obs
        v1 = fields[0]
        v2 = fields[1]
        if v1 == v2:
            print 'ERROR'
            continue
        e = (v1,v2)
        if e in edges:
            print 'ERROR2'
            print e
            continue

        for source,target in edges:
            if source == v1 or source == v2:
                #outfile.write('insert.insert('+'"'+str(source)+'_'+str(target)+'"'+','+'"'+str(v1)+'_'+str(v2)+'"'+');\n')
                outfile.write(str(source)+'_'+str(target)+'\t'+str(v1)+'_'+str(v2)+'\n')
            elif target == v1 or target == v2:
                #outfile.write('insert.insert('+'"'+str(source)+'_'+str(target)+'"'+','+'"'+str(v1)+'_'+str(v2)+'"'+');\n')
                outfile.write(str(source)+'_'+str(target)+'\t'+str(v1)+'_'+str(v2)+'\n')
        edges.append(e)
    outfile.close()
    

    for i in range(length_obs-2):
        outfile_i = open('data/traffic/conjestion'+str(i)+'.txt','w')
        for line in obs:
            fields = line.strip().split(' ')
            v1 = fields[0]
            v2 = fields[1]
            conj = int(fields[i+2])
            if conj == 1:
                #outfile_i.write('insert.insert('+'"'+str(v1)+'_'+str(v2)+'"'+');\n')
                outfile_i.write(str(v1)+'_'+str(v2)+'\n')
        outfile_i.close()

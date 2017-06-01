from scipy.stats import beta as beta_dist
import re
import numpy as np
'''
data=beta.rvs(5,2,loc=0,scale=1,size=20)
print type(data[0])
alpha, beta2, loc, scale = beta.fit(data)
print alpha, beta2, alpha/(alpha + beta2), 5/7.0
data=beta.rvs(5,2,loc=0,scale=1,size=20)
alpha, beta2, loc, scale = beta.fit(data)
print alpha, beta2, alpha/(alpha + beta2), 5/7.0
'''
obs_dict = {}
for i in range(289):
    infile = open('data/traffic/result/result'+str(i)+'.txt','r')
    for line in infile:
        line = line.strip()
        fields = re.split("\'|\t",line)
        edge = fields[1]
        prob = float(fields[3])
        print fields
        if edge not in obs_dict:
            obs_dict[edge] = []
            obs_dict[edge].append(prob)
        else:
            obs_dict[edge].append(prob)
print obs_dict

print 'edge\t alpha\t beta' 
for edge in obs_dict.keys():
    #data = np.array(obs_dict[edge],dtype=np.float64)
    data = obs_dict[edge]
    alpha, beta, loc, scale = beta_dist.fit(data)
    print edge, alpha, beta


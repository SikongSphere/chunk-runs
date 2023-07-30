# 从csv中读取，csv结构geonameid,asciiname,latitude,longitude,dem,
# 将十进制度表示的坐标d转换为度分秒表示的坐标

import random


# 随机从南北纬选一个
def random_lat_ns():
    lat_ns = [' N', ' S']
    return lat_ns[random.randint(0, 1)]


# 随机从东西经选一个
def random_lon_ew():
    lon_ew = [' E', ' W']
    return lon_ew[random.randint(0, 1)]


def degree_to_dms(coord):
    d = int(coord)
    m = int((coord - d) * 60)
    s = round((coord - d - m / 60) * 3600, 2)
    return str(d) + '°' + str(m) + '\'' + str(s) + '"'


input_file = r'./dataset/geonames-lat-lon.csv'
output_file = r'./dataset/geonames-lat-lon-dms.csv'

out_f = open(output_file, 'w', encoding='utf-8')
with open(input_file, 'r', encoding='utf-8') as in_f:
    lines = in_f.readlines()
    out_f.writelines(lines[0])
    for line in lines[1:]:
        try:
            # print(line)
            line = line.strip()
            line = line.split(',')
            geonameid = line[0]
            asciiname = line[1]
            latitude = degree_to_dms(float(line[2]))
            longitude = degree_to_dms(float(line[3]))
            dem = line[4]
            new_line = geonameid + ',' + asciiname + ',' + latitude + random_lat_ns() + ',' + longitude + random_lon_ew() + ',' + dem + ',' + '\n'
            # print(new_line.strip('\n'))
            out_f.write(new_line)
        except:
            continue

    in_f.close()
out_f.close()

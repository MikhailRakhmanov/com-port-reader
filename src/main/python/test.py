import fdb
import time

connect = fdb.connect(
    dsn='192.168.1.44:C:/DataBase/16112023V135.FDB',
    fb_library_name='C:/Program Files/Firebird/Firebird_2_1/bin/fbclient.dll',
    user='sysdba',
    password='masterkey',
    charset='WIN1251')
cursor = connect.cursor()


def send(data: list):
    result = []
    for item in data:
        cursor.execute(f'select * from V0877_C2({item[0]}) order by np desc ')
        result += cursor.fetchall()
    return result


def get():
    cursor.execute('select * from V0172_3_C1(15,16267820,0) where DOTPRN is null order by idbrig,wdate')
    result = cursor.fetchall()
    return result


def my_send(data: list):
    i = 1
    total_result = []
    for el in data:
        cursor.execute(f"select IDZMAT,IDPACKET from ZMAT where  (IDZMAT = {el[0]})")
        lines = cursor.fetchall()
        for idzmat, idpacket in lines:
            idbrig = None
            brig = None
            if idpacket > 0:
                cursor.execute(f'select distinct IDBRIG,DOT2 from base_baza  where IDPACKETHAFF={idpacket}')
                idbrig, wdate = cursor.fetchone()
                if idbrig is not None and idbrig > 0:
                    cursor.execute(f'select STRVALUE from TBL_LISTVAR where TDEF_ID = 292 and id={idbrig}')
                    brig = cursor.fetchone()[0]

            cursor.execute(
                f"select barcode,NUMDOG,a.NPOS, b.pos,b.caption,b.h,b.w,a.notes,typeizd from listizd a, packetinizd b, ZMATLIST c, dogovor d where b.idizd = a.id and a.id=c.idizd and c.IDCOMP = b.IDGRAPH and c.IDZMAT = {idzmat} and b.IDMAT not in (5705806,5705807)  and a.iddog=d.iddogovor order by a.npos,b.pos")
            result = list(cursor.fetchall());
            for item in result:
                item = list(item)
                item[1] = str(item[1]) + " " + str(item.pop(2)) + '.' + str(item.pop(2))
                item.append(brig)
                item.append(i)
                total_result.append(item)
                i += 1

    return total_result


data = get()
start_time = time.time()
my_send(data)
print(*my_send(data), sep='\n')
connect.close()
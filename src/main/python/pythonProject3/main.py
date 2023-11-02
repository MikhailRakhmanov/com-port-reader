import os
import requests
import serial
import win32api
import win32print
from pyhtml2pdf import converter

port = os.getenv('COM')
baudrate = 9600


class Data:
    def __init__(self, platform=None, product=None):
        self.platform = platform
        self.product = product


try:
    ser = serial.Serial(port, baudrate=baudrate)
    print("Выполнено подключение.")
    data = Data()
    while True:
        barcode = int(ser.readline().decode().strip())
        if barcode:
            if barcode == 99999994:
                print('Печать...')
                converter.convert(f"http://{os.getenv('IP_AND_PORT')}/print", "result.pdf", power=2,
                                  print_options={"scale": 0.75})
                win32api.ShellExecute(
                    0,
                    "print",
                    'C:/Users/Sysadm/PycharmProjects/pythonProject3/result.pdf',
                    '/d:"%s"' % win32print.GetDefaultPrinter(),
                    ".",
                    0
                )
                print('Файл распечатан.')
                continue

            if barcode <= 215 or barcode == 666:
                data = Data(platform=barcode, product=None)
                print(f'Пирамида: {data.platform}')
            elif data.platform is not None:
                data.product = barcode
                print(f'{data.platform}: {data.product}')

            response = requests.post(f"http://{os.getenv('IP_AND_PORT')}/api/sp_data", json=data.__dict__)

except serial.SerialException as se:
    print("Serial port error:", str(se))

except KeyboardInterrupt:
    pass

finally:
    if ser.is_open:
        ser.close()
        print("Serial connection closed.")

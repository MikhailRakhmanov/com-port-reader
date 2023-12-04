import requests


class Data:
    def __init__(self, platform=None, product=None):
        self.platform = platform
        self.product = product



data = Data(platform=10, product=None)


response = requests.post("http://192.168.1.63/api/sp_data", json=data.__dict__)
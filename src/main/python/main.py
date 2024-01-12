import requests


class Data:
    def __init__(self, platform=None, product=None):
        self.platform = platform
        self.product = product



data = Data(platform=10, product=13217)
print(data.__dict__)

response = requests.post("http://localhost:8080/api/sp_data", json=data.__dict__)
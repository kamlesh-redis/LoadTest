from locust import HttpUser, task

class HelloWorldUser(HttpUser):
    
    @task(1)
    def update(self):
        self.client.get("/update")

    @task(4)
    def search(self):
        self.client.get("/hello")

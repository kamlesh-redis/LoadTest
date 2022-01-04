import time
import redis
import random

r = redis.Redis(host="redis-15547.c17333.us-east1-mz.gcp.cloud.rlrcp.com", 
    port="15547", decode_responses=True, password="j3fzUXi7jS07GV137C4FWlxb1qnwX9ZU") #, socket_timeout=1);#, password="password");
# r = redis.Redis(host="10.58.208.244") #, socket_timeout=1);#, password="password");

def load():
    i=0
    while i <= 700:
        key = "Location:" + str(i)

        max = random.randint(0, 99999)
        act = random.randint(0, max)

        r.hmset(key, {
            "max_pick": max,
            "active_pick": act,
            "location": i
            }
        )
        i = i + 1

def update():
    while True:
        key = "Location:" + str(random.randint(0, 700))

        max = random.randint(0, 99999)
        act = random.randint(0, max)

        r.hmset(key, {
            "max_pick": max,
            "active_pick": act
            }
        )

if __name__ == '__main__':
    # update()
    load()
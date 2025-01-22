# Start the Flask server
- Execute `python3 -m venv venv`
- Execute `source venv/bin/activate`
- Execute `python3 flask-app.py`

**Note:**

- The server is reachable on http://0.0.0.0:5001 or http://IP:5001
- Port can be changed accordingly in flask-app.py
- Server supports two paths:
  - GET (/hello): http://0.0.0.0:5001/hello
  - POST (/data): `curl -X POST http://192.168.0.166:5001/data -H "Content-Type: application/json" -d '{"key": "value"}'`
- The server will also create a file using the first key of JSON
  - E.g., If POST JSON is `{"key": "value", "key1": "value1"}` then a `.json` file will be created under `devices/key.json` using the value of the first key

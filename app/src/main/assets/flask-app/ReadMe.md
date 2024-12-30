# Start the Flask server
- Execute `python3 -m venv venv`
- Execute `source venv/bin/activate`
- Execute `python3 flask-app.py`

**Note:**

- The server is reachable on http://0.0.0.0:5001 or http://IP:5001
- Port can be changes accordingly in flask-app.py
- Server support two path:
  - GET (/hello): http://0.0.0.0:5001/hello
  - POST (/data): `curl -X POST http://192.168.0.166:5001/data -H "Content-Type: application/json" -d '{"key": "value"}'`
- The server will also create a file using the first key of JSON
  - For e.g., If POST JSON is `{"key": "value", "key1": "value1"}` the a `.json` file will be create under `devices/key.json`

from flask import Flask, request, jsonify
import json
import os

app = Flask(__name__)

@app.route('/data', methods=['POST'])
def receive_data():
    data = request.get_json()
    os.makedirs('devices', exist_ok=True)
    
    # Save to a JSON file
    with open(f"devices/{data['serial_number']}.json", 'w') as file:
        json.dump(data, file, indent=4)
    print(json.dumps(data, indent=4))
    return jsonify({"status": "success", "data": data}), 200

@app.route('/hello', methods=['GET'])
def say_hello():
    return "Hello", 200

if __name__ == '__main__':
    app.run(host='0.0.0.0', port=5001)

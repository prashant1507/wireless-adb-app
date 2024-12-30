import sqlite3
import csv

def load_csv_to_db(csv_file_path, db_file_path):
    # Connect to SQLite database
    conn = sqlite3.connect(db_file_path)
    cursor = conn.cursor()

    # Create a table (if not already exists)
    cursor.execute('''
        CREATE TABLE IF NOT EXISTS devices (
            Retail_Branding TEXT,
            Marketing_Name TEXT,
            Device TEXT,
            Model TEXT
        )
    ''')

    # Attempt to read the CSV with UTF-16 encoding
    try:
        with open(csv_file_path, mode='r', encoding='utf-16') as file:
            csv_reader = csv.DictReader(file)

            # Clean up the headers by stripping null bytes
            headers = [header.replace('\x00', '') for header in csv_reader.fieldnames]
            print(f"Cleaned Headers: {headers}")

            # Iterate through the rows and insert data into the database
            for row in csv_reader:
                cursor.execute('''
                    INSERT INTO devices (Retail_Branding, Marketing_Name, Device, Model)
                    VALUES (?, ?, ?, ?)
                ''', (row[headers[0]], row[headers[1]], row[headers[2]], row[headers[3]]))
    except UnicodeDecodeError as e:
        print(f"Failed to read with encoding UTF-16: {e}")
    except KeyError as e:
        print(f"Failed to access columns: {e}")

    # Commit and close connection
    conn.commit()
    conn.close()

# Example usage
# https://storage.googleapis.com/play_public/supported_devices.html
csv_file_path = 'supported_devices.csv'  # Path to the CSV file
db_file_path = 'devices.db'  # Path where you want the database to be stored

load_csv_to_db(csv_file_path, db_file_path)

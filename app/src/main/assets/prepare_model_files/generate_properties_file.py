import csv

# File paths
input_file = "supported_devices.csv"  # Replace with the path to your CSV file
output_file = "../devices.properties"  # Replace with the desired output file path

# Open the input CSV and output properties file
with open(input_file, mode='r', encoding='utf-16') as csv_file, open(output_file, "w") as properties_file:
    reader = csv.DictReader(csv_file)

    for row in reader:
        model = row["Model"].strip()
        marketing_name = row["Marketing Name"].strip()

        # Write to properties file in the desired format
        properties_file.write(f"{model}={marketing_name}\n")

print(f"Properties file generated: {output_file}")

# https://storage.googleapis.com/play_public/supported_devices.html

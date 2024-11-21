from flask import Flask, request, jsonify
import joblib
import numpy as np

app = Flask(__name__)

# Load the pre-trained model
model = joblib.load('model.pkl')

# Root route
@app.route('/', methods=['GET'])
def home():
    return "Welcome to the Predictor API", 200

@app.route('/predict', methods=['POST'])
def predict():
    # Get input data from the request
    data = request.json
    
    # Extract features from the input data
    gender = data['gender']
    educ = data['educ']
    mmse = data['mmse']
    nwbv = data['nwbv']
    cdr = data['cdr']
    
    # Convert gender to numerical value (0 for male, 1 for female)
    gender_numeric = 0 if gender == 'male' else 1
    
    # Combine features into a numpy array
    features = np.array([[cdr, mmse, nwbv, educ, gender_numeric]])
    
    # Make predictions using the model
    prediction = model.predict(features)
    
    # Prepare response
    response = {'prediction': prediction.tolist()}
    
    return jsonify(response)

if __name__ == '__main__':
    app.run(debug=True)

import json
import os
from flask import Flask, current_app, request, send_from_directory, make_response
from itsdangerous import TimedJSONWebSignatureSerializer as Serializer

app = Flask(__name__)
app.config['SECRET_KEY'] = os.urandom(24)
app.config['UPLOAD_FOLDER'] = os.getcwd() + "/userfile/"


@app.route('/word', methods=['GET'])
def word():
    return app.send_static_file('word.db')


@app.route('/quote', methods=['GET'])
def quore():
    return app.send_static_file('quote.db')


@app.route('/login', methods=['POST'])
def login():
    username = request.form.get('username')
    password = request.form.get('password')

    if username is not None and password is not None:
        with open("users.json", 'r') as f:
            users = json.load(f)

        for user in users['users']:
            if username == user['username'] and password == user['password']:
                serializer = Serializer(current_app.config["SECRET_KEY"], expires_in=2592000)
                token = serializer.dumps({"id": username}).decode("utf8")

                return token

        return "Bad authentication."


@app.route('/api/upload', methods=['POST'])
def upload():
    token = request.form.get("token")

    serializer = Serializer(current_app.config['SECRET_KEY'])

    try:
        user = serializer.loads(token)
    except Exception:
        return "Bad authentication."

    db = request.files.get('db')
    if db and (db.filename == user['id'] + ".sqlite"):
        db.save(os.path.join(app.config['UPLOAD_FOLDER'], db.filename))

        return "Successfully uploaded."

    return "Upload failed."


@app.route('/api/fetch', methods=['POST'])
def fetch():
    token = request.form.get("token")

    serializer = Serializer(current_app.config['SECRET_KEY'])

    try:
        user = serializer.loads(token)
    except Exception:
        return "Bad authentication."

    response = make_response(send_from_directory(os.path.join(app.config['UPLOAD_FOLDER']), user['id'] + ".sqlite",
                                                 as_attachment=True))

    return response


if __name__ == "__main__":
    app.run()

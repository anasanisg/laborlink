from flask import Blueprint, jsonify, request
from .driver import save_qr_code,generate_jwt

main_bp = Blueprint("main", __name__)

@main_bp.route("/obtain", methods=["POST"])
def obtain():
    res = request.get_json()
    save_qr_code(generate_jwt(res['username']),f'{res['username']}.png')
    return request.get_data(), 201


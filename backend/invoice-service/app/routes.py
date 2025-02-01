from flask import Blueprint, jsonify, request,send_file
from .driver import storeInStorage
from .mongo_client import sv_in_collection,get_all_invoices_related_to_owner
import os;



main_bp = Blueprint("main", __name__) 



@main_bp.route("/invoice", methods=["GET"])
def get_invoices_by_owner():

    owner_id = request.args.get("ownerId")

    ret = get_all_invoices_related_to_owner(owner_id)

    return jsonify(ret)



@main_bp.route('/invoice/get/<string:id>')
def download_invoice_by_activity_id(id):
    return send_file(f'{os.getenv('STORAGE')}/invoice_{id}.pdf',as_attachment=True)
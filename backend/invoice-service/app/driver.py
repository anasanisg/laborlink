from reportlab.pdfgen import canvas
from reportlab.lib.pagesizes import letter
import os



def storeInStorage(json):


    # Extract
    activity_id = json.get("activityId")
    owner_id = json.get("ownerId") 
    total_price = json.get("totalPrice")
    total_rented_tools = sum(item.get("quantity", 0) for item in json.get("items", [])) # sum of all quantities
    total_renting_time = json.get("totalRentingTimeInSeconds")


    pdf_path = os.path.join(os.getenv('STORAGE'), f"invoice_{activity_id}.pdf")


    # PDF Generating
    pdf = canvas.Canvas(pdf_path, pagesize=letter)
    pdf.drawImage(os.getenv('LOGO_PATH'), x=(letter[0] - 72) / 2, y=720, width=72, height=72)
    pdf.drawString(100, 700, "Invoice Details")
    pdf.drawString(100, 680, f"Activity ID: {activity_id}")
    pdf.drawString(100, 660, f"Owner ID: {owner_id}")
    pdf.drawString(100, 640, f"Total Price: ${total_price:.2f}")
    pdf.drawString(100, 620, f"Total Rented Tools: {total_rented_tools}")
    pdf.drawString(100, 600, f"Total Renting Time (seconds): {total_renting_time}")
    pdf.save()

   
    invoice_path = 'LATER'


    ret = {
        "activityId": activity_id,
        "ownerId": owner_id, 
        "totalPrice": total_price,
        "totalRentedTools": total_rented_tools,
        "totalRentingTimeInSeconds": total_renting_time,
        "invoiceLink": invoice_path
    }

    return ret






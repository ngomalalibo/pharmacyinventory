package mgt.inventory.pharmacy.sms;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;

public class SendSMS
{
    
    public static final String ACCOUNT_SID = "ACcf72e45278e8b05c8e67ff5b5a38d2e4";
    public static final String AUTH_TOKEN = "secretCode";
    
    public static String sendSms(String to, String from, String msg)
    {
        try
        {
            Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
            Message message = Message.creator(new PhoneNumber(to), new PhoneNumber(from), msg).create();
            System.out.println(message.getSid());
            System.out.println("message.price: "+message.getPrice());
            System.out.println("message.body: "+message.getBody());
            System.out.println("message.priceUnit: "+message.getPriceUnit());
            System.out.println("message.errorMessage: "+message.getErrorMessage());
    
            return "SMS Successfully Sent";
        }
        catch(Exception e)
        {
            System.out.println("Error Sending SMS "+e.getMessage());
            return "Error sending SMS!";
        }
        
        
        
    }
    
    public static void main(String[] args)
    {
        sendSms("+2348098423619", "+18509295655", "Thank you for your Patronage.");
    }
}
